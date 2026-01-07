package cn.iocoder.yudao.module.member.controller.app.water.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "用户 APP - 故障报修分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class AppWaterFaultPageReqVO extends PageParam {

    @Schema(description = "设备号", example = "3706028009944")
    private String deviceNo;
}
