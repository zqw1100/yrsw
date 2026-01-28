package cn.iocoder.yudao.module.system.dal.mysql.user;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.system.dal.dataobject.user.UserCommunityDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface UserCommunityMapper extends BaseMapperX<UserCommunityDO> {

    default List<UserCommunityDO> selectListByUserId(Long userId) {
        return selectList(UserCommunityDO::getUserId, userId);
    }

    default void deleteListByUserId(Long userId) {
        delete(new LambdaQueryWrapper<UserCommunityDO>().eq(UserCommunityDO::getUserId, userId));
    }

    default void deleteListByUserIdAndCommunityIds(Long userId, Collection<String> communityIds) {
        if (communityIds == null || communityIds.isEmpty()) {
            return;
        }
        delete(new LambdaQueryWrapper<UserCommunityDO>()
                .eq(UserCommunityDO::getUserId, userId)
                .in(UserCommunityDO::getCommunityId, communityIds));
    }
}
