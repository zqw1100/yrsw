package cn.iocoder.yudao.module.member.framework.water.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 物联网水表接口配置
 */
@Component
@ConfigurationProperties(prefix = "yudao.member.water-meter")
@Data
public class MemberWaterMeterProperties {

    /**
     * 接口前缀地址
     */
    private String baseUrl = "http://meter-api-dev.ucn.plus";
    /**
     * 鉴权请求路径
     */
    private String tokenPath = "/auth/oauth/token?grant_type=client_credentials&scope=open";
    /**
     * 鉴权请求 Header
     */
    private String tokenAuthorization = "Basic b3BlbmNsaWVudDpvcGVuY2xpZW50";
    /**
     * 设备读取接口路径前缀
     */
    private String readInfoPath = "/open/openapi/read/info";
    /**
     * 设备新增接口路径前缀
     */
    private String addDevicePath = "/open/openapi/v3/add/device";
    /**
     * 阀门操作接口路径前缀
     */
    private String valvePath = "/open/openapi/valve";
    /**
     * 换表接口路径前缀
     */
    private String changeDevicePath = "/open/openapi/device";
    /**
     * 上传周期接口路径前缀
     */
    private String uploadModePath = "/open/openapi/device";
    /**
     * 默认设备版本名称
     */
    private String deviceVersionName = "NB-IoT水表";
    /**
     * openId
     */
    private String openId = "1";
}
