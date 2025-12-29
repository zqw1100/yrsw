package cn.iocoder.yudao.module.member.controller.admin.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 居民报装房屋信息 Base VO
 */
@Data
public class MemberWaterHouseBaseVO {

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

    @Schema(description = "报装状态（0 未报装 1 已报装）", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "报装状态不能为空")
    private Integer installStatus;

    @Schema(description = "排序", example = "0")
    private Integer sort;

    @Schema(description = "备注", example = "一期")
    private String remark;

}
