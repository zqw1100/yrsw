package cn.iocoder.yudao.module.member.dal.dataobject.water;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * 居民报装充值套餐 DO
 */
@TableName("member_water_recharge_package")
@KeySequence("member_water_recharge_package_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberWaterRechargePackageDO extends TenantBaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 套餐名称
     */
    private String name;
    /**
     * 套餐售价
     */
    private BigDecimal price;
    /**
     * 基础水量(升)
     */
    private Integer waterVolume;
    /**
     * 赠送水量(升)
     */
    private Integer giftWaterVolume;
    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;
    /**
     * 状态（0 停用 1 启用）
     */
    private Integer status;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 备注
     */
    private String remark;
}
