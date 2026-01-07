package cn.iocoder.yudao.module.member.dal.mysql.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDevicePageReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterDeviceDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 居民设备信息 Mapper
 */
@Mapper
public interface MemberWaterDeviceMapper extends BaseMapperX<MemberWaterDeviceDO> {

    default MemberWaterDeviceDO selectByDeviceNo(String deviceNo) {
        return selectOne(new LambdaQueryWrapperX<MemberWaterDeviceDO>()
                .eq(MemberWaterDeviceDO::getDeviceNo, deviceNo));
    }

    default PageResult<MemberWaterDeviceDO> selectPage(MemberWaterDevicePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MemberWaterDeviceDO>()
                .likeIfPresent(MemberWaterDeviceDO::getDeviceNo, reqVO.getDeviceNo())
                .likeIfPresent(MemberWaterDeviceDO::getDeviceUserName, reqVO.getDeviceUserName())
                .likeIfPresent(MemberWaterDeviceDO::getDeviceAddress, reqVO.getDeviceAddress())
                .eqIfPresent(MemberWaterDeviceDO::getValveStatus, reqVO.getValveStatus())
                .betweenIfPresent(MemberWaterDeviceDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MemberWaterDeviceDO::getId));
    }
}
