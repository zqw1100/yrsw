package cn.iocoder.yudao.module.member.framework.water.core;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.util.http.HttpUtils;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.module.member.framework.water.config.MemberWaterMeterProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 物联网水表接口客户端
 */
@Component
@Slf4j
public class MemberWaterMeterClient {

    @Resource
    private MemberWaterMeterProperties properties;

    private volatile String accessToken;
    private volatile LocalDateTime tokenExpireTime;

    public MemberWaterMeterExtendDictDTO readDeviceInfo(String deviceCode) {
        String token = getAccessToken();
        if (StrUtil.isBlank(token)) {
            return null;
        }
        String url = properties.getBaseUrl() + properties.getReadInfoPath() + "/" + properties.getOpenId() + "/" + deviceCode;
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        String responseBody = HttpUtils.get(url, headers);
        MemberWaterMeterReadInfoRespDTO respDTO = parseSafely(responseBody, MemberWaterMeterReadInfoRespDTO.class);
        if (respDTO == null || respDTO.getCode() == null || respDTO.getCode() != 0 || respDTO.getData() == null) {
            log.warn("[readDeviceInfo][deviceCode({}) response({})]", deviceCode, responseBody);
            return null;
        }
        return respDTO.getData().getExtendDict();
    }

    private String getAccessToken() {
        if (StrUtil.isNotBlank(accessToken) && tokenExpireTime != null
                && tokenExpireTime.isAfter(LocalDateTime.now().plusSeconds(30))) {
            return accessToken;
        }
        return refreshAccessToken();
    }

    private synchronized String refreshAccessToken() {
        if (StrUtil.isNotBlank(accessToken) && tokenExpireTime != null
                && tokenExpireTime.isAfter(LocalDateTime.now().plusSeconds(30))) {
            return accessToken;
        }
        String url = properties.getBaseUrl() + properties.getTokenPath();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Authorization", properties.getTokenAuthorization());
        String responseBody = HttpUtils.post(url, headers, "");
        MemberWaterMeterTokenRespDTO respDTO = parseSafely(responseBody, MemberWaterMeterTokenRespDTO.class);
        if (respDTO == null || StrUtil.isBlank(respDTO.getAccessToken())) {
            log.warn("[refreshAccessToken][response({})]", responseBody);
            return null;
        }
        accessToken = respDTO.getAccessToken();
        int expiresIn = respDTO.getExpiresIn() == null ? 0 : respDTO.getExpiresIn();
        tokenExpireTime = LocalDateTime.now().plusSeconds(Math.max(expiresIn - 60, 0));
        return accessToken;
    }

    private <T> T parseSafely(String responseBody, Class<T> clazz) {
        try {
            return JsonUtils.parseObject(responseBody, clazz);
        } catch (Exception ex) {
            log.warn("[parseSafely][clazz({}) response({}) parse failed]", clazz.getSimpleName(), responseBody, ex);
            return null;
        }
    }

    @Data
    public static class MemberWaterMeterTokenRespDTO {

        @com.fasterxml.jackson.annotation.JsonProperty("access_token")
        private String accessToken;
        @com.fasterxml.jackson.annotation.JsonProperty("token_type")
        private String tokenType;
        @com.fasterxml.jackson.annotation.JsonProperty("expires_in")
        private Integer expiresIn;
        private String scope;
    }

    @Data
    public static class MemberWaterMeterReadInfoRespDTO {

        private Integer code;
        private String msg;
        private MemberWaterMeterReadInfoDataDTO data;
    }

    @Data
    public static class MemberWaterMeterReadInfoDataDTO {

        private Integer responseCode;
        private String responseInfo;
        private MemberWaterMeterExtendDictDTO extendDict;
    }

    @Data
    public static class MemberWaterMeterExtendDictDTO {

        private String deviceClock;
        private String updateTime;
        private Integer deviceRSSI;
        private Integer deviceBuyTimes;
        private String deviceVoltage;
        private Long deviceCurrentData;
        private Integer deviceSettleDay;
        private Long deviceSettleDayData;
        private Long deviceBalance;
        private Long deviceTotalData;
        private Long deviceLastData;
        private Integer valveStatus;
        private Integer voltageStatus;
        private Integer feeStatus;
        private String deviceAddress;
        private String deviceUserName;
    }
}
