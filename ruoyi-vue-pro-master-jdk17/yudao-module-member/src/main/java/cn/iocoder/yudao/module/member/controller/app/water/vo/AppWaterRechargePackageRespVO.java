package cn.iocoder.yudao.module.member.controller.app.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "用户 APP - 居民报装充值套餐 Response VO")
@Data
public class AppWaterRechargePackageRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "套餐名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "首次充值300元")
    private String name;

    @Schema(description = "套餐售价", requiredMode = Schema.RequiredMode.REQUIRED, example = "300.00")
    private BigDecimal price;

    @Schema(description = "基础水量(升)", requiredMode = Schema.RequiredMode.REQUIRED, example = "300")
    private Integer waterVolume;

    @Schema(description = "赠送水量(升)", example = "20")
    private Integer giftWaterVolume;

    @Schema(description = "优惠金额", example = "25.00")
    private BigDecimal discountAmount;
}
