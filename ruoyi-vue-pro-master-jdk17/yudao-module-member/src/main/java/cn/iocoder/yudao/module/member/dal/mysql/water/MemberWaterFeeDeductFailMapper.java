package cn.iocoder.yudao.module.member.dal.mysql.water;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFeeDeductFailDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 水费扣费失败记录 Mapper
 */
@Mapper
public interface MemberWaterFeeDeductFailMapper extends BaseMapperX<MemberWaterFeeDeductFailDO> {
}
