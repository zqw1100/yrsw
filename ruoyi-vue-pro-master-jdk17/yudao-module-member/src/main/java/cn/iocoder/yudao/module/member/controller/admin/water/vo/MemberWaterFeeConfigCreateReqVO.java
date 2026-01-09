package cn.iocoder.yudao.module.member.controller.admin.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - 水费配置创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class MemberWaterFeeConfigCreateReqVO extends MemberWaterFeeConfigBaseVO {
}
