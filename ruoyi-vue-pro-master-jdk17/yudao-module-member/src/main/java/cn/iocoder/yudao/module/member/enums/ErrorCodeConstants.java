package cn.iocoder.yudao.module.member.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * Member 错误码枚举类
 * <p>
 * member 系统，使用 1-004-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== 用户相关  1-004-001-000 ============
    ErrorCode USER_NOT_EXISTS = new ErrorCode(1_004_001_000, "用户不存在");
    ErrorCode USER_MOBILE_NOT_EXISTS = new ErrorCode(1_004_001_001, "手机号未注册用户");
    ErrorCode USER_MOBILE_USED = new ErrorCode(1_004_001_002, "修改手机失败，该手机号({})已经被使用");
    ErrorCode USER_POINT_NOT_ENOUGH = new ErrorCode(1_004_001_003, "用户积分余额不足");

    // ========== AUTH 模块 1-004-003-000 ==========
    ErrorCode AUTH_LOGIN_BAD_CREDENTIALS = new ErrorCode(1_004_003_000, "登录失败，账号密码不正确");
    ErrorCode AUTH_LOGIN_USER_DISABLED = new ErrorCode(1_004_003_001, "登录失败，账号被禁用");
    ErrorCode AUTH_SOCIAL_USER_NOT_FOUND = new ErrorCode(1_004_003_005, "登录失败，解析不到三方登录信息");
    ErrorCode AUTH_MOBILE_USED = new ErrorCode(1_004_003_007, "手机号已经被使用");

    // ========== 用户收件地址 1-004-004-000 ==========
    ErrorCode ADDRESS_NOT_EXISTS = new ErrorCode(1_004_004_000, "用户收件地址不存在");

    // ========== 居民报装房屋信息 1-004-005-000 ==========
    ErrorCode WATER_HOUSE_NOT_EXISTS = new ErrorCode(1_004_005_000, "居民报装房屋信息不存在");
    ErrorCode WATER_HOUSE_IMPORT_LIST_IS_EMPTY = new ErrorCode(1_004_005_001, "居民报装房屋信息导入数据为空");
    ErrorCode WATER_HOUSE_ALREADY_INSTALLED = new ErrorCode(1_004_005_002, "该房间已完成报装");
    ErrorCode WATER_APPLY_NOT_EXISTS = new ErrorCode(1_004_005_003, "居民报装申请不存在");
    ErrorCode WATER_RECHARGE_PACKAGE_NOT_EXISTS = new ErrorCode(1_004_005_004, "居民报装充值套餐不存在");
    ErrorCode WATER_APPLY_NOT_ALLOW = new ErrorCode(1_004_005_005, "居民报装申请不属于当前用户");
    ErrorCode WATER_APPLY_DEVICE_NO_REQUIRED = new ErrorCode(1_004_005_006, "施工完成时必须填写设备号");
    ErrorCode WATER_FAULT_INIT_NOT_EXISTS = new ErrorCode(1_004_005_007, "暂无可报修的报装信息");
    ErrorCode WATER_FAULT_NOT_EXISTS = new ErrorCode(1_004_005_008, "故障报修不存在");
    ErrorCode WATER_WORK_ORDER_NOT_EXISTS = new ErrorCode(1_004_005_009, "施工工单不存在");
    ErrorCode WATER_WORK_ORDER_NOT_ALLOWED = new ErrorCode(1_004_005_010, "没有权限操作施工工单");
    ErrorCode WATER_WORK_ORDER_STATUS_NOT_ALLOWED = new ErrorCode(1_004_005_011, "施工工单状态不支持此操作");
    ErrorCode WATER_DEVICE_NOT_EXISTS = new ErrorCode(1_004_005_012, "设备不存在");
    ErrorCode WATER_FEE_CONFIG_NOT_EXISTS = new ErrorCode(1_004_005_013, "水费配置不存在");
    ErrorCode WATER_APPLY_STATUS_NOT_ALLOWED = new ErrorCode(1_004_005_014, "居民报装状态不支持此操作");
    ErrorCode WATER_FAULT_STATUS_NOT_ALLOWED = new ErrorCode(1_004_005_015, "故障报修状态不支持此操作");

    //========== 用户标签 1-004-006-000 ==========
    ErrorCode TAG_NOT_EXISTS = new ErrorCode(1_004_006_000, "用户标签不存在");
    ErrorCode TAG_NAME_EXISTS = new ErrorCode(1_004_006_001, "用户标签已经存在");
    ErrorCode TAG_HAS_USER = new ErrorCode(1_004_006_002, "用户标签下存在用户，无法删除");

    //========== 积分配置 1-004-007-000 ==========

    //========== 积分记录 1-004-008-000 ==========
    ErrorCode POINT_RECORD_BIZ_NOT_SUPPORT = new ErrorCode(1_004_008_000, "用户积分记录业务类型不支持");

    //========== 签到配置 1-004-009-000 ==========
    ErrorCode SIGN_IN_CONFIG_NOT_EXISTS = new ErrorCode(1_004_009_000, "签到天数规则不存在");
    ErrorCode SIGN_IN_CONFIG_EXISTS = new ErrorCode(1_004_009_001, "签到天数规则已存在");

    //========== 签到配置 1-004-010-000 ==========
    ErrorCode SIGN_IN_RECORD_TODAY_EXISTS = new ErrorCode(1_004_010_000, "今日已签到，请勿重复签到");

    //========== 用户等级 1-004-011-000 ==========
    ErrorCode LEVEL_NOT_EXISTS = new ErrorCode(1_004_011_000, "用户等级不存在");
    ErrorCode LEVEL_NAME_EXISTS = new ErrorCode(1_004_011_001, "用户等级名称[{}]已被使用");
    ErrorCode LEVEL_VALUE_EXISTS = new ErrorCode(1_004_011_002, "用户等级值[{}]已被[{}]使用");
    ErrorCode LEVEL_EXPERIENCE_MIN = new ErrorCode(1_004_011_003, "升级经验必须大于上一个等级[{}]设置的升级经验[{}]");
    ErrorCode LEVEL_EXPERIENCE_MAX = new ErrorCode(1_004_011_004, "升级经验必须小于下一个等级[{}]设置的升级经验[{}]");
    ErrorCode LEVEL_HAS_USER = new ErrorCode(1_004_011_005, "用户等级下存在用户，无法删除");

    ErrorCode EXPERIENCE_BIZ_NOT_SUPPORT = new ErrorCode(1_004_011_201, "用户经验业务类型不支持");

    //========== 用户分组 1-004-012-000 ==========
    ErrorCode GROUP_NOT_EXISTS = new ErrorCode(1_004_012_000, "用户分组不存在");
    ErrorCode GROUP_HAS_USER = new ErrorCode(1_004_012_001, "用户分组下存在用户，无法删除");

    // 远程错误 1-004-013-000
    ErrorCode ERROR_WITH_THE_REMOTE_INTERFACE = new ErrorCode(1_004_013_005, "水表信息异常");


}
