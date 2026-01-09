package cn.iocoder.yudao.module.member.service.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDeviceHistoryPageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDeviceHistoryRespVO;

/**
 * 居民设备用水历史 Service
 */
public interface MemberWaterDeviceHistoryService {

    /**
     * 获得设备用水历史分页
     *
     * @param pageReqVO 分页查询
     * @return 分页结果
     */
    PageResult<MemberWaterDeviceHistoryRespVO> getHistoryPage(MemberWaterDeviceHistoryPageReqVO pageReqVO);
}
