package cn.iocoder.yudao.module.member.dal.mysql.water;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFeeConfigPageReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFeeConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 水费配置 Mapper
 */
@Mapper
public interface MemberWaterFeeConfigMapper extends BaseMapperX<MemberWaterFeeConfigDO> {

    default PageResult<MemberWaterFeeConfigDO> selectPage(MemberWaterFeeConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MemberWaterFeeConfigDO>()
                .eqIfPresent(MemberWaterFeeConfigDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(MemberWaterFeeConfigDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MemberWaterFeeConfigDO::getId));
    }

    default MemberWaterFeeConfigDO selectLatestEnabled() {
        return selectOne(new LambdaQueryWrapperX<MemberWaterFeeConfigDO>()
                .eq(MemberWaterFeeConfigDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
                .orderByDesc(MemberWaterFeeConfigDO::getId)
                .last("LIMIT 1"));
    }
}
