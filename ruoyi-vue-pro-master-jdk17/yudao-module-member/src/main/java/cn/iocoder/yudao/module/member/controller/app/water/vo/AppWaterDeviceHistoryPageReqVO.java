package cn.iocoder.yudao.module.member.controller.app.water.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 设备用水历史分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class AppWaterDeviceHistoryPageReqVO extends PageParam {

    @Schema(description = "设备号", requiredMode = Schema.RequiredMode.REQUIRED, example = "340119025153")
    @NotBlank(message = "设备号不能为空")
    private String deviceNo;

    @Schema(description = "抄收时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] deviceUpdateTime;
}
