package cn.iocoder.yudao.module.member.dal.mysql.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDevicePageReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterDeviceDO;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

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
                .eqIfPresent(MemberWaterDeviceDO::getCommunityId, reqVO.getCommunityId())
                .likeIfPresent(MemberWaterDeviceDO::getDeviceUserName, reqVO.getDeviceUserName())
                .likeIfPresent(MemberWaterDeviceDO::getDeviceAddress, reqVO.getDeviceAddress())
                .eqIfPresent(MemberWaterDeviceDO::getValveStatus, reqVO.getValveStatus())
                .betweenIfPresent(MemberWaterDeviceDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MemberWaterDeviceDO::getId));
    }

    default int updateDeviceNo(String oldDeviceNo, String newDeviceNo, LocalDateTime lastSyncTime) {
        if (StrUtil.isBlank(oldDeviceNo) || StrUtil.isBlank(newDeviceNo)) {
            return 0;
        }
        LambdaUpdateWrapper<MemberWaterDeviceDO> updateWrapper = new LambdaUpdateWrapper<MemberWaterDeviceDO>()
                .set(MemberWaterDeviceDO::getDeviceNo, newDeviceNo)
                .set(MemberWaterDeviceDO::getLastSyncTime, lastSyncTime)
                .eq(MemberWaterDeviceDO::getDeviceNo, oldDeviceNo);
        return update(null, updateWrapper);
    }
}
