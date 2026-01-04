package cn.iocoder.yudao.module.member.controller.app.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "用户 APP - 故障报修 Response VO")
@Data
public class AppWaterFaultRespVO {

    @Schema(description = "编号", example = "1")
    private Long id;

    @Schema(description = "户主姓名", example = "张三")
    private String ownerName;

    @Schema(description = "设备号", example = "3706028009944")
    private String deviceNo;

    @Schema(description = "地区名称", example = "山东省烟台市")
    private String areaName;

    @Schema(description = "小区名称", example = "某某社区")
    private String communityName;

    @Schema(description = "楼栋名称", example = "1号楼")
    private String buildingName;

    @Schema(description = "单元名称", example = "2单元")
    private String unitName;

    @Schema(description = "房间号", example = "201")
    private String roomNo;

    @Schema(description = "联系电话", example = "13800000000")
    private String contactMobile;

    @Schema(description = "故障原因", example = "leak")
    private String faultCode;

    @Schema(description = "问题反馈", example = "设备漏水")
    private String feedback;

    @Schema(description = "优先级（1低 2中 3高）", example = "1")
    private Integer priority;

    @Schema(description = "故障图片列表")
    private List<String> imageUrls;

    @Schema(description = "处理状态（0待处理 1处理中 2已完成）", example = "0")
    private Integer processStatus;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
