package cn.iocoder.yudao.module.member.controller.app.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 APP - 故障报修初始化 Response VO")
@Data
public class AppWaterFaultInitRespVO {

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
}
