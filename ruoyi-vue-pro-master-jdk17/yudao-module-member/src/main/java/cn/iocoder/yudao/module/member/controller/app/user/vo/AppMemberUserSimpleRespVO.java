package cn.iocoder.yudao.module.member.controller.app.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 APP - 用户简要信息 Response VO")
@Data
public class AppMemberUserSimpleRespVO {

    @Schema(description = "用户编号", example = "1")
    private Long id;

    @Schema(description = "用户昵称", example = "张三")
    private String nickname;

    @Schema(description = "用户手机号", example = "15601691300")
    private String mobile;
}
