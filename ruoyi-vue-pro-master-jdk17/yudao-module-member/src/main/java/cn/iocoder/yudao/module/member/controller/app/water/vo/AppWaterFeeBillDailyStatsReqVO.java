package cn.iocoder.yudao.module.member.controller.app.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Schema(description = "用户 APP - 设备日用水统计 Request VO")
@Data
public class AppWaterFeeBillDailyStatsReqVO {

    @Schema(description = "设备号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "设备号不能为空")
    private String deviceNo;

    @Schema(description = "统计月份，格式：YYYY-MM", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "统计月份不能为空")
    @Pattern(regexp = "\\d{4}-\\d{2}", message = "统计月份格式应为 YYYY-MM")
    private String month;
}
