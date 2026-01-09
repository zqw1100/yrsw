package cn.iocoder.yudao.module.member.controller.app.water;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDeviceHistoryPageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDeviceHistoryRespVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterDeviceHistoryPageReqVO;
import cn.iocoder.yudao.module.member.service.water.MemberWaterDeviceHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 设备用水历史")
@RestController
@RequestMapping("/member/water-device-history")
@Validated
public class AppWaterDeviceHistoryController {

    @Resource
    private MemberWaterDeviceHistoryService historyService;

    @GetMapping("/page")
    @Operation(summary = "获得设备用水历史分页")
    public CommonResult<PageResult<MemberWaterDeviceHistoryRespVO>> getHistoryPage(
            @Valid AppWaterDeviceHistoryPageReqVO pageReqVO) {
        MemberWaterDeviceHistoryPageReqVO reqVO = BeanUtils.toBean(pageReqVO, MemberWaterDeviceHistoryPageReqVO.class);
        return success(historyService.getHistoryPage(reqVO));
    }
}
