package cn.iocoder.yudao.module.member.controller.app.water.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 App - 居民报装申请分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class AppWaterApplyPageReqVO extends PageParam {

    @Schema(description = "申请状态（0 待补充资料 1 已提交）", example = "1")
    private Integer applyStatus;

    @Schema(description = "处理状态", example = "0")
    private Integer processStatus;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;
}
