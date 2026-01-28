package cn.iocoder.yudao.module.member.controller.admin.water.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - 故障报修分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class MemberWaterFaultPageReqVO extends PageParam {

    @Schema(description = "小区名称", example = "桂桂小区")
    private String communityName;

    @Schema(description = "小区编号", example = "e10adc3949ba59abbe56e057f20f883e")
    private String communityId;

    @Schema(description = "楼栋名称", example = "1栋")
    private String buildingName;

    @Schema(description = "单元名称", example = "1单元")
    private String unitName;

    @Schema(description = "房间号", example = "101")
    private String roomNo;

    @Schema(description = "联系人手机号", example = "13800138000")
    private String contactMobile;

    @Schema(description = "户主姓名", example = "张三")
    private String ownerName;

    @Schema(description = "设备号", example = "SN-1001")
    private String deviceNo;

    @Schema(description = "故障类型", example = "device-fault-001")
    private String faultCode;

    @Schema(description = "优先级（1 低 2 中 3 高）", example = "1")
    private Integer priority;

    @Schema(description = "处理状态（0 待处理 1 处理中 2 已完成）", example = "0")
    private Integer processStatus;
}
