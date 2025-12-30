package cn.iocoder.yudao.module.member.controller.admin.water.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 居民报装充值套餐分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MemberWaterRechargePackagePageReqVO extends PageParam {

    @Schema(description = "套餐名称", example = "首次充值")
    private String name;

    @Schema(description = "状态（0 停用 1 启用）", example = "1")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime[] createTime;
}
