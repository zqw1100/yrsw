package cn.iocoder.yudao.module.member.controller.admin.water;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDashboardRespVO;
import cn.iocoder.yudao.module.member.service.water.MemberWaterDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 用户用水统计")
@RestController
@RequestMapping("/member/water-dashboard")
public class MemberWaterDashboardController {

    @Resource
    private MemberWaterDashboardService dashboardService;

    @GetMapping
    @Operation(summary = "获取用户用水统计")
    public CommonResult<MemberWaterDashboardRespVO> getDashboard() {
        return success(dashboardService.getDashboard());
    }
}
