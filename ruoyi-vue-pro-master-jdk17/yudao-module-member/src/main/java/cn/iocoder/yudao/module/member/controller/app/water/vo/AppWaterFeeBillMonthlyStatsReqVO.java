package cn.iocoder.yudao.module.member.controller.app.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Schema(description = "用户 APP - 设备月度水费统计 Request VO")
@Data
public class AppWaterFeeBillMonthlyStatsReqVO {

    @Schema(description = "设备号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "设备号不能为空")
    private String deviceNo;

    @Schema(description = "统计年份，格式：YYYY", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "统计年份不能为空")
    @Pattern(regexp = "\\\\d{4}", message = "统计年份格式应为 YYYY")
    private String year;
}
