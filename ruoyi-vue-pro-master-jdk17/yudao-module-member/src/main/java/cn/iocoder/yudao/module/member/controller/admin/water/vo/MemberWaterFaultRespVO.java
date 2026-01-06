package cn.iocoder.yudao.module.member.controller.admin.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 故障报修 Response VO")
@Data
public class MemberWaterFaultRespVO {

    @Schema(description = "编号", example = "1")
    private Long id;

    @Schema(description = "用户编号", example = "1024")
    private Long userId;

    @Schema(description = "报装房屋编号", example = "2048")
    private Long waterHouseId;

    @Schema(description = "报装申请编号", example = "4096")
    private Long applyId;

    @Schema(description = "户主姓名", example = "张三")
    private String ownerName;

    @Schema(description = "设备号", example = "SN-1001")
    private String deviceNo;

    @Schema(description = "地区名称", example = "高新区")
    private String areaName;

    @Schema(description = "小区名称", example = "桂桂小区")
    private String communityName;

    @Schema(description = "楼栋名称", example = "1栋")
    private String buildingName;

    @Schema(description = "单元名称", example = "1单元")
    private String unitName;

    @Schema(description = "房间号", example = "101")
    private String roomNo;

    @Schema(description = "联系电话", example = "13800138000")
    private String contactMobile;

    @Schema(description = "故障类型", example = "device-fault-001")
    private String faultCode;

    @Schema(description = "问题反馈", example = "水表无法启动")
    private String feedback;

    @Schema(description = "优先级（1 低 2 中 3 高）", example = "1")
    private Integer priority;

    @Schema(description = "故障图片")
    private List<String> imageUrls;

    @Schema(description = "处理状态（0 待处理 1 处理中 2 已完成）", example = "0")
    private Integer processStatus;

    @Schema(description = "备注", example = "尽快处理")
    private String remark;

    @Schema(description = "创建时间", example = "2025-01-01 10:00:00")
    private LocalDateTime createTime;
}
