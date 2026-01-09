package cn.iocoder.yudao.module.member.service.water;

import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFeeBillDO;

/**
 * 水费结算服务
 */
public interface MemberWaterFeeSettleService {

    /**
     * 创建水费结算单并扣费
     *
     * @param bill 水费结算单
     * @param fee  扣费金额
     */
    void createBillAndReduceWallet(MemberWaterFeeBillDO bill, Integer fee);

}
