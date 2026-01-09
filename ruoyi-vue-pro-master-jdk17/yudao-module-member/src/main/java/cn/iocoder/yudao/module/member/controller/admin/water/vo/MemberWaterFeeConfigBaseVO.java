package cn.iocoder.yudao.module.member.controller.admin.water.vo;

import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.member.enums.water.MemberWaterFeeConfigStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 水费配置 Base VO
 */
@Data
public class MemberWaterFeeConfigBaseVO {

    @Schema(description = "每升单价（分）", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    @NotNull(message = "每升单价不能为空")
    private Integer pricePerLiter;

    @Schema(description = "状态（0 停用 1 启用）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    @InEnum(MemberWaterFeeConfigStatusEnum.class)
    private Integer status;

    @Schema(description = "备注", example = "默认单价")
    private String remark;
}
