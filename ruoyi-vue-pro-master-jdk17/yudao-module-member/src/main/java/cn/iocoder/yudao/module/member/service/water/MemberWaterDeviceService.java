package cn.iocoder.yudao.module.member.service.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDevicePageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDeviceRespVO;

/**
 * 居民设备信息 Service
 */
public interface MemberWaterDeviceService {

    /**
     * 获得设备分页
     *
     * @param pageReqVO 分页查询
     * @return 分页结果
     */
    PageResult<MemberWaterDeviceRespVO> getDevicePage(MemberWaterDevicePageReqVO pageReqVO);

    /**
     * 注册或更新设备信息
     *
     * @param deviceNo 设备号
     */
    void registerOrUpdateDevice(String deviceNo);

    /**
     * 绑定报装信息并注册设备
     *
     * @param apply 报装信息
     * @param deviceNo 设备号
     */
    void registerDeviceForApply(cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterApplyDO apply, String deviceNo);

    /**
     * 刷新设备信息
     *
     * @param id 设备编号
     */
    void refreshDevice(Long id);
}
