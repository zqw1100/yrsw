package cn.iocoder.yudao.module.member.convert.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFeeBillRespVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFeeBillDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MemberWaterFeeBillConvert {

    MemberWaterFeeBillConvert INSTANCE = Mappers.getMapper(MemberWaterFeeBillConvert.class);

    MemberWaterFeeBillRespVO convert(MemberWaterFeeBillDO bean);

    PageResult<MemberWaterFeeBillRespVO> convertPage(PageResult<MemberWaterFeeBillDO> pageResult);
}
