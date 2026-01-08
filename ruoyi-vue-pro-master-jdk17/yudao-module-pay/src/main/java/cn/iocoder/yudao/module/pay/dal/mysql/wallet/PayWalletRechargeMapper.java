package cn.iocoder.yudao.module.pay.dal.mysql.wallet;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletRechargeDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletRechargePackageDO;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayWalletRechargeMapper extends BaseMapperX<PayWalletRechargeDO> {

    default int updateByIdAndPaid(Long id, boolean wherePayStatus, PayWalletRechargeDO updateObj) {
        return update(updateObj, new LambdaQueryWrapperX<PayWalletRechargeDO>()
                .eq(PayWalletRechargeDO::getId, id).eq(PayWalletRechargeDO::getPayStatus, wherePayStatus));
    }

    default int updateByIdAndRefunded(Long id, Integer whereRefundStatus, PayWalletRechargeDO updateObj) {
        return update(updateObj, new LambdaQueryWrapperX<PayWalletRechargeDO>()
                .eq(PayWalletRechargeDO::getId, id).eq(PayWalletRechargeDO::getRefundStatus, whereRefundStatus));
    }

    default PageResult<PayWalletRechargeDO> selectPage(PageParam pageReqVO, Long walletId, Boolean payStatus) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<PayWalletRechargeDO>()
                .eq(PayWalletRechargeDO::getWalletId, walletId)
                .eq(PayWalletRechargeDO::getPayStatus, payStatus)
                .orderByDesc(PayWalletRechargeDO::getId));
    }

    default int updateDeviceNo(String oldDeviceNo, String newDeviceNo) {
        if (StrUtil.isBlank(oldDeviceNo) || StrUtil.isBlank(newDeviceNo)) {
            return 0;
        }
        LambdaUpdateWrapper<PayWalletRechargeDO> updateWrapper = new LambdaUpdateWrapper<PayWalletRechargeDO>()
                .set(PayWalletRechargeDO::getDeviceNo, newDeviceNo)
                .eq(PayWalletRechargeDO::getDeviceNo, oldDeviceNo);
        return update(null, updateWrapper);
    }

}
