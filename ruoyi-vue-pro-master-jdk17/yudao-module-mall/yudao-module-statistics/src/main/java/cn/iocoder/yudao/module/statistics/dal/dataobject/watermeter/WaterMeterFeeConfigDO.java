package cn.iocoder.yudao.module.statistics.dal.dataobject.watermeter;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 水表手续费配置 DO
 */
@TableName("water_meter_fee_config")
@KeySequence("water_meter_fee_config_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WaterMeterFeeConfigDO extends BaseDO {

    @TableId
    private Long id;

    /**
     * 手续费比例（如 0.006）
     */
    private BigDecimal feeRate;

    /**
     * 备注
     */
    private String remark;
}
