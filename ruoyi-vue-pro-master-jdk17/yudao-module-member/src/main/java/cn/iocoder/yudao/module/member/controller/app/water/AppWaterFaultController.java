package cn.iocoder.yudao.module.member.controller.app.water;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterFaultCreateReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterFaultInitRespVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterFaultPageReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterFaultRespVO;
import cn.iocoder.yudao.module.member.service.water.MemberWaterFaultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 APP - 故障报修")
@RestController
@RequestMapping("/member/water-fault")
@Validated
public class AppWaterFaultController {

    @Resource
    private MemberWaterFaultService faultService;

    @GetMapping("/init")
    @Operation(summary = "获得故障报修信息初始化")
    public CommonResult<AppWaterFaultInitRespVO> getFaultInit() {
        return success(faultService.getFaultInit(getLoginUserId()));
    }

    @PostMapping("/create")
    @Operation(summary = "创建故障报修")
    public CommonResult<Long> createFault(@Valid @RequestBody AppWaterFaultCreateReqVO createReqVO) {
        return success(faultService.createFault(getLoginUserId(), createReqVO));
    }

    @GetMapping("/page")
    @Operation(summary = "获得故障报修分页")
    public CommonResult<PageResult<AppWaterFaultRespVO>> getFaultPage(@Valid AppWaterFaultPageReqVO pageReqVO) {
        return success(faultService.getFaultPage(getLoginUserId(), pageReqVO));
    }
}
