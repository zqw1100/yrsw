package cn.iocoder.yudao.module.member.dal.mysql.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterWorkOrderPageReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterWorkOrderDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberWaterWorkOrderMapper extends BaseMapperX<MemberWaterWorkOrderDO> {

    default PageResult<MemberWaterWorkOrderDO> selectPage(AppWaterWorkOrderPageReqVO reqVO, Long workerId, boolean manager) {
        LambdaQueryWrapperX<MemberWaterWorkOrderDO> wrapper = new LambdaQueryWrapperX<MemberWaterWorkOrderDO>()
                .eqIfPresent(MemberWaterWorkOrderDO::getStatus, reqVO.getStatus())
                .eqIfPresent(MemberWaterWorkOrderDO::getOrderType, reqVO.getOrderType())
                .orderByDesc(MemberWaterWorkOrderDO::getId);
        if (!manager) {
            wrapper.eq(MemberWaterWorkOrderDO::getWorkerId, workerId);
        }
        return selectPage(reqVO, wrapper);
    }

    default MemberWaterWorkOrderDO selectByBiz(Integer orderType, Long bizId) {
        return selectOne(new LambdaQueryWrapperX<MemberWaterWorkOrderDO>()
                .eq(MemberWaterWorkOrderDO::getOrderType, orderType)
                .eq(MemberWaterWorkOrderDO::getBizId, bizId));
    }

    default java.util.List<MemberWaterWorkOrderDO> selectListByBizIds(Integer orderType, java.util.Collection<Long> bizIds) {
        return selectList(new LambdaQueryWrapperX<MemberWaterWorkOrderDO>()
                .eq(MemberWaterWorkOrderDO::getOrderType, orderType)
                .in(MemberWaterWorkOrderDO::getBizId, bizIds));
    }
}
