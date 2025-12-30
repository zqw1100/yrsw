package cn.iocoder.yudao.module.member.controller.admin.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 居民报装充值套餐 Base VO
 */
@Data
public class MemberWaterRechargePackageBaseVO {

    @Schema(description = "套餐名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "首次充值300元")
    @NotBlank(message = "套餐名称不能为空")
    private String name;

    @Schema(description = "套餐售价", requiredMode = Schema.RequiredMode.REQUIRED, example = "300.00")
    @NotNull(message = "套餐售价不能为空")
    private BigDecimal price;

    @Schema(description = "基础水量(升)", requiredMode = Schema.RequiredMode.REQUIRED, example = "300")
    @NotNull(message = "基础水量不能为空")
    private Integer waterVolume;

    @Schema(description = "赠送水量(升)", example = "20")
    private Integer giftWaterVolume;

    @Schema(description = "优惠金额", example = "25.00")
    private BigDecimal discountAmount;

    @Schema(description = "状态（0 停用 1 启用）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "排序", example = "0")
    private Integer sort;

    @Schema(description = "备注", example = "首充套餐")
    private String remark;
}
