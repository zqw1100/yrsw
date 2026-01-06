package cn.iocoder.yudao.module.member.controller.app.water.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "用户 APP - 施工工单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class AppWaterWorkOrderPageReqVO extends PageParam {

    @Schema(description = "工单类型（0 报装 1 报修）", example = "0")
    private Integer orderType;

    @Schema(description = "施工状态（0 待接受 1 待施工 2 施工中 3 已完成）", example = "0")
    private Integer status;
}
