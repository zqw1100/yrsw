package cn.iocoder.yudao.module.member.service.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDevicePageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDeviceRespVO;
import cn.iocoder.yudao.module.member.controller.open.water.vo.MemberWaterDeviceDataPushReqVO;

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
     * 接收设备抄收数据
     *
     * @param reqVO 抄收数据
     */
    void syncDeviceData(MemberWaterDeviceDataPushReqVO reqVO);

    /**
     * 刷新设备信息
     *
     * @param id 设备编号
     */
    void refreshDevice(Long id);

    /**
     * 阀门操作
     *
     * @param deviceNo 设备号
     * @param valveStatus 阀门状态
     * @return 是否成功
     */
    boolean operateValve(String deviceNo, Integer valveStatus);

    /**
     * 换表
     *
     * @param originalDeviceCode 旧表设备编码
     * @param newDeviceCode 新表设备编码
     * @param originalTotalData 旧表累计流量
     * @return 是否成功
     */
    boolean changeDevice(String originalDeviceCode, String newDeviceCode, Long originalTotalData);

    /**
     * 设置设备上传周期
     *
     * @param deviceCode 设备编码
     * @param uploadType 上传类型
     * @param value 上传周期
     * @return 是否成功
     */
    boolean setUploadMode(String deviceCode, Integer uploadType, Integer value);
}
