package cn.iocoder.yudao.module.member.dal.mysql.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDeviceHistoryPageReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterDeviceHistoryDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 居民设备用水历史 Mapper
 */
@Mapper
public interface MemberWaterDeviceHistoryMapper extends BaseMapperX<MemberWaterDeviceHistoryDO> {

    default PageResult<MemberWaterDeviceHistoryDO> selectPage(MemberWaterDeviceHistoryPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MemberWaterDeviceHistoryDO>()
                .likeIfPresent(MemberWaterDeviceHistoryDO::getDeviceNo, reqVO.getDeviceNo())
                .likeIfPresent(MemberWaterDeviceHistoryDO::getDeviceUserName, reqVO.getDeviceUserName())
                .likeIfPresent(MemberWaterDeviceHistoryDO::getDeviceAddress, reqVO.getDeviceAddress())
                .eqIfPresent(MemberWaterDeviceHistoryDO::getValveStatus, reqVO.getValveStatus())
                .eqIfPresent(MemberWaterDeviceHistoryDO::getReportReason, reqVO.getReportReason())
                .betweenIfPresent(MemberWaterDeviceHistoryDO::getDeviceUpdateTime, reqVO.getDeviceUpdateTime())
                .orderByDesc(MemberWaterDeviceHistoryDO::getId));
    }

    default MemberWaterDeviceHistoryDO selectLatestByDeviceNo(String deviceNo) {
        return selectOne(new LambdaQueryWrapperX<MemberWaterDeviceHistoryDO>()
                .eq(MemberWaterDeviceHistoryDO::getDeviceNo, deviceNo)
                .orderByDesc(MemberWaterDeviceHistoryDO::getDeviceUpdateTime)
                .orderByDesc(MemberWaterDeviceHistoryDO::getId)
                .last("LIMIT 1"));
    }
}
