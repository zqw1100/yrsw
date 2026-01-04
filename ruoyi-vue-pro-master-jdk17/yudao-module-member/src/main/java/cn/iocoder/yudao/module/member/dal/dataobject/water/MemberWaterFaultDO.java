package cn.iocoder.yudao.module.member.dal.dataobject.water;

import cn.iocoder.yudao.framework.mybatis.core.type.StringListTypeHandler;
import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.List;

/**
 * 故障报修 DO
 */
@TableName(value = "member_water_fault", autoResultMap = true)
@KeySequence("member_water_fault_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberWaterFaultDO extends TenantBaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 报装房屋编号
     */
    private Long waterHouseId;
    /**
     * 报装申请编号
     */
    private Long applyId;
    /**
     * 户主姓名
     */
    private String ownerName;
    /**
     * 设备号
     */
    private String deviceNo;
    /**
     * 地区名称
     */
    private String areaName;
    /**
     * 小区名称
     */
    private String communityName;
    /**
     * 楼栋名称
     */
    private String buildingName;
    /**
     * 单元名称
     */
    private String unitName;
    /**
     * 房间号
     */
    private String roomNo;
    /**
     * 联系电话
     */
    private String contactMobile;
    /**
     * 故障原因
     */
    private String faultCode;
    /**
     * 问题反馈
     */
    private String feedback;
    /**
     * 优先级（1 低 2 中 3 高）
     */
    private Integer priority;
    /**
     * 故障图片
     */
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> imageUrls;
    /**
     * 处理状态（0 待处理 1 处理中 2 已完成）
     */
    private Integer processStatus;
    /**
     * 备注
     */
    private String remark;
}
