package cn.iocoder.yudao.module.member.controller.admin.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 设备换表 Request VO")
@Data
public class MemberWaterDeviceChangeReqVO {

    @Schema(description = "旧表设备编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "340119025153")
    @NotBlank(message = "旧表设备编码不能为空")
    private String originalDeviceCode;

    @Schema(description = "新表设备编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "340119025154")
    @NotBlank(message = "新表设备编码不能为空")
    private String newDeviceCode;

    @Schema(description = "旧表累计流量（升）", requiredMode = Schema.RequiredMode.REQUIRED, example = "23")
    @NotNull(message = "旧表累计流量不能为空")
    @Min(value = 0, message = "旧表累计流量不能小于 0")
    private Long originalTotalData;
}
