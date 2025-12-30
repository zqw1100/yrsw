package cn.iocoder.yudao.module.member.dal.mysql.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterHousePageReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterHouseDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 居民报装房屋信息 Mapper
 */
@Mapper
public interface MemberWaterHouseMapper extends BaseMapperX<MemberWaterHouseDO> {

    default PageResult<MemberWaterHouseDO> selectPage(MemberWaterHousePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MemberWaterHouseDO>()
                .eqIfPresent(MemberWaterHouseDO::getAreaId, reqVO.getAreaId())
                .likeIfPresent(MemberWaterHouseDO::getCommunityName, reqVO.getCommunityName())
                .likeIfPresent(MemberWaterHouseDO::getBuildingName, reqVO.getBuildingName())
                .likeIfPresent(MemberWaterHouseDO::getUnitName, reqVO.getUnitName())
                .likeIfPresent(MemberWaterHouseDO::getRoomNo, reqVO.getRoomNo())
                .eqIfPresent(MemberWaterHouseDO::getInstallStatus, reqVO.getInstallStatus())
                .betweenIfPresent(MemberWaterHouseDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MemberWaterHouseDO::getId));
    }

}
