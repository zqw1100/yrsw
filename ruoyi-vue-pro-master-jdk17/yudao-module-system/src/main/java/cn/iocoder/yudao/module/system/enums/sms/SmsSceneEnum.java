package cn.iocoder.yudao.module.system.enums.sms;

import cn.hutool.core.util.ArrayUtil;
import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 用户短信验证码发送场景的枚举
 *
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum SmsSceneEnum implements ArrayValuable<Integer> {

    MEMBER_LOGIN(1, "SMS_329271055", "会员用户 - 手机号登陆"),
    MEMBER_UPDATE_MOBILE(2, "SMS_329271055", "会员用户 - 修改手机"),
    MEMBER_UPDATE_PASSWORD(3, "SMS_329271055", "会员用户 - 修改密码"),
    MEMBER_RESET_PASSWORD(4, "SMS_329271055", "会员用户 - 忘记密码"),

    ADMIN_MEMBER_LOGIN(21, "SMS_329271055", "后台用户 - 手机号登录"),
    ADMIN_MEMBER_REGISTER(22, "SMS_329271055", "后台用户 - 手机号注册"),
    ADMIN_MEMBER_RESET_PASSWORD(23, "SMS_329271055", "后台用户 - 忘记密码");

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(SmsSceneEnum::getScene).toArray(Integer[]::new);

    /**
     * 验证场景的编号
     */
    private final Integer scene;
    /**
     * 模版编码
     */
    private final String templateCode;
    /**
     * 描述
     */
    private final String description;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

    public static SmsSceneEnum getCodeByScene(Integer scene) {
        return ArrayUtil.firstMatch(sceneEnum -> sceneEnum.getScene().equals(scene),
                values());
    }

}
