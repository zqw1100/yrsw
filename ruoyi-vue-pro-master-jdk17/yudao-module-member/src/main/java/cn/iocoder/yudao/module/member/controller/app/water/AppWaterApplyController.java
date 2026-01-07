package cn.iocoder.yudao.module.member.controller.app.water;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterApplyRespVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterApplyCompleteReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterApplyCreateReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterApplyPageReqVO;
import cn.iocoder.yudao.module.member.service.water.MemberWaterApplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 APP - 居民报装申请")
@RestController
@RequestMapping("/member/water-apply")
@Validated
public class AppWaterApplyController {

    @Resource
    private MemberWaterApplyService applyService;

    @PostMapping("/create")
    @Operation(summary = "创建居民报装申请")
    public CommonResult<Long> createApply(@Valid @RequestBody AppWaterApplyCreateReqVO createReqVO) {
        return success(applyService.createApply(createReqVO));
    }

    @PutMapping("/complete")
    @Operation(summary = "补充居民报装资料")
    public CommonResult<Boolean> completeApply(@Valid @RequestBody AppWaterApplyCompleteReqVO completeReqVO) {
        applyService.completeApply(completeReqVO);
        return success(true);
    }

    @GetMapping("/page")
    @Operation(summary = "获得用户居民报装申请分页")
    public CommonResult<PageResult<MemberWaterApplyRespVO>> getApplyPage(
            @Valid AppWaterApplyPageReqVO pageReqVO) {
        return success(applyService.getApplyPage(getLoginUserId(), pageReqVO));
    }

    @GetMapping("/check-device")
    @Operation(summary = "校验设备号是否已绑定")
    public CommonResult<Boolean> checkDeviceNo(@RequestParam("deviceNo") @NotBlank String deviceNo,
                                               @RequestParam(value = "excludeApplyId", required = false) Long excludeApplyId) {
        return success(applyService.isDeviceNoUsed(deviceNo, excludeApplyId));
    }
}
