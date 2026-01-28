package cn.iocoder.yudao.module.member.controller.admin.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 小区下拉选项 Response VO")
@Data
public class MemberWaterCommunityOptionRespVO {

    @Schema(description = "小区编号", example = "e10adc3949ba59abbe56e057f20f883e")
    private String communityId;

    @Schema(description = "小区名称", example = "城市花园")
    private String communityName;
}
