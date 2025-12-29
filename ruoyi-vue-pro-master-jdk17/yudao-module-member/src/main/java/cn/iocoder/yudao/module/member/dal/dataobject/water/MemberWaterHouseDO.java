package cn.iocoder.yudao.module.member.dal.dataobject.water;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 居民报装房屋信息 DO
 */
@TableName("member_water_house")
@KeySequence("member_water_house_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberWaterHouseDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
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
     * 报装状态（0 未报装 1 已报装）
     */
    private Integer installStatus;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 备注
     */
    private String remark;

}
