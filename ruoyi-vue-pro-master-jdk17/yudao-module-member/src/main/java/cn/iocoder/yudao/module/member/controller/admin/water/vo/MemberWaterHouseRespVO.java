package cn.iocoder.yudao.module.member.controller.admin.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 居民报装房屋信息 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class MemberWaterHouseRespVO extends MemberWaterHouseBaseVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;
}
