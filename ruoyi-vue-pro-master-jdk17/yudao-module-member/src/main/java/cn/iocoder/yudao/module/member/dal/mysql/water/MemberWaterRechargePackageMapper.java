package cn.iocoder.yudao.module.member.dal.mysql.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterRechargePackagePageReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterRechargePackageDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 居民报装充值套餐 Mapper
 */
@Mapper
public interface MemberWaterRechargePackageMapper extends BaseMapperX<MemberWaterRechargePackageDO> {

    default PageResult<MemberWaterRechargePackageDO> selectPage(MemberWaterRechargePackagePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MemberWaterRechargePackageDO>()
                .likeIfPresent(MemberWaterRechargePackageDO::getName, reqVO.getName())
                .eqIfPresent(MemberWaterRechargePackageDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(MemberWaterRechargePackageDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MemberWaterRechargePackageDO::getSort)
                .orderByDesc(MemberWaterRechargePackageDO::getId));
    }

    default List<MemberWaterRechargePackageDO> selectEnableList() {
        return selectList(new LambdaQueryWrapperX<MemberWaterRechargePackageDO>()
                .eq(MemberWaterRechargePackageDO::getStatus, 1)
                .orderByDesc(MemberWaterRechargePackageDO::getSort)
                .orderByDesc(MemberWaterRechargePackageDO::getId));
    }
}
