package cn.iocoder.yudao.module.statistics.controller.admin.watermeter;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.statistics.controller.admin.watermeter.vo.WaterMeterFinanceDailyRespVO;
import cn.iocoder.yudao.module.statistics.controller.admin.watermeter.vo.WaterMeterFinanceMonthlyRespVO;
import cn.iocoder.yudao.module.statistics.controller.admin.watermeter.vo.WaterMeterFinanceReportReqVO;
import cn.iocoder.yudao.module.statistics.service.watermeter.WaterMeterFinanceReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 水表财务统计")
@RestController
@RequestMapping("/statistics/water-meter/finance")
@Validated
public class WaterMeterFinanceReportController {

    @Resource
    private WaterMeterFinanceReportService waterMeterFinanceReportService;

    @GetMapping("/month-list")
    @Operation(summary = "获得水表财务统计月报")
    @PreAuthorize("@ss.hasPermission('statistics:water-meter-finance:query')")
    public CommonResult<List<WaterMeterFinanceMonthlyRespVO>> getMonthlyList(WaterMeterFinanceReportReqVO reqVO) {
        return success(waterMeterFinanceReportService.getMonthlyReport(reqVO));
    }

    @GetMapping("/day-list")
    @Operation(summary = "获得水表财务统计日报")
    @PreAuthorize("@ss.hasPermission('statistics:water-meter-finance:query')")
    public CommonResult<List<WaterMeterFinanceDailyRespVO>> getDailyList(WaterMeterFinanceReportReqVO reqVO) {
        return success(waterMeterFinanceReportService.getDailyReport(reqVO));
    }

    @GetMapping("/export-month-excel")
    @Operation(summary = "导出水表财务统计月报 Excel")
    @PreAuthorize("@ss.hasPermission('statistics:water-meter-finance:export')")
    public void exportMonthlyExcel(WaterMeterFinanceReportReqVO reqVO, HttpServletResponse response) throws IOException {
        List<WaterMeterFinanceMonthlyRespVO> list = waterMeterFinanceReportService.getMonthlyReport(reqVO);
        ExcelUtils.write(response, "水表财务统计月报.xls", "数据", WaterMeterFinanceMonthlyRespVO.class, list);
    }

    @GetMapping("/export-day-excel")
    @Operation(summary = "导出水表财务统计日报 Excel")
    @PreAuthorize("@ss.hasPermission('statistics:water-meter-finance:export')")
    public void exportDailyExcel(WaterMeterFinanceReportReqVO reqVO, HttpServletResponse response) throws IOException {
        List<WaterMeterFinanceDailyRespVO> list = waterMeterFinanceReportService.getDailyReport(reqVO);
        ExcelUtils.write(response, "水表财务统计日报.xls", "数据", WaterMeterFinanceDailyRespVO.class, list);
    }
}
