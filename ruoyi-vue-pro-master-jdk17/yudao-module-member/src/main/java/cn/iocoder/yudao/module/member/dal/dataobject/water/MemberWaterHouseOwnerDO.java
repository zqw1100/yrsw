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
 * 居民报装房屋户主信息 DO
 */
@TableName(value = "member_water_house_owner", autoResultMap = true)
@KeySequence("member_water_house_owner_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberWaterHouseOwnerDO extends TenantBaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 报装房屋编号
     */
    private Long waterHouseId;
    /**
     * 申请编号
     */
    private Long applyId;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 户主姓名
     */
    private String ownerName;
    /**
     * 户主身份证号
     */
    private String ownerIdCard;
    /**
     * 合同图片地址列表
     */
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> contractImageUrls;
    /**
     * 备注
     */
    private String remark;
}
