package cn.iocoder.yudao.module.member.job.water;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterApplyDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterDeviceDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterDeviceHistoryDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFeeBillDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFeeConfigDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFeeDeductFailDO;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterApplyMapper;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterDeviceHistoryMapper;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterDeviceMapper;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterFeeBillMapper;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterFeeConfigMapper;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterFeeDeductFailMapper;
import cn.iocoder.yudao.module.member.service.water.MemberWaterDeviceService;
import cn.iocoder.yudao.module.member.service.water.MemberWaterFeeSettleService;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.iocoder.yudao.module.pay.dal.mysql.wallet.PayWalletMapper;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletService;
import cn.iocoder.yudao.module.system.service.sms.SmsSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.module.pay.enums.ErrorCodeConstants.WALLET_BALANCE_NOT_ENOUGH;

/**
 * 水费结算 Job
 */
@Component
@Slf4j
public class MemberWaterFeeSettleJob implements JobHandler {

    private static final Integer VALVE_CLOSE_STATUS = 2;
    private static final Integer LOW_BALANCE_THRESHOLD = 2000;
    private static final String SMS_TEMPLATE_CODE_LOW_BALANCE = "SMS_500470233";

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
    @Resource
    private MemberWaterFeeSettleService memberWaterFeeSettleService;
    @Resource
    private MemberWaterFeeDeductFailMapper feeDeductFailMapper;
    @Resource
    private MemberWaterDeviceService deviceService;
    @Resource
    private SmsSendService smsSendService;

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
                    .communityId(device.getCommunityId())
                    .statDate(statDate)
                    .totalUsage(totalUsage)
                    .lastTotalUsage(lastTotalUsage)
                    .usageDiff(usageDiff)
                    .fee(fee)
                    .walletId(wallet.getId())
                    .build();

            if (fee <= 0) {
                feeBillMapper.insert(bill);
                MemberWaterFeeBillDO updateBill = new MemberWaterFeeBillDO();
                updateBill.setId(bill.getId());
                updateBill.setBalance(wallet.getBalance());
                feeBillMapper.updateById(updateBill);
                bill.setBalance(wallet.getBalance());
                sendLowBalanceSmsIfNeeded(deviceNo, wallet, statDate, bill.getBalance());
                billed++;
                continue;
            }

            try {
                memberWaterFeeSettleService.createBillAndReduceWallet(bill, fee);
                sendLowBalanceSmsIfNeeded(deviceNo, wallet, statDate, bill.getBalance());
                billed++;
            } catch (Exception ex) {
                log.warn("[execute][deviceNo({}) 扣费失败：{}]", deviceNo, ex.getMessage());
                MemberWaterFeeDeductFailDO failDO = new MemberWaterFeeDeductFailDO();
                failDO.setDeviceNo(deviceNo);
                failDO.setCommunityId(device.getCommunityId());
                failDO.setStatDate(statDate);
                failDO.setTotalUsage(totalUsage);
                failDO.setLastTotalUsage(lastTotalUsage);
                failDO.setUsageDiff(usageDiff);
                failDO.setFee(fee);
                failDO.setWalletId(wallet.getId());
                if (ex instanceof ServiceException serviceException) {
                    failDO.setErrorCode(serviceException.getCode());
                    failDO.setErrorMessage(serviceException.getMessage());
                    if (WALLET_BALANCE_NOT_ENOUGH.getCode().equals(serviceException.getCode())) {
                        boolean valveClosed = deviceService.operateValve(deviceNo, VALVE_CLOSE_STATUS);
                        if (!valveClosed) {
                            log.warn("[execute][deviceNo({}) 余额不足关阀失败]", deviceNo);
                        }
                    }
                } else {
                    failDO.setErrorMessage(ex.getMessage());
                }
                feeDeductFailMapper.insert(failDO);
            }
        }

        return StrUtil.format("水费结算完成，处理设备 {} 个，生成结算 {} 条", processed, billed);
    }

    private void sendLowBalanceSmsIfNeeded(String deviceNo, PayWalletDO wallet, LocalDate statDate, Integer balance) {
        if (balance == null || balance >= LOW_BALANCE_THRESHOLD) {
            return;
        }
        Long userId = wallet.getUserId();
        if (userId == null) {
            log.warn("[sendLowBalanceSmsIfNeeded][deviceNo({}) 余额不足但缺少用户编号]", deviceNo);
            return;
        }
        LocalDate startDate = statDate.withDayOfMonth(1);
        LocalDate endDate = statDate.withDayOfMonth(statDate.lengthOfMonth());
        int lowBalanceCount = feeBillMapper.countLowBalanceInMonth(deviceNo, startDate, endDate, LOW_BALANCE_THRESHOLD);
//        if (lowBalanceCount != 1) {
//            return;
//        }
        MemberWaterApplyDO apply = applyMapper.selectLatestByDeviceNo(deviceNo);
        String communityName = apply != null ? apply.getCommunityName() : null;
        if (StrUtil.isBlank(communityName)) {
            log.warn("[sendLowBalanceSmsIfNeeded][deviceNo({}) 缺少报装小区名称]", deviceNo);
            communityName = "";
        }
        int arrears = Math.max(LOW_BALANCE_THRESHOLD - balance, 0);
        smsSendService.sendSingleSmsToMember(null, userId, SMS_TEMPLATE_CODE_LOW_BALANCE,
                Map.of(
                        "communityName", communityName,
                        "deviceNo", deviceNo,
                        "amount", formatAmount(arrears)
                ));
    }

    private String formatAmount(Integer amount) {
        return BigDecimal.valueOf(amount)
                .movePointLeft(2)
                .setScale(2, RoundingMode.HALF_UP)
                .toPlainString();
    }

    private PayWalletDO findWallet(String deviceNo) {
        MemberWaterApplyDO apply = applyMapper.selectLatestByDeviceNo(deviceNo);
        if (apply != null && apply.getUserId() != null) {
            return payWalletService.getOrCreateWallet(apply.getUserId(), UserTypeEnum.MEMBER.getValue(), deviceNo);
        }
        return payWalletMapper.selectByDeviceNo(deviceNo);
    }
}
