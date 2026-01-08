package cn.iocoder.yudao.module.member.controller.admin.water;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterApplyPageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterApplyRespVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterApplyStatusUpdateReqVO;
import cn.iocoder.yudao.module.member.service.water.MemberWaterApplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 居民报装申请")
@RestController
@RequestMapping("/member/water-apply")
@Validated
public class MemberWaterApplyController {

    @Resource
    private MemberWaterApplyService applyService;

    @GetMapping("/page")
    @Operation(summary = "获得居民报装申请分页")
    @PreAuthorize("@ss.hasPermission('member:water-apply:query')")
    public CommonResult<PageResult<MemberWaterApplyRespVO>> getApplyPage(@Valid MemberWaterApplyPageReqVO pageReqVO) {
        return success(applyService.getApplyPage(pageReqVO));
    }

    @PutMapping("/update-status")
    @Operation(summary = "更新居民报装处理状态")
    @PreAuthorize("@ss.hasPermission('member:water-apply:update-status')")
    public CommonResult<Boolean> updateApplyStatus(@Valid @RequestBody MemberWaterApplyStatusUpdateReqVO updateReqVO) {
        applyService.updateApplyStatus(updateReqVO);
        return success(true);
    }

    @GetMapping("/check-device")
    public CommonResult<Boolean> checkDeviceNo(@RequestParam("deviceNo") @NotBlank String deviceNo,
                                               @RequestParam(value = "excludeApplyId", required = false) Long excludeApplyId) {
        return success(applyService.isDeviceNoUsed(deviceNo, excludeApplyId));
    }
}
