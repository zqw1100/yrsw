package cn.iocoder.yudao.module.member.controller.app.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Schema(description = "用户 APP - 设备日用水统计 Response VO")
@Data
public class AppWaterFeeBillDailyStatsRespVO {

    @Schema(description = "统计日期")
    private LocalDate statDate;

    @Schema(description = "总用水量（升）")
    private Long totalUsage;

    @Schema(description = "上次用水量（升）")
    private Long lastTotalUsage;

    @Schema(description = "用量差额（升）")
    private Long usageDiff;

    @Schema(description = "费用（分）")
    private Integer fee;
}
