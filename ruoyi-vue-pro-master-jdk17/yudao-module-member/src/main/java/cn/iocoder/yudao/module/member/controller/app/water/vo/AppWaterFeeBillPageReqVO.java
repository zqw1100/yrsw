package cn.iocoder.yudao.module.member.controller.app.water.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;

@Schema(description = "用户 APP - 水费结算流水分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class AppWaterFeeBillPageReqVO extends PageParam {

    @Schema(description = "设备号", requiredMode = Schema.RequiredMode.REQUIRED, example = "340119025153")
    @NotBlank(message = "设备号不能为空")
    private String deviceNo;

    @Schema(description = "统计日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private LocalDate[] statDate;
}
