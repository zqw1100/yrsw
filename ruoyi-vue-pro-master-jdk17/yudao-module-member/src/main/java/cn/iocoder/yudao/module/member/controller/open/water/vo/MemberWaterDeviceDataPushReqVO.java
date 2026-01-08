package cn.iocoder.yudao.module.member.controller.open.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "开放接口 - 水表设备抄收数据 Request VO")
@Data
public class MemberWaterDeviceDataPushReqVO {

    @Schema(description = "设备编码(表地址、表号)", example = "340180166222")
    private String deviceCode;

    @Schema(description = "设备时钟", example = "2021-09-30 06:14:18")
    private String deviceClock;

    @Schema(description = "信号强度", example = "13")
    private Integer deviceRSSI;

    @Schema(description = "购买次数", example = "0")
    private Integer deviceBuyTimes;

    @Schema(description = "电池电压", example = "3.56")
    private Double deviceVoltage;

    @Schema(description = "设备余额（分）", example = "0")
    private Long deviceBalance;

    @Schema(description = "设备累计流量（升）", example = "305")
    private Long deviceTotalData;

    @Schema(description = "结算日流量（升）", example = "300")
    private Long deviceSettleDayData;

    @Schema(description = "上月使用流量（升）", example = "300")
    private Long deviceLastData;

    @Schema(description = "结算日", example = "31")
    private Integer deviceSettleDay;

    @Schema(description = "瞬时流量（升）", example = "0")
    private Long deviceCurrentData;

    @Schema(description = "阀门状态(0 无阀控 1开-合 2 关-断 3 异常)", example = "0")
    private Integer valveStatus;

    @Schema(description = "电池状态(0 正常 1 欠压 3 异常)", example = "0")
    private Integer voltageStatus;

    @Schema(description = "表端预付费费用状态(0 正常 1 余额不足 2 欠费)", example = "0")
    private Integer feeStatus;

    @Schema(description = "设备周期数据")
    private CycleDataObject cycleDataObject;

    @Schema(description = "数据更新时间(毫秒时间戳)", example = "1632953664000")
    private Long updateTime;

    @Schema(description = "设备上报原因", example = "0")
    private Integer reportReason;

    @Schema(description = "设备地址")
    private String deviceAddress;

    @Schema(description = "设备用户名称")
    private String deviceUserName;

    @Data
    public static class CycleDataObject {

        @Schema(description = "设备周期数据", example = "{\"2021-09-30 06:00:00\": 0}")
        private String reportContent;

        @Schema(description = "周期数据类型", example = "5")
        private Integer reportType;
    }
}
