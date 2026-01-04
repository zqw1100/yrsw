package cn.iocoder.yudao.module.member.controller.app.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Schema(description = "用户 APP - 故障报修创建 Request VO")
@Data
public class AppWaterFaultCreateReqVO {

    @Schema(description = "联系电话", requiredMode = Schema.RequiredMode.REQUIRED, example = "13800000000")
    @NotBlank(message = "联系电话不能为空")
    private String contactMobile;

    @Schema(description = "故障原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "leak")
    @NotBlank(message = "故障原因不能为空")
    private String faultCode;

    @Schema(description = "问题反馈", requiredMode = Schema.RequiredMode.REQUIRED, example = "设备漏水")
    @NotBlank(message = "问题反馈不能为空")
    private String feedback;

    @Schema(description = "优先级（1低 2中 3高）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "优先级不能为空")
    private Integer priority;

    @Schema(description = "故障图片列表", example = "[\"https://example.com/img.png\"]")
    private List<String> imageUrls;
}
