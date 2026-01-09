package cn.iocoder.yudao.module.member.controller.admin.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 设备用水历史 Response VO")
@Data
public class MemberWaterDeviceHistoryRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "设备号", example = "340119025153")
    private String deviceNo;

    @Schema(description = "设备用户地址", example = "某小区 1 栋 101")
    private String deviceAddress;

    @Schema(description = "设备所属用户名称", example = "张三")
    private String deviceUserName;

    @Schema(description = "设备时钟")
    private LocalDateTime deviceClock;

    @Schema(description = "数据抄收时间")
    private LocalDateTime deviceUpdateTime;

    @Schema(description = "信号强度", example = "100")
    private Integer deviceRssi;

    @Schema(description = "购买次数", example = "52")
    private Integer deviceBuyTimes;

    @Schema(description = "电池电压", example = "3.18")
    private BigDecimal deviceVoltage;

    @Schema(description = "设备余额（分）", example = "50000")
    private Long deviceBalance;

    @Schema(description = "设备累计流量（升）", example = "1200")
    private Long deviceTotalData;

    @Schema(description = "结算日流量（升）", example = "300")
    private Long deviceSettleDayData;

    @Schema(description = "上月使用流量（升）", example = "200")
    private Long deviceLastData;

    @Schema(description = "结算日", example = "31")
    private Integer deviceSettleDay;

    @Schema(description = "瞬时流量（升）", example = "10")
    private Long deviceCurrentData;

    @Schema(description = "阀门状态", example = "1")
    private Integer valveStatus;

    @Schema(description = "电池状态", example = "0")
    private Integer voltageStatus;

    @Schema(description = "费用状态", example = "0")
    private Integer feeStatus;

    @Schema(description = "上报原因", example = "0")
    private Integer reportReason;

    @Schema(description = "周期数据类型", example = "5")
    private Integer cycleReportType;

    @Schema(description = "周期数据内容")
    private String cycleReportContent;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
