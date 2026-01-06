package cn.iocoder.yudao.module.member.controller.app.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Schema(description = "用户 APP - 工单开工 Request VO")
@Data
public class AppWaterWorkOrderStartReqVO {

    @Schema(description = "工单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "工单编号不能为空")
    private Long id;

    @Schema(description = "施工前图片")
    private List<String> beforeImageUrls;

    @Schema(description = "施工前备注", example = "已核验现场情况")
    private String beforeRemark;
}
