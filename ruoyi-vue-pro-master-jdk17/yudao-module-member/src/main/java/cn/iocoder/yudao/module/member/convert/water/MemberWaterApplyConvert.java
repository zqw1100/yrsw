package cn.iocoder.yudao.module.member.convert.water;

import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterApplyCreateReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterApplyDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MemberWaterApplyConvert {

    MemberWaterApplyConvert INSTANCE = Mappers.getMapper(MemberWaterApplyConvert.class);

    MemberWaterApplyDO convert(AppWaterApplyCreateReqVO bean);
}
