package cn.iocoder.yudao.module.member.service.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFaultPageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFaultRespVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFaultStatusUpdateReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterFaultCreateReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterFaultInitRespVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterFaultPageReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterFaultRespVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterApplyDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFaultDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterHouseOwnerDO;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterApplyMapper;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterFaultMapper;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterHouseOwnerMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.WATER_FAULT_INIT_NOT_EXISTS;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.WATER_FAULT_NOT_EXISTS;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.WATER_FAULT_STATUS_NOT_ALLOWED;

@Service
@Validated
public class MemberWaterFaultServiceImpl implements MemberWaterFaultService {

    private static final int PROCESS_STATUS_PENDING_CONFIRM = 3;
    private static final int PROCESS_STATUS_COMPLETE = 4;

    @Resource
    private MemberWaterFaultMapper faultMapper;
    @Resource
    private MemberWaterApplyMapper applyMapper;
    @Resource
    private MemberWaterHouseOwnerMapper ownerMapper;
    @Resource
    private MemberWaterWorkOrderService workOrderService;

    @Override
    public AppWaterFaultInitRespVO getFaultInit(Long userId, String deviceNo) {
        MemberWaterApplyDO apply = getLatestApply(userId, deviceNo);
        MemberWaterHouseOwnerDO owner = ownerMapper.selectByApplyId(apply.getId());
        AppWaterFaultInitRespVO respVO = new AppWaterFaultInitRespVO();
        respVO.setOwnerName(owner == null ? "" : owner.getOwnerName());
        respVO.setDeviceNo(apply.getDeviceNo());
        respVO.setAreaName(apply.getAreaName());
        respVO.setCommunityName(apply.getCommunityName());
        respVO.setBuildingName(apply.getBuildingName());
        respVO.setUnitName(apply.getUnitName());
        respVO.setRoomNo(apply.getRoomNo());
        respVO.setContactMobile(apply.getContactMobile());
        return respVO;
    }

    @Override
    public Long createFault(Long userId, AppWaterFaultCreateReqVO createReqVO) {
        MemberWaterApplyDO apply = getLatestApply(userId, createReqVO.getDeviceNo());
        MemberWaterHouseOwnerDO owner = ownerMapper.selectByApplyId(apply.getId());
        MemberWaterFaultDO fault = MemberWaterFaultDO.builder()
                .userId(userId)
                .waterHouseId(apply.getWaterHouseId())
                .applyId(apply.getId())
                .ownerName(owner == null ? "" : owner.getOwnerName())
                .deviceNo(createReqVO.getDeviceNo())
                .areaName(apply.getAreaName())
                .communityName(apply.getCommunityName())
                .buildingName(apply.getBuildingName())
                .unitName(apply.getUnitName())
                .roomNo(apply.getRoomNo())
                .contactMobile(createReqVO.getContactMobile())
                .faultCode(createReqVO.getFaultCode())
                .feedback(createReqVO.getFeedback())
                .priority(createReqVO.getPriority() == null ? 1 : createReqVO.getPriority())
                .imageUrls(createReqVO.getImageUrls())
                .processStatus(0)
                .remark("")
                .build();
        faultMapper.insert(fault);
        workOrderService.createForFault(fault);
        return fault.getId();
    }

    @Override
    public PageResult<AppWaterFaultRespVO> getFaultPage(Long userId, AppWaterFaultPageReqVO pageReqVO) {
        PageResult<MemberWaterFaultDO> pageResult = faultMapper.selectPageByUserId(userId, pageReqVO);
        return new PageResult<>(BeanUtils.toBean(pageResult.getList(), AppWaterFaultRespVO.class),
                pageResult.getTotal());
    }

    @Override
    public PageResult<MemberWaterFaultRespVO> getFaultPage(MemberWaterFaultPageReqVO pageReqVO) {
        PageResult<MemberWaterFaultDO> pageResult = faultMapper.selectPage(pageReqVO);
        return new PageResult<>(BeanUtils.toBean(pageResult.getList(), MemberWaterFaultRespVO.class),
                pageResult.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFaultStatus(MemberWaterFaultStatusUpdateReqVO updateReqVO) {
        MemberWaterFaultDO fault = faultMapper.selectById(updateReqVO.getId());
        if (fault == null) {
            throw exception(WATER_FAULT_NOT_EXISTS);
        }
        if (updateReqVO.getProcessStatus() != null
                && updateReqVO.getProcessStatus() != PROCESS_STATUS_PENDING_CONFIRM
                && updateReqVO.getProcessStatus() != PROCESS_STATUS_COMPLETE) {
            throw exception(WATER_FAULT_STATUS_NOT_ALLOWED);
        }
        MemberWaterFaultDO updateObj = MemberWaterFaultDO.builder()
                .id(updateReqVO.getId())
                .processStatus(updateReqVO.getProcessStatus())
                .remark(updateReqVO.getRemark())
                .build();
        faultMapper.updateById(updateObj);
        workOrderService.updateForFault(fault.getId(), updateReqVO.getProcessStatus());
    }

    private MemberWaterApplyDO getLatestApply(Long userId, String deviceNo) {
        MemberWaterApplyDO apply = applyMapper.selectLatestByUserAndDeviceNo(userId, deviceNo);
        if (apply == null) {
            throw exception(WATER_FAULT_INIT_NOT_EXISTS);
        }
        return apply;
    }
}
