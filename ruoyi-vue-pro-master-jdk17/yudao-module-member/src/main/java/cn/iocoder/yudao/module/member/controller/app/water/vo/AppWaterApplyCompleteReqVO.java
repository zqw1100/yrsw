package cn.iocoder.yudao.module.member.controller.app.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Schema(description = "用户 APP - 居民报装申请补充资料 Request VO")
@Data
public class AppWaterApplyCompleteReqVO {

    @Schema(description = "申请编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "申请编号不能为空")
    private Long id;

    @Schema(description = "户主姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotBlank(message = "户主姓名不能为空")
    private String ownerName;

    @Schema(description = "户主身份证号", requiredMode = Schema.RequiredMode.REQUIRED, example = "110101199001011234")
    @NotBlank(message = "户主身份证号不能为空")
    private String ownerIdCard;

    @Schema(description = "合同图片地址列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "合同图片不能为空")
    private List<String> contractImageUrls;

    @Schema(description = "首次充值套餐编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long rechargePackageId;

    @Schema(description = "备注", example = "备注信息")
    private String remark;
}
