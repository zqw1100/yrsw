package cn.iocoder.yudao.module.member.dal.mysql.water;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterApplyDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 居民报装申请 Mapper
 */
@Mapper
public interface MemberWaterApplyMapper extends BaseMapperX<MemberWaterApplyDO> {

    default MemberWaterApplyDO selectLatestByUserAndHouse(Long userId, Long waterHouseId, Integer applyStatus) {
        return selectOne(new LambdaQueryWrapperX<MemberWaterApplyDO>()
                .eq(MemberWaterApplyDO::getUserId, userId)
                .eq(MemberWaterApplyDO::getWaterHouseId, waterHouseId)
                .eqIfPresent(MemberWaterApplyDO::getApplyStatus, applyStatus)
                .orderByDesc(MemberWaterApplyDO::getId)
                .last("LIMIT 1"));
    }
}
