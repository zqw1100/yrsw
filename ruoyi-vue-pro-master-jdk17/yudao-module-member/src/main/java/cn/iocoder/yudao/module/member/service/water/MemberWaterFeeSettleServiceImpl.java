package cn.iocoder.yudao.module.member.service.water;

import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFeeBillDO;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterFeeBillMapper;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import cn.iocoder.yudao.module.pay.enums.wallet.PayWalletBizTypeEnum;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;

/**
 * 水费结算服务实现
 */
@Service
public class MemberWaterFeeSettleServiceImpl implements MemberWaterFeeSettleService {

    @Resource
    private MemberWaterFeeBillMapper feeBillMapper;
    @Resource
    private PayWalletService payWalletService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createBillAndReduceWallet(MemberWaterFeeBillDO bill, Integer fee) {
        feeBillMapper.insert(bill);
        PayWalletTransactionDO transaction = payWalletService.reduceWalletBalance(bill.getWalletId(),
                bill.getId(), PayWalletBizTypeEnum.WATER_FEE, fee);
        MemberWaterFeeBillDO updateBill = new MemberWaterFeeBillDO();
        updateBill.setId(bill.getId());
        updateBill.setBalance(transaction.getBalance());
        feeBillMapper.updateById(updateBill);
        bill.setBalance(transaction.getBalance());
    }
}
