package cn.iocoder.yudao.module.member.controller.admin.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 设备阀门操作 Request VO")
@Data
public class MemberWaterDeviceValveReqVO {

    @Schema(description = "设备号", requiredMode = Schema.RequiredMode.REQUIRED, example = "340119025153")
    @NotBlank(message = "设备号不能为空")
    private String deviceNo;

    @Schema(description = "阀门状态，1 开阀 2 关阀", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "阀门状态不能为空")
    @Min(value = 1, message = "阀门状态不合法")
    @Max(value = 2, message = "阀门状态不合法")
    private Integer valveStatus;
}
