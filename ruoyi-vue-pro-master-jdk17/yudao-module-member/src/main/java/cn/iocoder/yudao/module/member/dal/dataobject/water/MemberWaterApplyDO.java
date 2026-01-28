package cn.iocoder.yudao.module.member.dal.dataobject.water;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 居民报装申请 DO
 */
@TableName("member_water_apply")
@KeySequence("member_water_apply_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberWaterApplyDO extends TenantBaseDO {

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
     * 地区编号（区县）
     */
    private Long areaId;
    /**
     * 地区名称
     */
    private String areaName;
    /**
     * 小区名称
     */
    private String communityName;
    /**
     * 小区编号
     */
    private String communityId;
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
     * 联系人姓名
     */
    private String contactName;
    /**
     * 联系人手机号
     */
    private String contactMobile;
    /**
     * 推荐人
     */
    private String referrer;
    /**
     * 申请状态（0 待补充资料 1 已提交）
     */
    private Integer applyStatus;
    /**
     * 处理状态
     */
    private Integer processStatus;
    /**
     * 设备号（物联网水表唯一编号）
     */
    private String deviceNo;
    /**
     * 首次充值套餐编号
     */
    private Long rechargePackageId;
    /**
     * 备注
     */
    private String remark;
}
