package cn.iocoder.yudao.module.member.controller.app.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Schema(description = "用户 APP - 工单完工 Request VO")
@Data
public class AppWaterWorkOrderFinishReqVO {

    @Schema(description = "工单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "工单编号不能为空")
    private Long id;

    @Schema(description = "施工后图片")
    private List<String> afterImageUrls;

    @Schema(description = "施工后备注", example = "已完成施工")
    private String afterRemark;

    @Schema(description = "设备号（报装工单使用）", example = "WM10001")
    private String deviceNo;
}
