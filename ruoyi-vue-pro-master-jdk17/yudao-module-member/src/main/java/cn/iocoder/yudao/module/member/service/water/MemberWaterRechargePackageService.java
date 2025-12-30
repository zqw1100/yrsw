package cn.iocoder.yudao.module.member.service.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterRechargePackageCreateReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterRechargePackagePageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterRechargePackageUpdateReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterRechargePackageDO;

import java.util.List;

/**
 * 居民报装充值套餐 Service
 */
public interface MemberWaterRechargePackageService {

    Long createRechargePackage(MemberWaterRechargePackageCreateReqVO createReqVO);

    void updateRechargePackage(MemberWaterRechargePackageUpdateReqVO updateReqVO);

    void deleteRechargePackage(Long id);

    MemberWaterRechargePackageDO getRechargePackage(Long id);

    PageResult<MemberWaterRechargePackageDO> getRechargePackagePage(MemberWaterRechargePackagePageReqVO pageReqVO);

    List<MemberWaterRechargePackageDO> getEnableRechargePackageList();
}
