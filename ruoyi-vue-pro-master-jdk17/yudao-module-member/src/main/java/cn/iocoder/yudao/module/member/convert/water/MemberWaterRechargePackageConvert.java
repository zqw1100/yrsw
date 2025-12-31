package cn.iocoder.yudao.module.member.convert.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterRechargePackageCreateReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterRechargePackageRespVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterRechargePackageUpdateReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterRechargePackageRespVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterRechargePackageDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MemberWaterRechargePackageConvert {

    MemberWaterRechargePackageConvert INSTANCE = Mappers.getMapper(MemberWaterRechargePackageConvert.class);

    MemberWaterRechargePackageDO convert(MemberWaterRechargePackageCreateReqVO bean);

    MemberWaterRechargePackageDO convert(MemberWaterRechargePackageUpdateReqVO bean);

    MemberWaterRechargePackageRespVO convert(MemberWaterRechargePackageDO bean);

    PageResult<MemberWaterRechargePackageRespVO> convertPage(PageResult<MemberWaterRechargePackageDO> page);

    List<AppWaterRechargePackageRespVO> convertAppList(List<MemberWaterRechargePackageDO> list);
}
