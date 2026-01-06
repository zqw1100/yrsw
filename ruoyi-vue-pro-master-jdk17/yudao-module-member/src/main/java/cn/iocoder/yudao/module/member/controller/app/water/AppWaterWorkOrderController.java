package cn.iocoder.yudao.module.member.controller.app.water;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterWorkOrderAcceptReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterWorkOrderAssignReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterWorkOrderFinishReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterWorkOrderPageReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterWorkOrderRespVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterWorkOrderRevokeReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterWorkOrderStartReqVO;
import cn.iocoder.yudao.module.member.service.water.MemberWaterWorkOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 APP - 施工工单")
@RestController
@RequestMapping("/member/water-work-order")
@Validated
public class AppWaterWorkOrderController {

    @Resource
    private MemberWaterWorkOrderService workOrderService;

    @GetMapping("/page")
    @Operation(summary = "获得施工工单分页")
    public CommonResult<PageResult<AppWaterWorkOrderRespVO>> getWorkOrderPage(@Valid AppWaterWorkOrderPageReqVO pageReqVO) {
        return success(workOrderService.getWorkOrderPage(getLoginUserId(), pageReqVO));
    }

    @GetMapping("/get")
    @Operation(summary = "获得施工工单详情")
    public CommonResult<AppWaterWorkOrderRespVO> getWorkOrder(@Valid @RequestParam("id") Long id) {
        return success(workOrderService.getWorkOrder(getLoginUserId(), id));
    }

    @PostMapping("/assign")
    @Operation(summary = "指派施工工单")
    public CommonResult<Boolean> assignWorkOrder(@Valid @RequestBody AppWaterWorkOrderAssignReqVO reqVO) {
        workOrderService.assignWorkOrder(getLoginUserId(), reqVO);
        return success(true);
    }

    @PostMapping("/revoke")
    @Operation(summary = "撤回施工工单")
    public CommonResult<Boolean> revokeWorkOrder(@Valid @RequestBody AppWaterWorkOrderRevokeReqVO reqVO) {
        workOrderService.revokeWorkOrder(getLoginUserId(), reqVO);
        return success(true);
    }

    @PostMapping("/accept")
    @Operation(summary = "接受施工工单")
    public CommonResult<Boolean> acceptWorkOrder(@Valid @RequestBody AppWaterWorkOrderAcceptReqVO reqVO) {
        workOrderService.acceptWorkOrder(getLoginUserId(), reqVO);
        return success(true);
    }

    @PostMapping("/start")
    @Operation(summary = "施工前信息提交")
    public CommonResult<Boolean> startWorkOrder(@Valid @RequestBody AppWaterWorkOrderStartReqVO reqVO) {
        workOrderService.startWorkOrder(getLoginUserId(), reqVO);
        return success(true);
    }

    @PostMapping("/finish")
    @Operation(summary = "施工完成")
    public CommonResult<Boolean> finishWorkOrder(@Valid @RequestBody AppWaterWorkOrderFinishReqVO reqVO) {
        workOrderService.finishWorkOrder(getLoginUserId(), reqVO);
        return success(true);
    }
}
