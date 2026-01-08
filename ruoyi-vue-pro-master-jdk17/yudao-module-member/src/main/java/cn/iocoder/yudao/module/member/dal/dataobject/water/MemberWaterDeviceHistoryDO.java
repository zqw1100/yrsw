package cn.iocoder.yudao.module.member.dal.dataobject.water;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 居民设备用水历史 DO
 */
@TableName("member_water_device_history")
@KeySequence("member_water_device_history_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberWaterDeviceHistoryDO extends TenantBaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 设备号
     */
    private String deviceNo;
    /**
     * 设备用户地址
     */
    private String deviceAddress;
    /**
     * 设备所属用户名称
     */
    private String deviceUserName;
    /**
     * 设备时钟
     */
    private LocalDateTime deviceClock;
    /**
     * 数据抄收时间
     */
    private LocalDateTime deviceUpdateTime;
    /**
     * 信号强度
     */
    private Integer deviceRssi;
    /**
     * 购买次数
     */
    private Integer deviceBuyTimes;
    /**
     * 电池电压
     */
    private BigDecimal deviceVoltage;
    /**
     * 设备余额（分）
     */
    private Long deviceBalance;
    /**
     * 设备累计流量（升）
     */
    private Long deviceTotalData;
    /**
     * 结算日流量（升）
     */
    private Long deviceSettleDayData;
    /**
     * 上月使用流量（升）
     */
    private Long deviceLastData;
    /**
     * 结算日
     */
    private Integer deviceSettleDay;
    /**
     * 瞬时流量（升）
     */
    private Long deviceCurrentData;
    /**
     * 阀门状态
     */
    private Integer valveStatus;
    /**
     * 电池状态
     */
    private Integer voltageStatus;
    /**
     * 表端预付费费用状态
     */
    private Integer feeStatus;
    /**
     * 设备上报原因
     */
    private Integer reportReason;
    /**
     * 周期数据内容
     */
    private String cycleReportContent;
    /**
     * 周期数据类型
     */
    private Integer cycleReportType;
}
