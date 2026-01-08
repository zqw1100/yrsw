package cn.iocoder.yudao.module.member.controller.admin.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 设备上传周期 Request VO")
@Data
public class MemberWaterDeviceUploadModeReqVO {

    @Schema(description = "设备编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "340119025153")
    @NotBlank(message = "设备编码不能为空")
    private String deviceCode;

    @Schema(description = "周期上传类型，1-分钟，2-小时，3-天", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "周期上传类型不能为空")
    @Min(value = 1, message = "周期上传类型不合法")
    @Max(value = 3, message = "周期上传类型不合法")
    private Integer uploadType;

    @Schema(description = "上传周期值", requiredMode = Schema.RequiredMode.REQUIRED, example = "23")
    @NotNull(message = "上传周期不能为空")
    @Min(value = 1, message = "上传周期不能小于 1")
    private Integer value;
}
