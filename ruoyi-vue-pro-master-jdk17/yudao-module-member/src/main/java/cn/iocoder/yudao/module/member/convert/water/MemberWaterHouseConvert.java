package cn.iocoder.yudao.module.member.convert.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterHouseCreateReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterHouseRespVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterHouseUpdateReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterHouseRoomRespVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterHouseDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MemberWaterHouseConvert {

    MemberWaterHouseConvert INSTANCE = Mappers.getMapper(MemberWaterHouseConvert.class);

    MemberWaterHouseDO convert(MemberWaterHouseCreateReqVO bean);

    MemberWaterHouseDO convert(MemberWaterHouseUpdateReqVO bean);

    MemberWaterHouseRespVO convert(MemberWaterHouseDO bean);

    List<MemberWaterHouseRespVO> convertList(List<MemberWaterHouseDO> list);

    PageResult<MemberWaterHouseRespVO> convertPage(PageResult<MemberWaterHouseDO> page);

    AppWaterHouseRoomRespVO convert(MemberWaterHouseDO bean);
}
