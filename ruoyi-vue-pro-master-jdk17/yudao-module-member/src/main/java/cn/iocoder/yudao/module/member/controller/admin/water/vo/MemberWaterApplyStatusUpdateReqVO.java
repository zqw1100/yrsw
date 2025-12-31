package cn.iocoder.yudao.module.member.controller.admin.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 居民报装申请状态更新 Request VO")
@Data
public class MemberWaterApplyStatusUpdateReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "编号不能为空")
    private Long id;

    @Schema(description = "处理状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "处理状态不能为空")
    private Integer processStatus;

    @Schema(description = "设备号（物联网水表唯一编号）", example = "WM10001")
    private String deviceNo;
}
