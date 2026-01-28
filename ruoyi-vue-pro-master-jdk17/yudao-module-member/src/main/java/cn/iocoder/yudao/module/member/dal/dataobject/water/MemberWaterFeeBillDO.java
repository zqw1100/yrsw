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

import java.time.LocalDate;

/**
 * 水费结算流水 DO
 */
@TableName("member_water_fee_bill")
@KeySequence("member_water_fee_bill_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberWaterFeeBillDO extends TenantBaseDO {

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
     * 小区编号
     */
    private String communityId;
    /**
     * 统计日期
     */
    private LocalDate statDate;
    /**
     * 总用水量（升）
     */
    private Long totalUsage;
    /**
     * 上次用水量（升）
     */
    private Long lastTotalUsage;
    /**
     * 用量差额（升）
     */
    private Long usageDiff;
    /**
     * 费用（分）
     */
    private Integer fee;
    /**
     * 余额（分）
     */
    private Integer balance;
    /**
     * 钱包编号
     */
    private Long walletId;
}
