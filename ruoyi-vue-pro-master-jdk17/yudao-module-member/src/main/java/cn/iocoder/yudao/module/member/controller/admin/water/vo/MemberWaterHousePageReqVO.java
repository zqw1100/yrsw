package cn.iocoder.yudao.module.member.controller.admin.water.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 居民报装房屋信息分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class MemberWaterHousePageReqVO extends PageParam {

    @Schema(description = "地区编号（区县）", example = "110105")
    private Long areaId;

    @Schema(description = "小区名称", example = "城市花园")
    private String communityName;

    @Schema(description = "小区编号", example = "e10adc3949ba59abbe56e057f20f883e")
    private String communityId;

    @Schema(description = "楼栋名称", example = "1号楼")
    private String buildingName;

    @Schema(description = "单元名称", example = "1单元")
    private String unitName;

    @Schema(description = "房间号", example = "302")
    private String roomNo;

    @Schema(description = "报装状态（0 未报装 1 已报装）", example = "0")
    private Integer installStatus;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;
}
