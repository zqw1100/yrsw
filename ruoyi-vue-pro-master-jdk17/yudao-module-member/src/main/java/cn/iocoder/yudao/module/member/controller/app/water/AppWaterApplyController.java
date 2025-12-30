package cn.iocoder.yudao.module.member.controller.app.water;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterApplyCompleteReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterApplyCreateReqVO;
import cn.iocoder.yudao.module.member.service.water.MemberWaterApplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

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
}
