package cn.iocoder.yudao.module.member.service.water;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterWorkOrderAcceptReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterWorkOrderAssignReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterWorkOrderFinishReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterWorkOrderPageReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterWorkOrderRespVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterWorkOrderRevokeReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterWorkOrderStartReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterApplyDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFaultDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterWorkOrderDO;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterApplyMapper;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterFaultMapper;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterWorkOrderMapper;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.WATER_APPLY_DEVICE_NO_REQUIRED;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.WATER_WORK_ORDER_NOT_EXISTS;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.WATER_WORK_ORDER_NOT_ALLOWED;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.WATER_WORK_ORDER_STATUS_NOT_ALLOWED;

@Service
@Validated
public class MemberWaterWorkOrderServiceImpl implements MemberWaterWorkOrderService {

    private static final Long WORKER_GROUP_ID = 1L;
    private static final Long MANAGER_GROUP_ID = 2L;

    @Resource
    private MemberWaterWorkOrderMapper workOrderMapper;
    @Resource
    private MemberWaterApplyMapper applyMapper;
    @Resource
    private MemberWaterFaultMapper faultMapper;
    @Resource
    private MemberUserService memberUserService;
    @Resource
    private MemberWaterDeviceService deviceService;

    @Override
    public PageResult<AppWaterWorkOrderRespVO> getWorkOrderPage(Long userId, AppWaterWorkOrderPageReqVO pageReqVO) {
        boolean isManager = isManager(userId);
        PageResult<MemberWaterWorkOrderDO> pageResult = workOrderMapper.selectPage(pageReqVO, userId, isManager);
        if (pageResult.getTotal() == 0) {
            return new PageResult<>(Collections.emptyList(), 0L);
        }
        List<MemberWaterWorkOrderDO> orders = pageResult.getList();
        Map<Long, MemberWaterApplyDO> applyMap = getApplyMap(orders);
        Map<Long, MemberWaterFaultDO> faultMap = getFaultMap(orders);
        Map<Long, MemberUserDO> workerMap = getWorkerMap(orders);
        List<AppWaterWorkOrderRespVO> respList = orders.stream()
                .map(order -> buildResp(order, applyMap, faultMap, workerMap))
                .collect(Collectors.toList());
        return new PageResult<>(respList, pageResult.getTotal());
    }

    @Override
    public AppWaterWorkOrderRespVO getWorkOrder(Long userId, Long id) {
        MemberWaterWorkOrderDO order = workOrderMapper.selectById(id);
        validateOrderExists(order);
        if (!isManager(userId) && !userId.equals(order.getWorkerId())) {
            throw exception(WATER_WORK_ORDER_NOT_ALLOWED);
        }
        Map<Long, MemberWaterApplyDO> applyMap = getApplyMap(Collections.singletonList(order));
        Map<Long, MemberWaterFaultDO> faultMap = getFaultMap(Collections.singletonList(order));
        Map<Long, MemberUserDO> workerMap = getWorkerMap(Collections.singletonList(order));
        return buildResp(order, applyMap, faultMap, workerMap);
    }

    @Override
    public void assignWorkOrder(Long userId, AppWaterWorkOrderAssignReqVO reqVO) {
        if (!isManager(userId)) {
            throw exception(WATER_WORK_ORDER_NOT_ALLOWED);
        }
        MemberWaterWorkOrderDO order = workOrderMapper.selectById(reqVO.getId());
        validateOrderExists(order);
        if (order.getStatus() != null && order.getStatus() != 0) {
            throw exception(WATER_WORK_ORDER_STATUS_NOT_ALLOWED);
        }
        LocalDateTime now = LocalDateTime.now();
        MemberWaterWorkOrderDO updateObj = MemberWaterWorkOrderDO.builder()
                .id(order.getId())
                .workerId(reqVO.getWorkerId())
                .assignTime(now)
                .status(0)
                .build();
        workOrderMapper.updateById(updateObj);
        updateBizStatus(order, 0);
    }

    @Override
    public void revokeWorkOrder(Long userId, AppWaterWorkOrderRevokeReqVO reqVO) {
        if (!isManager(userId)) {
            throw exception(WATER_WORK_ORDER_NOT_ALLOWED);
        }
        MemberWaterWorkOrderDO order = workOrderMapper.selectById(reqVO.getId());
        validateOrderExists(order);
        if (order.getStatus() != null && order.getStatus() != 0) {
            throw exception(WATER_WORK_ORDER_STATUS_NOT_ALLOWED);
        }
        workOrderMapper.update(
                null,
                new LambdaUpdateWrapper<MemberWaterWorkOrderDO>()
                        .eq(MemberWaterWorkOrderDO::getId, order.getId())
                        .set(MemberWaterWorkOrderDO::getWorkerId, null)
                        .set(MemberWaterWorkOrderDO::getAssignTime, null)
                        .set(MemberWaterWorkOrderDO::getStatus, 0)
        );
        updateBizStatus(order, 0);
    }

    @Override
    public void acceptWorkOrder(Long userId, AppWaterWorkOrderAcceptReqVO reqVO) {
        MemberWaterWorkOrderDO order = workOrderMapper.selectById(reqVO.getId());
        validateOrderExists(order);
        validateWorker(userId, order);
        if (order.getStatus() != null && order.getStatus() != 0) {
            throw exception(WATER_WORK_ORDER_STATUS_NOT_ALLOWED);
        }
        MemberWaterWorkOrderDO updateObj = MemberWaterWorkOrderDO.builder()
                .id(order.getId())
                .status(1)
                .acceptTime(LocalDateTime.now())
                .build();
        workOrderMapper.updateById(updateObj);
        updateBizStatus(order, 1);
    }

    @Override
    public void startWorkOrder(Long userId, AppWaterWorkOrderStartReqVO reqVO) {
        MemberWaterWorkOrderDO order = workOrderMapper.selectById(reqVO.getId());
        validateOrderExists(order);
        validateWorker(userId, order);
        if (order.getStatus() != null && order.getStatus() != 1) {
            throw exception(WATER_WORK_ORDER_STATUS_NOT_ALLOWED);
        }
        MemberWaterWorkOrderDO updateObj = MemberWaterWorkOrderDO.builder()
                .id(order.getId())
                .status(2)
                .startTime(LocalDateTime.now())
                .beforeImageUrls(reqVO.getBeforeImageUrls())
                .beforeRemark(reqVO.getBeforeRemark())
                .build();
        workOrderMapper.updateById(updateObj);
        updateBizStatus(order, 2);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finishWorkOrder(Long userId, AppWaterWorkOrderFinishReqVO reqVO) {
        MemberWaterWorkOrderDO order = workOrderMapper.selectById(reqVO.getId());
        validateOrderExists(order);
        validateWorker(userId, order);
        if (order.getStatus() != null && order.getStatus() != 2) {
            throw exception(WATER_WORK_ORDER_STATUS_NOT_ALLOWED);
        }
        if (order.getOrderType() != null && order.getOrderType() == 0 && StrUtil.isBlank(reqVO.getDeviceNo())) {
            throw exception(WATER_APPLY_DEVICE_NO_REQUIRED);
        }
        MemberWaterWorkOrderDO updateObj = MemberWaterWorkOrderDO.builder()
                .id(order.getId())
                .status(3)
                .finishTime(LocalDateTime.now())
                .afterImageUrls(reqVO.getAfterImageUrls())
                .afterRemark(reqVO.getAfterRemark())
                .build();
        workOrderMapper.updateById(updateObj);
        if (order.getOrderType() != null && order.getOrderType() == 0) {
            MemberWaterApplyDO apply = applyMapper.selectById(order.getBizId());
            MemberWaterApplyDO applyUpdate = MemberWaterApplyDO.builder()
                    .id(order.getBizId())
                    .deviceNo(reqVO.getDeviceNo())
                    .build();
            applyMapper.updateById(applyUpdate);
            if (apply != null && StrUtil.isBlank(apply.getDeviceNo())) {
                apply.setDeviceNo(reqVO.getDeviceNo());
                deviceService.registerDeviceForApply(apply, reqVO.getDeviceNo());
            } else {
                deviceService.registerOrUpdateDevice(reqVO.getDeviceNo());
            }
        }
        updateBizStatus(order, 3);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createForApply(MemberWaterApplyDO apply) {
        MemberWaterWorkOrderDO existing = workOrderMapper.selectByBiz(0, apply.getId());
        if (existing != null) {
            return;
        }
        MemberWaterWorkOrderDO order = MemberWaterWorkOrderDO.builder()
                .orderType(0)
                .bizId(apply.getId())
                .status(0)
                .build();
        workOrderMapper.insert(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createForFault(MemberWaterFaultDO fault) {
        MemberWaterWorkOrderDO existing = workOrderMapper.selectByBiz(1, fault.getId());
        if (existing != null) {
            return;
        }
        MemberWaterWorkOrderDO order = MemberWaterWorkOrderDO.builder()
                .orderType(1)
                .bizId(fault.getId())
                .status(0)
                .build();
        workOrderMapper.insert(order);
    }

    private boolean isManager(Long userId) {
        MemberUserDO user = memberUserService.getUser(userId);
        return user != null && MANAGER_GROUP_ID.equals(user.getGroupId());
    }

    private void validateWorker(Long userId, MemberWaterWorkOrderDO order) {
        MemberUserDO user = memberUserService.getUser(userId);
        if (user == null || !WORKER_GROUP_ID.equals(user.getGroupId()) || !userId.equals(order.getWorkerId())) {
            throw exception(WATER_WORK_ORDER_NOT_ALLOWED);
        }
    }

    private void validateOrderExists(MemberWaterWorkOrderDO order) {
        if (order == null) {
            throw exception(WATER_WORK_ORDER_NOT_EXISTS);
        }
    }

    private void updateBizStatus(MemberWaterWorkOrderDO order, Integer status) {
        if (order.getOrderType() == null) {
            return;
        }
        if (order.getOrderType() == 0) {
            MemberWaterApplyDO updateObj = MemberWaterApplyDO.builder()
                    .id(order.getBizId())
                    .processStatus(status)
                    .build();
            applyMapper.updateById(updateObj);
        } else if (order.getOrderType() == 1) {
            Integer processStatus = mapFaultStatus(status);
            MemberWaterFaultDO updateObj = MemberWaterFaultDO.builder()
                    .id(order.getBizId())
                    .processStatus(processStatus)
                    .build();
            faultMapper.updateById(updateObj);
        }
    }

    private Integer mapFaultStatus(Integer status) {
        if (status == null) {
            return null;
        }
        if (status <= 1) {
            return 0;
        }
        if (status == 2) {
            return 1;
        }
        return 2;
    }

    private Map<Long, MemberWaterApplyDO> getApplyMap(List<MemberWaterWorkOrderDO> orders) {
        Set<Long> applyIds = orders.stream()
                .filter(order -> order.getOrderType() != null && order.getOrderType() == 0)
                .map(MemberWaterWorkOrderDO::getBizId)
                .collect(Collectors.toSet());
        if (CollUtil.isEmpty(applyIds)) {
            return Collections.emptyMap();
        }
        return applyMapper.selectBatchIds(applyIds).stream()
                .collect(Collectors.toMap(MemberWaterApplyDO::getId, Function.identity()));
    }

    private Map<Long, MemberWaterFaultDO> getFaultMap(List<MemberWaterWorkOrderDO> orders) {
        Set<Long> faultIds = orders.stream()
                .filter(order -> order.getOrderType() != null && order.getOrderType() == 1)
                .map(MemberWaterWorkOrderDO::getBizId)
                .collect(Collectors.toSet());
        if (CollUtil.isEmpty(faultIds)) {
            return Collections.emptyMap();
        }
        return faultMapper.selectBatchIds(faultIds).stream()
                .collect(Collectors.toMap(MemberWaterFaultDO::getId, Function.identity()));
    }

    private Map<Long, MemberUserDO> getWorkerMap(List<MemberWaterWorkOrderDO> orders) {
        Set<Long> workerIds = orders.stream()
                .map(MemberWaterWorkOrderDO::getWorkerId)
                .filter(id -> id != null)
                .collect(Collectors.toSet());
        if (CollUtil.isEmpty(workerIds)) {
            return Collections.emptyMap();
        }
        return memberUserService.getUserList(workerIds).stream()
                .collect(Collectors.toMap(MemberUserDO::getId, Function.identity()));
    }

    private AppWaterWorkOrderRespVO buildResp(MemberWaterWorkOrderDO order,
                                              Map<Long, MemberWaterApplyDO> applyMap,
                                              Map<Long, MemberWaterFaultDO> faultMap,
                                              Map<Long, MemberUserDO> workerMap) {
        AppWaterWorkOrderRespVO respVO = BeanUtils.toBean(order, AppWaterWorkOrderRespVO.class);
        MemberUserDO worker = workerMap.get(order.getWorkerId());
        if (worker != null) {
            respVO.setWorkerName(worker.getNickname());
        }
        if (order.getOrderType() != null && order.getOrderType() == 0) {
            MemberWaterApplyDO apply = applyMap.get(order.getBizId());
            if (apply != null) {
                respVO.setContactName(apply.getContactName());
                respVO.setContactMobile(apply.getContactMobile());
                respVO.setAddress(buildAddress(apply.getAreaName(), apply.getCommunityName(),
                        apply.getBuildingName(), apply.getUnitName(), apply.getRoomNo()));
                respVO.setDeviceNo(apply.getDeviceNo());
            }
        } else if (order.getOrderType() != null && order.getOrderType() == 1) {
            MemberWaterFaultDO fault = faultMap.get(order.getBizId());
            if (fault != null) {
                respVO.setContactName(fault.getOwnerName());
                respVO.setContactMobile(fault.getContactMobile());
                respVO.setAddress(buildAddress(fault.getAreaName(), fault.getCommunityName(),
                        fault.getBuildingName(), fault.getUnitName(), fault.getRoomNo()));
                respVO.setFeedback(fault.getFeedback());
            }
        }
        return respVO;
    }

    private String buildAddress(String areaName, String communityName, String buildingName,
                                String unitName, String roomNo) {
        return String.join(" ", CollUtil.newArrayList(areaName, communityName, buildingName, unitName, roomNo).stream()
                .filter(item -> item != null && !item.isEmpty())
                .collect(Collectors.toList()));
    }
}
