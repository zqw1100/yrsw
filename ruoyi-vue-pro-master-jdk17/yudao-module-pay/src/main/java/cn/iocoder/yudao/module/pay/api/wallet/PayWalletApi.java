package cn.iocoder.yudao.module.pay.api.wallet;

import cn.iocoder.yudao.module.pay.api.wallet.dto.PayWalletAddBalanceReqDTO;
import cn.iocoder.yudao.module.pay.api.wallet.dto.PayWalletRespDTO;

/**
 * 钱包 API 接口
 *
 * @author liurulin
 */
public interface PayWalletApi {

    /**
     * 添加钱包余额
     *
     * @param reqDTO 增加余额请求
     */
    void addWalletBalance(PayWalletAddBalanceReqDTO reqDTO);

    /**
     * 获取钱包信息
     *
     * @param userId 用户编号
     * @param userType 用户类型
     * @param deviceNo 设备号
     * @return 钱包信息
     */
    PayWalletRespDTO getOrCreateWallet(Long userId, Integer userType, String deviceNo);

    /**
     * 获取钱包信息（兼容旧接口）
     *
     * @param userId 用户编号
     * @param userType 用户类型
     * @return 钱包信息
     */
    default PayWalletRespDTO getOrCreateWallet(Long userId, Integer userType) {
        return getOrCreateWallet(userId, userType, null);
    }

}
