package cn.iocoder.yudao.module.member.controller.open.water;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.member.controller.open.water.vo.MemberWaterDeviceDataPushReqVO;
import cn.iocoder.yudao.module.member.framework.water.config.MemberWaterMeterProperties;
import cn.iocoder.yudao.module.member.service.water.MemberWaterDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception0;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "开放接口 - 水表设备数据推送")
@RestController
@RequestMapping("/open-api/water")
@Validated
@Slf4j
public class MemberWaterDeviceDataController {

    @Resource
    private MemberWaterDeviceService deviceService;
    @Resource
    private MemberWaterMeterProperties waterMeterProperties;

    @PostMapping("/{vendorCode}/devicedata")
    @PermitAll
    @Operation(summary = "水表设备抄收数据推送")
    public CommonResult<Boolean> pushDeviceData(@PathVariable("vendorCode") String vendorCode,
                                                @RequestHeader(value = "X-Api-Key", required = false) String apiKey,
                                                @Validated @RequestBody MemberWaterDeviceDataPushReqVO reqVO) {
        validateAuth(vendorCode, apiKey);
        deviceService.syncDeviceData(reqVO);
        return success(Boolean.TRUE);
    }

    private void validateAuth(String vendorCode, String apiKey) {
        if (!StrUtil.equals(vendorCode, waterMeterProperties.getPushVendorCode())
                || !StrUtil.equals(apiKey, waterMeterProperties.getPushApiKey())) {
            log.warn("[pushDeviceData][vendorCode({}) apiKey({}) auth failed]", vendorCode, apiKey);
            throw exception0(GlobalErrorCodeConstants.UNAUTHORIZED.getCode(), "鉴权失败");
        }
    }
}
