package cn.iocoder.yudao.module.member.service.water;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDevicePageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDeviceRespVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterDeviceDO;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterDeviceMapper;
import cn.iocoder.yudao.module.member.framework.water.core.MemberWaterMeterClient;
import cn.iocoder.yudao.module.member.framework.water.core.MemberWaterMeterClient.MemberWaterMeterExtendDictDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
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
            log.warn("[registerOrUpdateDevice][deviceNo({}) read info failed]", deviceNo, ex);
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
    public void refreshDevice(Long id) {
        MemberWaterDeviceDO device = deviceMapper.selectById(id);
        if (device == null) {
            throw exception(WATER_DEVICE_NOT_EXISTS);
        }
        registerOrUpdateDevice(device.getDeviceNo());
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
        return LocalDateTimeUtils.parse(time);
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
