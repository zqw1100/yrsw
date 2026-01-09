package cn.iocoder.yudao.module.member.dal.mysql.water;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterApplyPageReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterApplyPageReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterApplyDO;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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

    default MemberWaterApplyDO selectLatestByUserWithDeviceNo(Long userId) {
        return selectOne(new LambdaQueryWrapperX<MemberWaterApplyDO>()
                .eq(MemberWaterApplyDO::getUserId, userId)
                .isNotNull(MemberWaterApplyDO::getDeviceNo)
                .ne(MemberWaterApplyDO::getDeviceNo, "")
                .orderByDesc(MemberWaterApplyDO::getId)
                .last("LIMIT 1"));
    }

    default MemberWaterApplyDO selectLatestByUserAndDeviceNo(Long userId, String deviceNo) {
        return selectOne(new LambdaQueryWrapperX<MemberWaterApplyDO>()
                .eq(MemberWaterApplyDO::getUserId, userId)
                .eq(MemberWaterApplyDO::getDeviceNo, deviceNo)
                .orderByDesc(MemberWaterApplyDO::getId)
                .last("LIMIT 1"));
    }

    default MemberWaterApplyDO selectLatestByDeviceNo(String deviceNo) {
        return selectOne(new LambdaQueryWrapperX<MemberWaterApplyDO>()
                .eq(MemberWaterApplyDO::getDeviceNo, deviceNo)
                .orderByDesc(MemberWaterApplyDO::getId)
                .last("LIMIT 1"));
    }

    default PageResult<MemberWaterApplyDO> selectPage(MemberWaterApplyPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MemberWaterApplyDO>()
                .eqIfPresent(MemberWaterApplyDO::getAreaId, reqVO.getAreaId())
                .likeIfPresent(MemberWaterApplyDO::getCommunityName, reqVO.getCommunityName())
                .likeIfPresent(MemberWaterApplyDO::getBuildingName, reqVO.getBuildingName())
                .likeIfPresent(MemberWaterApplyDO::getUnitName, reqVO.getUnitName())
                .likeIfPresent(MemberWaterApplyDO::getRoomNo, reqVO.getRoomNo())
                .likeIfPresent(MemberWaterApplyDO::getContactName, reqVO.getContactName())
                .likeIfPresent(MemberWaterApplyDO::getContactMobile, reqVO.getContactMobile())
                .eqIfPresent(MemberWaterApplyDO::getApplyStatus, reqVO.getApplyStatus())
                .eqIfPresent(MemberWaterApplyDO::getProcessStatus, reqVO.getProcessStatus())
                .betweenIfPresent(MemberWaterApplyDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MemberWaterApplyDO::getId));
    }

    default PageResult<MemberWaterApplyDO> selectPageByUser(Long userId, AppWaterApplyPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MemberWaterApplyDO>()
                .eq(MemberWaterApplyDO::getUserId, userId)
                .eqIfPresent(MemberWaterApplyDO::getApplyStatus, reqVO.getApplyStatus())
                .eqIfPresent(MemberWaterApplyDO::getProcessStatus, reqVO.getProcessStatus())
                .betweenIfPresent(MemberWaterApplyDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MemberWaterApplyDO::getId));
    }

    default boolean existsByDeviceNo(String deviceNo, Long excludeApplyId) {
        return selectCount(new LambdaQueryWrapperX<MemberWaterApplyDO>()
                .eq(MemberWaterApplyDO::getDeviceNo, deviceNo)
                .neIfPresent(MemberWaterApplyDO::getId, excludeApplyId)) > 0;
    }

    default int updateDeviceNo(String oldDeviceNo, String newDeviceNo) {
        if (StrUtil.isBlank(oldDeviceNo) || StrUtil.isBlank(newDeviceNo)) {
            return 0;
        }
        LambdaUpdateWrapper<MemberWaterApplyDO> updateWrapper = new LambdaUpdateWrapper<MemberWaterApplyDO>()
                .set(MemberWaterApplyDO::getDeviceNo, newDeviceNo)
                .eq(MemberWaterApplyDO::getDeviceNo, oldDeviceNo);
        return update(null, updateWrapper);
    }
}
