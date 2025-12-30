package cn.iocoder.yudao.module.member.controller.app.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "用户 APP - 居民报装申请创建 Request VO")
@Data
public class AppWaterApplyCreateReqVO {

    @Schema(description = "地区编号（区县）", requiredMode = Schema.RequiredMode.REQUIRED, example = "110105")
    @NotNull(message = "地区编号不能为空")
    private Long areaId;

    @Schema(description = "地区名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "北京市 朝阳区")
    @NotBlank(message = "地区名称不能为空")
    private String areaName;

    @Schema(description = "小区名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "城市花园")
    @NotBlank(message = "小区名称不能为空")
    private String communityName;

    @Schema(description = "楼栋名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "1号楼")
    @NotBlank(message = "楼栋名称不能为空")
    private String buildingName;

    @Schema(description = "单元名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "1单元")
    @NotBlank(message = "单元名称不能为空")
    private String unitName;

    @Schema(description = "房间号", requiredMode = Schema.RequiredMode.REQUIRED, example = "302")
    @NotBlank(message = "房间号不能为空")
    private String roomNo;

    @Schema(description = "联系人姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotBlank(message = "联系人不能为空")
    private String contactName;

    @Schema(description = "联系人手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "13800000000")
    @NotBlank(message = "联系人手机号不能为空")
    private String contactMobile;

    @Schema(description = "推荐人", example = "A001")
    private String referrer;
}
