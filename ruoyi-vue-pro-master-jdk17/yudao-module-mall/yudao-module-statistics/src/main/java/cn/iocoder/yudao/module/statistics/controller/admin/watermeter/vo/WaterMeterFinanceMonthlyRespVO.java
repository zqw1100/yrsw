package cn.iocoder.yudao.module.statistics.controller.admin.watermeter.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 水表财务统计月报 Response VO")
@Data
@ExcelIgnoreUnannotated
public class WaterMeterFinanceMonthlyRespVO {

    @Schema(description = "城市", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("城市")
    private String city;

    @Schema(description = "县区", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("县区")
    private String county;

    @Schema(description = "小区", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("小区")
    private String community;

    @Schema(description = "年份", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("年份")
    private Integer year;

    @Schema(description = "月份", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("月份")
    private String month;

    @Schema(description = "计费天数", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("计费天数")
    private Integer billingDays;

    @Schema(description = "订单总量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("订单总量")
    private Integer orderCount;

    @Schema(description = "订单金额汇总(元)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("订单金额汇总(元)")
    private Double orderAmount;

    @Schema(description = "订单配送金额(元)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("订单配送金额(元)")
    private Double deliveryAmount;

    @Schema(description = "入账金额汇总(元)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("入账金额汇总(元)")
    private Double paidAmount;

    @Schema(description = "交易单数", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("交易单数")
    private Integer wechatCount;

    @Schema(description = "交易总额(元)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("交易总额(元)")
    private Double wechatAmount;

    @Schema(description = "手续费总额(元)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("手续费总额(元)")
    private Double wechatFee;

    @Schema(description = "退款总金额(元)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("退款总金额(元)")
    private Double refundAmount;
}
