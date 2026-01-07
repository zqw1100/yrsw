package cn.iocoder.yudao.module.member.service.water;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterApplyPageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterApplyRespVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterApplyStatusUpdateReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterApplyCompleteReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterApplyCreateReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterApplyPageReqVO;
import cn.iocoder.yudao.module.member.convert.water.MemberWaterApplyConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterApplyDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterHouseDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterHouseOwnerDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterWorkOrderDO;
import cn.iocoder.yudao.module.pay.dal.dataobject.wallet.PayWalletRechargePackageDO;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterApplyMapper;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterHouseMapper;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterHouseOwnerMapper;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterWorkOrderMapper;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletRechargePackageService;
import cn.iocoder.yudao.module.pay.service.wallet.PayWalletService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 居民报装申请 Service 实现类
 */
@Service
@Validated
public class MemberWaterApplyServiceImpl implements MemberWaterApplyService {

    private static final String TEMP_DEVICE_NO_PREFIX = "TMP-APPLY-";

    @Resource
    private MemberWaterApplyMapper applyMapper;
    @Resource
    private MemberWaterHouseMapper waterHouseMapper;
    @Resource
    private MemberWaterHouseOwnerMapper ownerMapper;
    @Resource
    private PayWalletRechargePackageService walletRechargePackageService;
    @Resource
    private PayWalletService payWalletService;
    @Resource
    private MemberWaterWorkOrderService workOrderService;
    @Resource
    private MemberWaterWorkOrderMapper workOrderMapper;

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
        apply.setProcessStatus(0);
        applyMapper.insert(apply);
        return apply.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeApply(AppWaterApplyCompleteReqVO completeReqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        MemberWaterApplyDO apply = applyMapper.selectById(completeReqVO.getId());
        if (apply == null) {
            throw exception(WATER_APPLY_NOT_EXISTS);
        }
        if (!userId.equals(apply.getUserId())) {
            throw exception(WATER_APPLY_NOT_ALLOW);
        }
        MemberWaterHouseOwnerDO owner = ownerMapper.selectByApplyId(completeReqVO.getId());
        MemberWaterHouseOwnerDO updateOwner = MemberWaterHouseOwnerDO.builder()
                .id(owner == null ? null : owner.getId())
                .applyId(apply.getId())
                .waterHouseId(apply.getWaterHouseId())
                .userId(userId)
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
        workOrderService.createForApply(apply);
    }

    @Override
    public PageResult<MemberWaterApplyRespVO> getApplyPage(MemberWaterApplyPageReqVO pageReqVO) {
        PageResult<MemberWaterApplyDO> pageResult = applyMapper.selectPage(pageReqVO);
        return buildApplyPage(pageResult);
    }

    @Override
    public PageResult<MemberWaterApplyRespVO> getApplyPage(Long userId, AppWaterApplyPageReqVO pageReqVO) {
        PageResult<MemberWaterApplyDO> pageResult = applyMapper.selectPageByUser(userId, pageReqVO);
        return buildApplyPage(pageResult);
    }

    @Override
    public void updateApplyStatus(MemberWaterApplyStatusUpdateReqVO updateReqVO) {
        MemberWaterApplyDO apply = applyMapper.selectById(updateReqVO.getId());
        if (apply == null) {
            throw exception(WATER_APPLY_NOT_EXISTS);
        }
        if (updateReqVO.getProcessStatus() != null && updateReqVO.getProcessStatus() == 3
                && StrUtil.isBlank(updateReqVO.getDeviceNo())) {
            throw exception(WATER_APPLY_DEVICE_NO_REQUIRED);
        }
        if (StrUtil.isBlank(apply.getDeviceNo()) && StrUtil.isNotBlank(updateReqVO.getDeviceNo())) {
            String tempDeviceNo = TEMP_DEVICE_NO_PREFIX + apply.getId();
            payWalletService.updateWalletDeviceNo(apply.getUserId(), UserTypeEnum.MEMBER.getValue(),
                    tempDeviceNo, updateReqVO.getDeviceNo());
        }
        MemberWaterApplyDO updateObj = MemberWaterApplyDO.builder()
                .id(updateReqVO.getId())
                .processStatus(updateReqVO.getProcessStatus())
                .deviceNo(updateReqVO.getDeviceNo())
                .build();
        applyMapper.updateById(updateObj);
    }

    private PageResult<MemberWaterApplyRespVO> buildApplyPage(PageResult<MemberWaterApplyDO> pageResult) {
        if (pageResult.getTotal() == 0) {
            return new PageResult<>(Collections.emptyList(), 0L);
        }
        List<Long> applyIds = pageResult.getList().stream()
                .map(MemberWaterApplyDO::getId)
                .collect(Collectors.toList());
        Map<Long, MemberWaterHouseOwnerDO> ownerMap = ownerMapper.selectListByApplyIds(applyIds).stream()
                .collect(Collectors.toMap(MemberWaterHouseOwnerDO::getApplyId, Function.identity(), (first, second) -> first));
        Map<Long, MemberWaterWorkOrderDO> workOrderMap = workOrderMapper.selectListByBizIds(0, applyIds).stream()
                .collect(Collectors.toMap(MemberWaterWorkOrderDO::getBizId, Function.identity(), (first, second) -> first));
        List<MemberWaterApplyRespVO> list = pageResult.getList().stream()
                .map(apply -> {
                    MemberWaterApplyRespVO respVO = BeanUtils.toBean(apply, MemberWaterApplyRespVO.class);
                    MemberWaterHouseOwnerDO owner = ownerMap.get(apply.getId());
                    if (owner != null) {
                        respVO.setOwnerName(owner.getOwnerName());
                        respVO.setOwnerIdCard(owner.getOwnerIdCard());
                        respVO.setContractImageUrls(owner.getContractImageUrls());
                    }
                    MemberWaterWorkOrderDO workOrder = workOrderMap.get(apply.getId());
                    if (workOrder != null) {
                        respVO.setBeforeImageUrls(workOrder.getBeforeImageUrls());
                        respVO.setBeforeRemark(workOrder.getBeforeRemark());
                        respVO.setAfterImageUrls(workOrder.getAfterImageUrls());
                        respVO.setAfterRemark(workOrder.getAfterRemark());
                    }
                    return respVO;
                }).collect(Collectors.toList());
        return new PageResult<>(list, pageResult.getTotal());
    }
}
