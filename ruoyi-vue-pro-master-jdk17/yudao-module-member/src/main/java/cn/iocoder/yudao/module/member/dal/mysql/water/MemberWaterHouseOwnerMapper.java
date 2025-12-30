package cn.iocoder.yudao.module.member.dal.mysql.water;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterHouseOwnerDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * 居民报装房屋户主信息 Mapper
 */
@Mapper
public interface MemberWaterHouseOwnerMapper extends BaseMapperX<MemberWaterHouseOwnerDO> {

    default MemberWaterHouseOwnerDO selectByApplyId(Long applyId) {
        return selectOne(new LambdaQueryWrapperX<MemberWaterHouseOwnerDO>()
                .eq(MemberWaterHouseOwnerDO::getApplyId, applyId));
    }

    default List<MemberWaterHouseOwnerDO> selectListByApplyIds(Collection<Long> applyIds) {
        return selectList(new LambdaQueryWrapperX<MemberWaterHouseOwnerDO>()
                .in(MemberWaterHouseOwnerDO::getApplyId, applyIds));
    }
}
