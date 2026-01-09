package cn.iocoder.yudao.module.member.convert.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFeeConfigCreateReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFeeConfigRespVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFeeConfigUpdateReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFeeConfigDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MemberWaterFeeConfigConvert {

    MemberWaterFeeConfigConvert INSTANCE = Mappers.getMapper(MemberWaterFeeConfigConvert.class);

    MemberWaterFeeConfigDO convert(MemberWaterFeeConfigCreateReqVO bean);

    MemberWaterFeeConfigDO convert(MemberWaterFeeConfigUpdateReqVO bean);

    MemberWaterFeeConfigRespVO convert(MemberWaterFeeConfigDO bean);

    PageResult<MemberWaterFeeConfigRespVO> convertPage(PageResult<MemberWaterFeeConfigDO> pageResult);
}
