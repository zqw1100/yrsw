package cn.iocoder.yudao.module.member.controller.admin.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 水费结算流水 Response VO")
@Data
public class MemberWaterFeeBillRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "设备号", requiredMode = Schema.RequiredMode.REQUIRED, example = "340119025153")
    private String deviceNo;

    @Schema(description = "小区编号", example = "e10adc3949ba59abbe56e057f20f883e")
    private String communityId;

    @Schema(description = "统计日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate statDate;

    @Schema(description = "总用水量（升）", example = "100")
    private Long totalUsage;

    @Schema(description = "上次用水量（升）", example = "80")
    private Long lastTotalUsage;

    @Schema(description = "用量差额（升）", example = "20")
    private Long usageDiff;

    @Schema(description = "费用（分）", example = "100")
    private Integer fee;

    @Schema(description = "余额（分）", example = "500")
    private Integer balance;

    @Schema(description = "钱包编号", example = "123")
    private Long walletId;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;
}
