package cn.iocoder.yudao.module.member.controller.admin.water;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFaultPageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFaultRespVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFaultStatusUpdateReqVO;
import cn.iocoder.yudao.module.member.service.water.MemberWaterFaultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 故障报修")
@RestController
@RequestMapping("/member/water-fault")
@Validated
public class MemberWaterFaultController {

    @Resource
    private MemberWaterFaultService faultService;

    @GetMapping("/page")
    @Operation(summary = "获得故障报修分页")
    @PreAuthorize("@ss.hasPermission('member:water-fault:query')")
    public CommonResult<PageResult<MemberWaterFaultRespVO>> getFaultPage(@Valid MemberWaterFaultPageReqVO pageReqVO) {
        return success(faultService.getFaultPage(pageReqVO));
    }

    @PutMapping("/update-status")
    @Operation(summary = "更新故障报修处理状态")
    @PreAuthorize("@ss.hasPermission('member:water-fault:update-status')")
    public CommonResult<Boolean> updateFaultStatus(@Valid @RequestBody MemberWaterFaultStatusUpdateReqVO updateReqVO) {
        faultService.updateFaultStatus(updateReqVO);
        return success(true);
    }
}
