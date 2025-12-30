package cn.iocoder.yudao.module.member.controller.admin.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Schema(description = "管理后台 - 居民报装房屋信息导入 Response VO")
@Data
@Builder
public class MemberWaterHouseImportRespVO {

    @Schema(description = "新增成功列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> createKeys;

    @Schema(description = "更新成功列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> updateKeys;

    @Schema(description = "导入失败列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private Map<String, String> failureKeys = new LinkedHashMap<>();
}
