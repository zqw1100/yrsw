package cn.iocoder.yudao.module.member.controller.app.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 APP - 居民报装房间 Response VO")
@Data
public class AppWaterHouseRoomRespVO {

    @Schema(description = "房间号", requiredMode = Schema.RequiredMode.REQUIRED, example = "302")
    private String roomNo;

    @Schema(description = "报装状态（0 未报装 1 已报装）", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer installStatus;
}
