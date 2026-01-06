package cn.iocoder.yudao.module.member.controller.app.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "用户 APP - 施工工单 Response VO")
@Data
public class AppWaterWorkOrderRespVO {

    @Schema(description = "工单编号", example = "1")
    private Long id;

    @Schema(description = "工单类型（0 报装 1 报修）", example = "0")
    private Integer orderType;

    @Schema(description = "施工状态（0 待接受 1 待施工 2 施工中 3 已完成）", example = "0")
    private Integer status;

    @Schema(description = "业务编号", example = "1024")
    private Long bizId;

    @Schema(description = "联系人姓名", example = "张三")
    private String contactName;

    @Schema(description = "联系人电话", example = "15601691300")
    private String contactMobile;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "设备号", example = "WM10001")
    private String deviceNo;

    @Schema(description = "问题描述", example = "水表无法启动")
    private String feedback;

    @Schema(description = "指派施工人员编号", example = "2")
    private Long workerId;

    @Schema(description = "指派施工人员名称", example = "李四")
    private String workerName;

    @Schema(description = "施工前图片")
    private List<String> beforeImageUrls;

    @Schema(description = "施工前备注")
    private String beforeRemark;

    @Schema(description = "施工后图片")
    private List<String> afterImageUrls;

    @Schema(description = "施工后备注")
    private String afterRemark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
