package cn.iocoder.yudao.module.member.service.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterFeeBillDailyStatsRespVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterFeeBillMonthlyStatsRespVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFeeBillPageReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFeeBillDO;

import java.util.List;

/**
 * 水费结算流水 Service
 */
public interface MemberWaterFeeBillService {

    MemberWaterFeeBillDO getFeeBill(Long id);

    PageResult<MemberWaterFeeBillDO> getFeeBillPage(MemberWaterFeeBillPageReqVO pageReqVO);

    List<AppWaterFeeBillDailyStatsRespVO> getDailyStats(String deviceNo, String month);

    List<AppWaterFeeBillMonthlyStatsRespVO> getMonthlyStats(String deviceNo, String year);
}
