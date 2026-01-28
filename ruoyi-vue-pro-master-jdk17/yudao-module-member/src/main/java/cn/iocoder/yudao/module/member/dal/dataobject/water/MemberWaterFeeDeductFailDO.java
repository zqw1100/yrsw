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
 * 水费扣费失败记录 DO
 */
@TableName("member_water_fee_deduct_fail")
@KeySequence("member_water_fee_deduct_fail_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberWaterFeeDeductFailDO extends TenantBaseDO {

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
     * 钱包编号
     */
    private Long walletId;
    /**
     * 错误码
     */
    private Integer errorCode;
    /**
     * 错误信息
     */
    private String errorMessage;
}
