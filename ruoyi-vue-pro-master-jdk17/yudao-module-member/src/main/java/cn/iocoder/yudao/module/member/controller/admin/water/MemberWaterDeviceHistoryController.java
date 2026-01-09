package cn.iocoder.yudao.module.member.controller.admin.water;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDeviceHistoryPageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDeviceHistoryRespVO;
import cn.iocoder.yudao.module.member.service.water.MemberWaterDeviceHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 设备用水历史")
@RestController
@RequestMapping("/member/water-device-history")
@Validated
public class MemberWaterDeviceHistoryController {

    @Resource
    private MemberWaterDeviceHistoryService historyService;

    @GetMapping("/page")
    @Operation(summary = "获得设备用水历史分页")
    @PreAuthorize("@ss.hasPermission('member:water-device-history:query')")
    public CommonResult<PageResult<MemberWaterDeviceHistoryRespVO>> getHistoryPage(
            @Valid MemberWaterDeviceHistoryPageReqVO pageReqVO) {
        return success(historyService.getHistoryPage(pageReqVO));
    }
}
