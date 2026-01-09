package cn.iocoder.yudao.module.member.enums.water;

import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 水费配置状态枚举
 *
 * 说明：该表的状态定义为 0 停用、1 启用，与 {@link cn.iocoder.yudao.framework.common.enums.CommonStatusEnum}
 * 不一致，因此单独定义枚举避免误用。
 */
@Getter
@AllArgsConstructor
public enum MemberWaterFeeConfigStatusEnum implements ArrayValuable<Integer> {

    DISABLE(1, "停用"),
    ENABLE(0, "启用");

    public static final Integer[] ARRAYS =
            Arrays.stream(values()).map(MemberWaterFeeConfigStatusEnum::getStatus).toArray(Integer[]::new);

    /**
     * 状态值
     */
    private final Integer status;
    /**
     * 状态名
     */
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }
}
