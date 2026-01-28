package cn.iocoder.yudao.module.member.controller.admin.water.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 设备用水历史分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class MemberWaterDeviceHistoryPageReqVO extends PageParam {

    @Schema(description = "设备号", example = "340119025153")
    private String deviceNo;

    @Schema(description = "小区编号", example = "e10adc3949ba59abbe56e057f20f883e")
    private String communityId;

    @Schema(description = "设备所属用户名称", example = "张三")
    private String deviceUserName;

    @Schema(description = "设备用户地址", example = "某小区 1 栋 101")
    private String deviceAddress;

    @Schema(description = "阀门状态", example = "1")
    private Integer valveStatus;

    @Schema(description = "上报原因", example = "0")
    private Integer reportReason;

    @Schema(description = "抄收时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] deviceUpdateTime;
}
