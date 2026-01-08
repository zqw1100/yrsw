package cn.iocoder.yudao.module.member.service.water;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.exception.ServerException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDevicePageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDeviceRespVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterApplyDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterDeviceDO;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterApplyMapper;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterDeviceMapper;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterFaultMapper;
import cn.iocoder.yudao.module.member.framework.water.core.MemberWaterMeterClient;
import cn.iocoder.yudao.module.member.framework.water.core.MemberWaterMeterClient.MemberWaterMeterExtendDictDTO;
import cn.iocoder.yudao.module.member.framework.water.core.MemberWaterMeterClient.MemberWaterMeterAddDeviceReqDTO;
import cn.iocoder.yudao.module.member.framework.water.core.MemberWaterMeterClient.MemberWaterMeterChangeDeviceReqDTO;
import cn.iocoder.yudao.module.member.framework.water.core.MemberWaterMeterClient.MemberWaterMeterUploadModeReqDTO;
import cn.iocoder.yudao.module.member.framework.water.config.MemberWaterMeterProperties;
import cn.iocoder.yudao.module.pay.dal.mysql.wallet.PayWalletMapper;
import cn.iocoder.yudao.module.pay.dal.mysql.wallet.PayWalletRechargeMapper;
import cn.iocoder.yudao.module.pay.dal.mysql.wallet.PayWalletTransactionMapper;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.WATER_APPLY_NOT_ALLOW;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.WATER_DEVICE_NOT_EXISTS;

/**
 * 居民设备信息 Service 实现类
 */
@Service
@Validated
@Slf4j
public class MemberWaterDeviceServiceImpl implements MemberWaterDeviceService {

    @Resource
    private MemberWaterDeviceMapper deviceMapper;
    @Resource
    private MemberWaterMeterClient meterClient;
    @Resource
    private MemberWaterMeterProperties meterProperties;
    @Resource
    private MemberWaterApplyMapper applyMapper;
    @Resource
    private MemberWaterFaultMapper faultMapper;
    @Resource
    private PayWalletMapper payWalletMapper;
    @Resource
    private PayWalletRechargeMapper payWalletRechargeMapper;
    @Resource
    private PayWalletTransactionMapper payWalletTransactionMapper;

    @Override
    public PageResult<MemberWaterDeviceRespVO> getDevicePage(MemberWaterDevicePageReqVO pageReqVO) {
        PageResult<MemberWaterDeviceDO> pageResult = deviceMapper.selectPage(pageReqVO);
        return BeanUtils.toBean(pageResult, MemberWaterDeviceRespVO.class);
    }

    @Override
    public void registerOrUpdateDevice(String deviceNo) {
        if (StrUtil.isBlank(deviceNo)) {
            return;
        }
        MemberWaterDeviceDO device = deviceMapper.selectByDeviceNo(deviceNo);
        MemberWaterMeterExtendDictDTO info = null;
        try {
            info = meterClient.readDeviceInfo(deviceNo);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
           // log.warn("[registerOrUpdateDevice][deviceNo({}) read info failed]", deviceNo, ex);
        }
        LocalDateTime now = LocalDateTime.now();
        MemberWaterDeviceDO updateObj = buildDevice(deviceNo, info, now);
        if (device == null) {
            deviceMapper.insert(updateObj);
        } else {
            updateObj.setId(device.getId());
            deviceMapper.updateById(updateObj);
        }
    }

    @Override
    public void registerDeviceForApply(MemberWaterApplyDO apply, String deviceNo) {
        if (apply == null || StrUtil.isBlank(deviceNo)) {
            return;
        }
        MemberWaterMeterAddDeviceReqDTO reqDTO = new MemberWaterMeterAddDeviceReqDTO();
        reqDTO.setAddress(buildDeviceAddress(apply));
        reqDTO.setDeviceCode(deviceNo);
        reqDTO.setDeviceName(buildDeviceName(apply, deviceNo));
        reqDTO.setUserName(StrUtil.nullToEmpty(apply.getContactName()));
        reqDTO.setDescription(StrUtil.blankToDefault(apply.getRemark(), "居民报装"));
        reqDTO.setDeviceVersionName(meterProperties.getDeviceVersionName());
        meterClient.addDevice(reqDTO);
        registerOrUpdateDevice(deviceNo);
    }

    @Override
    public void refreshDevice(Long id) {
        MemberWaterDeviceDO device = deviceMapper.selectById(id);
        if (device == null) {
            throw exception(WATER_DEVICE_NOT_EXISTS);
        }
        registerOrUpdateDevice(device.getDeviceNo());
    }

    @Override
    public boolean operateValve(String deviceNo, Integer valveStatus) {
        return meterClient.operateValve(deviceNo, valveStatus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changeDevice(String originalDeviceCode, String newDeviceCode, Long originalTotalData) {
        MemberWaterMeterChangeDeviceReqDTO reqDTO = new MemberWaterMeterChangeDeviceReqDTO();
        reqDTO.setOriginalDeviceCode(originalDeviceCode);
        reqDTO.setNewDeviceCode(newDeviceCode);
        reqDTO.setOriginalTotalData(originalTotalData);
        boolean success = meterClient.changeDevice(reqDTO);
        if (success) {
            updateDeviceNoData(originalDeviceCode, newDeviceCode);
        }
        return success;
    }

    @Override
    public boolean setUploadMode(String deviceCode, Integer uploadType, Integer value) {
        MemberWaterMeterUploadModeReqDTO reqDTO = new MemberWaterMeterUploadModeReqDTO();
        reqDTO.setDeviceCode(deviceCode);
        reqDTO.setUploadType(uploadType);
        reqDTO.setValue(value);
        return meterClient.setUploadMode(reqDTO);
    }

    private void updateDeviceNoData(String originalDeviceCode, String newDeviceCode) {
        LocalDateTime now = LocalDateTime.now();
        int deviceUpdated = deviceMapper.updateDeviceNo(originalDeviceCode, newDeviceCode, now);
        if (deviceUpdated == 0) {
            log.warn("[updateDeviceNoData][originalDeviceCode({}) not found in device table]", originalDeviceCode);
        }
        int i = applyMapper.updateDeviceNo(originalDeviceCode, newDeviceCode);
        int i1 = faultMapper.updateDeviceNo(originalDeviceCode, newDeviceCode);
        int i2 = payWalletMapper.updateDeviceNo(originalDeviceCode, newDeviceCode);
        int i3 = payWalletRechargeMapper.updateDeviceNo(originalDeviceCode, newDeviceCode);
    }

    private String buildDeviceAddress(MemberWaterApplyDO apply) {
        List<String> parts = new ArrayList<>();
        if (StrUtil.isNotBlank(apply.getAreaName())) {
            parts.add(apply.getAreaName());
        }
        if (StrUtil.isNotBlank(apply.getCommunityName())) {
            parts.add(apply.getCommunityName());
        }
        if (StrUtil.isNotBlank(apply.getBuildingName())) {
            parts.add(apply.getBuildingName());
        }
        if (StrUtil.isNotBlank(apply.getUnitName())) {
            parts.add(apply.getUnitName());
        }
        if (StrUtil.isNotBlank(apply.getRoomNo())) {
            parts.add(apply.getRoomNo());
        }
        return String.join("", parts);
    }

    private String buildDeviceName(MemberWaterApplyDO apply, String deviceNo) {
        String address = buildDeviceAddress(apply);
        if (StrUtil.isNotBlank(address)) {
            return address;
        }
        return deviceNo;
    }

    private MemberWaterDeviceDO buildDevice(String deviceNo, MemberWaterMeterExtendDictDTO info, LocalDateTime now) {
        MemberWaterDeviceDO.MemberWaterDeviceDOBuilder builder = MemberWaterDeviceDO.builder()
                .deviceNo(deviceNo)
                .lastSyncTime(now);
        if (info == null) {
            log.warn("[buildDevice][deviceNo({}) info is null]", deviceNo);
            return builder
                    .deviceAddress("")
                    .deviceUserName("")
                    .build();
        }
        return builder
                .deviceAddress(StrUtil.nullToEmpty(info.getDeviceAddress()))
                .deviceUserName(StrUtil.nullToEmpty(info.getDeviceUserName()))
                .deviceClock(parseTime(info.getDeviceClock()))
                .deviceUpdateTime(parseTime(info.getUpdateTime()))
                .deviceRssi(info.getDeviceRSSI())
                .deviceBuyTimes(info.getDeviceBuyTimes())
                .deviceVoltage(parseVoltage(info.getDeviceVoltage()))
                .deviceBalance(info.getDeviceBalance())
                .deviceTotalData(info.getDeviceTotalData())
                .deviceSettleDayData(info.getDeviceSettleDayData())
                .deviceLastData(info.getDeviceLastData())
                .deviceSettleDay(info.getDeviceSettleDay())
                .deviceCurrentData(info.getDeviceCurrentData())
                .valveStatus(info.getValveStatus())
                .voltageStatus(info.getVoltageStatus())
                .feeStatus(info.getFeeStatus())
                .build();
    }

    private LocalDateTime parseTime(String time) {
        if (StrUtil.isBlank(time)) {
            return null;
        }
        try {
            return LocalDateTimeUtils.parse(time);
        } catch (Exception ex) {
            try {
                return LocalDateTimeUtil.parse(time, DatePattern.NORM_DATETIME_PATTERN);
            } catch (Exception fallbackEx) {
                log.warn("[parseTime][time({}) parse failed]", time, fallbackEx);
                return null;
            }
        }
    }

    private BigDecimal parseVoltage(String voltage) {
        if (StrUtil.isBlank(voltage)) {
            return null;
        }
        try {
            return new BigDecimal(voltage);
        } catch (NumberFormatException ex) {
            log.warn("[parseVoltage][voltage({}) parse failed]", voltage, ex);
            return null;
        }
    }
}
