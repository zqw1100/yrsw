package cn.iocoder.yudao.module.member.controller.app.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 APP - 设备月度水费统计 Response VO")
@Data
public class AppWaterFeeBillMonthlyStatsRespVO {

    @Schema(description = "月份")
    private Integer month;

    @Schema(description = "费用（分）")
    private Integer fee;
}
