package cn.iocoder.yudao.module.member.service.water;

import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDashboardRespVO;

/**
 * 用户用水数据统计 Service
 */
public interface MemberWaterDashboardService {

    /**
     * 获取用户用水统计数据
     *
     * @return 统计数据
     */
    MemberWaterDashboardRespVO getDashboard();
}
