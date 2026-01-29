package cn.iocoder.yudao.module.statistics.service.watermeter;

import cn.iocoder.yudao.module.statistics.controller.admin.watermeter.vo.WaterMeterFinanceDailyRespVO;
import cn.iocoder.yudao.module.statistics.controller.admin.watermeter.vo.WaterMeterFinanceMonthlyRespVO;
import cn.iocoder.yudao.module.statistics.controller.admin.watermeter.vo.WaterMeterFinanceReportReqVO;

import java.util.List;

public interface WaterMeterFinanceReportService {

    List<WaterMeterFinanceMonthlyRespVO> getMonthlyReport(WaterMeterFinanceReportReqVO reqVO);

    List<WaterMeterFinanceDailyRespVO> getDailyReport(WaterMeterFinanceReportReqVO reqVO);
}
