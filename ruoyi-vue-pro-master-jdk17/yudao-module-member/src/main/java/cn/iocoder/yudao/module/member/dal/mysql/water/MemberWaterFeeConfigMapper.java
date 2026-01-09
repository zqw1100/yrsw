package cn.iocoder.yudao.module.member.dal.mysql.water;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFeeConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 水费配置 Mapper
 */
@Mapper
public interface MemberWaterFeeConfigMapper extends BaseMapperX<MemberWaterFeeConfigDO> {

    default MemberWaterFeeConfigDO selectLatestEnabled() {
        return selectOne(new LambdaQueryWrapperX<MemberWaterFeeConfigDO>()
                .eq(MemberWaterFeeConfigDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
                .orderByDesc(MemberWaterFeeConfigDO::getId)
                .last("LIMIT 1"));
    }
}
