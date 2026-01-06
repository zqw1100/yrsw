package cn.iocoder.yudao.module.member.controller.app.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "用户 APP - 工单指派 Request VO")
@Data
public class AppWaterWorkOrderAssignReqVO {

    @Schema(description = "工单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "工单编号不能为空")
    private Long id;

    @Schema(description = "施工人员编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "施工人员编号不能为空")
    private Long workerId;
}
