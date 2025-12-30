package cn.iocoder.yudao.module.member.service.water;

import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterApplyCompleteReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterApplyCreateReqVO;
import cn.iocoder.yudao.module.member.convert.water.MemberWaterApplyConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterApplyDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterHouseDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterHouseOwnerDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterRechargePackageDO;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterApplyMapper;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterHouseMapper;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterHouseOwnerMapper;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterRechargePackageMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 居民报装申请 Service 实现类
 */
@Service
@Validated
public class MemberWaterApplyServiceImpl implements MemberWaterApplyService {

    @Resource
    private MemberWaterApplyMapper applyMapper;
    @Resource
    private MemberWaterHouseMapper waterHouseMapper;
    @Resource
    private MemberWaterHouseOwnerMapper ownerMapper;
    @Resource
    private MemberWaterRechargePackageMapper rechargePackageMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createApply(AppWaterApplyCreateReqVO createReqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        MemberWaterHouseDO waterHouse = waterHouseMapper.selectByUnique(createReqVO.getAreaId(),
                createReqVO.getCommunityName(), createReqVO.getBuildingName(),
                createReqVO.getUnitName(), createReqVO.getRoomNo());
        if (waterHouse == null) {
            throw exception(WATER_HOUSE_NOT_EXISTS);
        }
        if (Integer.valueOf(1).equals(waterHouse.getInstallStatus())) {
            throw exception(WATER_HOUSE_ALREADY_INSTALLED);
        }
        MemberWaterApplyDO existingApply = applyMapper.selectLatestByUserAndHouse(userId, waterHouse.getId(), 0);
        if (existingApply != null) {
            return existingApply.getId();
        }
        MemberWaterApplyDO apply = MemberWaterApplyConvert.INSTANCE.convert(createReqVO);
        apply.setUserId(userId);
        apply.setWaterHouseId(waterHouse.getId());
        apply.setApplyStatus(0);
        applyMapper.insert(apply);
        return apply.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeApply(AppWaterApplyCompleteReqVO completeReqVO) {
        MemberWaterApplyDO apply = applyMapper.selectById(completeReqVO.getId());
        if (apply == null) {
            throw exception(WATER_APPLY_NOT_EXISTS);
        }
        MemberWaterRechargePackageDO packageDO = rechargePackageMapper.selectById(completeReqVO.getRechargePackageId());
        if (packageDO == null) {
            throw exception(WATER_RECHARGE_PACKAGE_NOT_EXISTS);
        }
        if (!Integer.valueOf(1).equals(packageDO.getStatus())) {
            throw exception(WATER_RECHARGE_PACKAGE_NOT_EXISTS);
        }
        MemberWaterHouseOwnerDO owner = ownerMapper.selectByApplyId(completeReqVO.getId());
        MemberWaterHouseOwnerDO updateOwner = MemberWaterHouseOwnerDO.builder()
                .id(owner == null ? null : owner.getId())
                .applyId(apply.getId())
                .waterHouseId(apply.getWaterHouseId())
                .ownerName(completeReqVO.getOwnerName())
                .ownerIdCard(completeReqVO.getOwnerIdCard())
                .contractImageUrls(completeReqVO.getContractImageUrls())
                .remark(completeReqVO.getRemark())
                .build();
        if (owner == null) {
            ownerMapper.insert(updateOwner);
        } else {
            ownerMapper.updateById(updateOwner);
        }
        MemberWaterApplyDO updateApply = MemberWaterApplyDO.builder()
                .id(apply.getId())
                .rechargePackageId(completeReqVO.getRechargePackageId())
                .remark(completeReqVO.getRemark())
                .applyStatus(1)
                .build();
        applyMapper.updateById(updateApply);
        MemberWaterHouseDO updateHouse = MemberWaterHouseDO.builder()
                .id(apply.getWaterHouseId())
                .installStatus(1)
                .build();
        waterHouseMapper.updateById(updateHouse);
    }
}
