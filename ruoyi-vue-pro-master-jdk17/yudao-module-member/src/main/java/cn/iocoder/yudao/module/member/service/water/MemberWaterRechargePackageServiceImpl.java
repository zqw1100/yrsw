package cn.iocoder.yudao.module.member.service.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterRechargePackageCreateReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterRechargePackagePageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterRechargePackageUpdateReqVO;
import cn.iocoder.yudao.module.member.convert.water.MemberWaterRechargePackageConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterRechargePackageDO;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterRechargePackageMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.WATER_RECHARGE_PACKAGE_NOT_EXISTS;

/**
 * 居民报装充值套餐 Service 实现类
 */
@Service
@Validated
public class MemberWaterRechargePackageServiceImpl implements MemberWaterRechargePackageService {

    @Resource
    private MemberWaterRechargePackageMapper rechargePackageMapper;

    @Override
    public Long createRechargePackage(MemberWaterRechargePackageCreateReqVO createReqVO) {
        MemberWaterRechargePackageDO rechargePackage = MemberWaterRechargePackageConvert.INSTANCE.convert(createReqVO);
        fillDefaultValues(rechargePackage);
        rechargePackageMapper.insert(rechargePackage);
        return rechargePackage.getId();
    }

    @Override
    public void updateRechargePackage(MemberWaterRechargePackageUpdateReqVO updateReqVO) {
        validateRechargePackageExists(updateReqVO.getId());
        MemberWaterRechargePackageDO updateObj = MemberWaterRechargePackageConvert.INSTANCE.convert(updateReqVO);
        fillDefaultValues(updateObj);
        rechargePackageMapper.updateById(updateObj);
    }

    @Override
    public void deleteRechargePackage(Long id) {
        validateRechargePackageExists(id);
        rechargePackageMapper.deleteById(id);
    }

    @Override
    public MemberWaterRechargePackageDO getRechargePackage(Long id) {
        return rechargePackageMapper.selectById(id);
    }

    @Override
    public PageResult<MemberWaterRechargePackageDO> getRechargePackagePage(MemberWaterRechargePackagePageReqVO pageReqVO) {
        return rechargePackageMapper.selectPage(pageReqVO);
    }

    @Override
    public List<MemberWaterRechargePackageDO> getEnableRechargePackageList() {
        return rechargePackageMapper.selectEnableList();
    }

    private void validateRechargePackageExists(Long id) {
        if (rechargePackageMapper.selectById(id) == null) {
            throw exception(WATER_RECHARGE_PACKAGE_NOT_EXISTS);
        }
    }

    private void fillDefaultValues(MemberWaterRechargePackageDO rechargePackage) {
        if (rechargePackage.getGiftWaterVolume() == null) {
            rechargePackage.setGiftWaterVolume(0);
        }
        if (rechargePackage.getDiscountAmount() == null) {
            rechargePackage.setDiscountAmount(java.math.BigDecimal.ZERO);
        }
        if (rechargePackage.getSort() == null) {
            rechargePackage.setSort(0);
        }
        if (rechargePackage.getRemark() == null) {
            rechargePackage.setRemark("");
        }
    }
}
