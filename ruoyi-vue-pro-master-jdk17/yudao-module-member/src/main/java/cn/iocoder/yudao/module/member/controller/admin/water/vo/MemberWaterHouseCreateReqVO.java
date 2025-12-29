package cn.iocoder.yudao.module.member.controller.admin.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - 居民报装房屋信息创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class MemberWaterHouseCreateReqVO extends MemberWaterHouseBaseVO {
}
