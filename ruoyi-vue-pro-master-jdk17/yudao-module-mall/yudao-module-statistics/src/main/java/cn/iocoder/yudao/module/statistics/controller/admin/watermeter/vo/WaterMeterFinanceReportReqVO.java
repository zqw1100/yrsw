package cn.iocoder.yudao.module.statistics.controller.admin.watermeter.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;

@Schema(description = "管理后台 - 水表财务统计 Request VO")
@Data
public class WaterMeterFinanceReportReqVO {

    @Schema(description = "统计年份", example = "2025")
    private Integer year;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "县区")
    private String county;

    @Schema(description = "小区")
    private String community;

    @Schema(description = "报表日期范围", example = "[2026-01-01, 2026-01-31]")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private LocalDate[] dateRange;
}
