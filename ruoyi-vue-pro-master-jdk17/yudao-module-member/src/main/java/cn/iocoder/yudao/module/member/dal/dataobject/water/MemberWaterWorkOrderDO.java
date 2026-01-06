package cn.iocoder.yudao.module.member.dal.dataobject.water;

import cn.iocoder.yudao.framework.mybatis.core.type.StringListTypeHandler;
import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 施工工单 DO
 */
@TableName(value = "member_water_work_order", autoResultMap = true)
@KeySequence("member_water_work_order_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberWaterWorkOrderDO extends TenantBaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 工单类型（0 报装 1 报修）
     */
    private Integer orderType;
    /**
     * 业务编号（报装申请或报修编号）
     */
    private Long bizId;
    /**
     * 施工状态（0 待接受 1 待施工 2 施工中 3 已完成）
     */
    private Integer status;
    /**
     * 指派施工人员编号
     */
    private Long workerId;
    /**
     * 指派时间
     */
    private LocalDateTime assignTime;
    /**
     * 接受时间
     */
    private LocalDateTime acceptTime;
    /**
     * 开始施工时间
     */
    private LocalDateTime startTime;
    /**
     * 完成时间
     */
    private LocalDateTime finishTime;
    /**
     * 施工前图片
     */
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> beforeImageUrls;
    /**
     * 施工前备注
     */
    private String beforeRemark;
    /**
     * 施工后图片
     */
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> afterImageUrls;
    /**
     * 施工后备注
     */
    private String afterRemark;
}
