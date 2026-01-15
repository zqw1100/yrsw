package cn.iocoder.yudao.module.member.controller.admin.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "管理后台 - 用户用水数据统计 Response VO")
@Data
public class MemberWaterDashboardRespVO {

    @Schema(description = "汇总信息")
    private Summary summary;

    @Schema(description = "近 7 日趋势")
    private List<Trend> dailyTrend;

    @Schema(description = "近 6 月趋势")
    private List<Trend> monthlyTrend;

    @Schema(description = "阀门状态分布")
    private List<PieItem> valveStatusStats;

    @Data
    public static class Summary {

        @Schema(description = "累计充值金额（元）")
        private BigDecimal totalRechargeAmount;

        @Schema(description = "累计赠送金额（元）")
        private BigDecimal totalRechargeBonusAmount;

        @Schema(description = "累计用水量（升）")
        private Long totalUsage;

        @Schema(description = "累计水费（元）")
        private BigDecimal totalFeeAmount;

        @Schema(description = "充值订单数")
        private Long rechargeCount;

        @Schema(description = "在用设备数")
        private Long deviceCount;

        @Schema(description = "低余额设备数")
        private Long lowBalanceDeviceCount;

        @Schema(description = "待处理报装数")
        private Long pendingApplyCount;

        @Schema(description = "待处理故障数")
        private Long pendingFaultCount;
    }

    @Data
    public static class Trend {

        @Schema(description = "统计时间")
        private String time;

        @Schema(description = "充值金额（元）")
        private BigDecimal rechargeAmount;

        @Schema(description = "用水量（升）")
        private Long usage;

        @Schema(description = "费用金额（元）")
        private BigDecimal feeAmount;
    }

    @Data
    public static class PieItem {

        @Schema(description = "名称")
        private String name;

        @Schema(description = "值")
        private Long value;
    }
}
