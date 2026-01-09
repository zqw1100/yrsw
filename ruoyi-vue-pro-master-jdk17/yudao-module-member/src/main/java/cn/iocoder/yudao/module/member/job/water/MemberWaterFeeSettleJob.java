package cn.iocoder.yudao.module.member.job.water;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterApplyDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterDeviceDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterDeviceHistoryDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFeeBillDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFeeConfigDO;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterApplyMapper;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterDeviceHistoryMapper;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterDeviceMapper;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterFeeBillMapper;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterFeeConfigMapper;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import cn.iocoder.yudao.module.pay.dal.mysql.wallet.PayWalletMapper;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletBizTypeEnum;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * 水费结算 Job
 */
@Component
@Slf4j
public class MemberWaterFeeSettleJob implements JobHandler {

    @Resource
    private MemberWaterDeviceMapper deviceMapper;
    @Resource
    private MemberWaterDeviceHistoryMapper deviceHistoryMapper;
    @Resource
    private MemberWaterFeeConfigMapper feeConfigMapper;
    @Resource
    private MemberWaterFeeBillMapper feeBillMapper;
    @Resource
    private MemberWaterApplyMapper applyMapper;
    @Resource
    private PayWalletService payWalletService;
    @Resource
    private PayWalletMapper payWalletMapper;

    @Override
    @TenantJob
    public String execute(String param) {
        MemberWaterFeeConfigDO config = feeConfigMapper.selectLatestEnabled();
        if (config == null || config.getPricePerLiter() == null || config.getPricePerLiter() <= 0) {
            return "未配置水费单价";
        }

        Integer pricePerLiter = config.getPricePerLiter();
        LocalDate statDate = LocalDate.now();
        List<MemberWaterDeviceDO> devices = deviceMapper.selectList();
        int processed = 0;
        int billed = 0;
        for (MemberWaterDeviceDO device : devices) {
            processed++;
            String deviceNo = device.getDeviceNo();
            MemberWaterDeviceHistoryDO history = deviceHistoryMapper.selectLatestByDeviceNo(deviceNo);
            if (history == null || history.getDeviceTotalData() == null) {
                continue;
            }

            long totalUsage = history.getDeviceTotalData();
            MemberWaterFeeBillDO lastBill = feeBillMapper.selectLatestByDeviceNo(deviceNo);
            long lastTotalUsage = lastBill != null && lastBill.getTotalUsage() != null ? lastBill.getTotalUsage() : 0L;
            long usageDiff = totalUsage - lastTotalUsage;
            if (usageDiff < 0) {
                log.warn("[execute][deviceNo({}) totalUsage({}) 小于上次用量({})]", deviceNo, totalUsage, lastTotalUsage);
                usageDiff = 0L;
            }

            long feeAmount = usageDiff * pricePerLiter;
            int fee = feeAmount > Integer.MAX_VALUE ? Integer.MAX_VALUE : Math.toIntExact(feeAmount);

            PayWalletDO wallet = findWallet(deviceNo);
            if (wallet == null) {
                log.warn("[execute][deviceNo({}) 未找到钱包，跳过扣费]", deviceNo);
                continue;
            }

            MemberWaterFeeBillDO bill = MemberWaterFeeBillDO.builder()
                    .deviceNo(deviceNo)
                    .statDate(statDate)
                    .totalUsage(totalUsage)
                    .lastTotalUsage(lastTotalUsage)
                    .usageDiff(usageDiff)
                    .fee(fee)
                    .walletId(wallet.getId())
                    .build();
            feeBillMapper.insert(bill);
            billed++;

            if (fee <= 0) {
                MemberWaterFeeBillDO updateBill = new MemberWaterFeeBillDO();
                updateBill.setId(bill.getId());
                updateBill.setBalance(wallet.getBalance());
                feeBillMapper.updateById(updateBill);
                continue;
            }

            try {
                PayWalletTransactionDO transaction = payWalletService.reduceWalletBalance(wallet.getId(),
                        bill.getId(), PayWalletBizTypeEnum.WATER_FEE, fee);
                MemberWaterFeeBillDO updateBill = new MemberWaterFeeBillDO();
                updateBill.setId(bill.getId());
                updateBill.setBalance(transaction.getBalance());
                feeBillMapper.updateById(updateBill);
            } catch (Exception ex) {
                log.warn("[execute][deviceNo({}) 扣费失败：{}]", deviceNo, ex.getMessage());
                MemberWaterFeeBillDO updateBill = new MemberWaterFeeBillDO();
                updateBill.setId(bill.getId());
                updateBill.setBalance(wallet.getBalance());
                feeBillMapper.updateById(updateBill);
            }
        }

        return StrUtil.format("水费结算完成，处理设备 {} 个，生成结算 {} 条", processed, billed);
    }

    private PayWalletDO findWallet(String deviceNo) {
        MemberWaterApplyDO apply = applyMapper.selectLatestByDeviceNo(deviceNo);
        if (apply != null && apply.getUserId() != null) {
            return payWalletService.getOrCreateWallet(apply.getUserId(), UserTypeEnum.MEMBER.getValue(), deviceNo);
        }
        return payWalletMapper.selectByDeviceNo(deviceNo);
    }
}
