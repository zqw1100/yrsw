package cn.iocoder.yudao.module.member.controller.admin.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 故障报修状态更新 Request VO")
@Data
public class MemberWaterFaultStatusUpdateReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "编号不能为空")
    private Long id;

    @Schema(description = "处理状态（0 待处理 1 处理中 2 已完成）", example = "1")
    private Integer processStatus;

    @Schema(description = "备注", example = "已安排人员处理")
    private String remark;
}
