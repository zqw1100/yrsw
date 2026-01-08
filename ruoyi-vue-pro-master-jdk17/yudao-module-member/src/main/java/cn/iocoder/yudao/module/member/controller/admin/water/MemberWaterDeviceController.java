package cn.iocoder.yudao.module.member.controller.admin.water;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDeviceChangeReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDevicePageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDeviceRespVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDeviceUploadModeReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDeviceValveReqVO;
import cn.iocoder.yudao.module.member.service.water.MemberWaterDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 设备信息")
@RestController
@RequestMapping("/member/water-device")
@Validated
public class MemberWaterDeviceController {

    @Resource
    private MemberWaterDeviceService deviceService;

    @GetMapping("/page")
    @Operation(summary = "获得设备信息分页")
    @PreAuthorize("@ss.hasPermission('member:water-device:query')")
    public CommonResult<PageResult<MemberWaterDeviceRespVO>> getDevicePage(@Valid MemberWaterDevicePageReqVO pageReqVO) {
        return success(deviceService.getDevicePage(pageReqVO));
    }

    @PutMapping("/refresh")
    @Operation(summary = "刷新设备信息")
    @PreAuthorize("@ss.hasPermission('member:water-device:update')")
    public CommonResult<Boolean> refreshDevice(@RequestParam("id") Long id) {
        deviceService.refreshDevice(id);
        return success(true);
    }

    @PostMapping("/valve")
    @Operation(summary = "阀门操作")
    @PreAuthorize("@ss.hasPermission('member:water-device:update')")
    public CommonResult<Boolean> operateValve(@Valid @RequestBody MemberWaterDeviceValveReqVO reqVO) {
        return success(deviceService.operateValve(reqVO.getDeviceNo(), reqVO.getValveStatus()));
    }

    @PostMapping("/change-device")
    @Operation(summary = "换表")
    @PreAuthorize("@ss.hasPermission('member:water-device:update')")
    public CommonResult<Boolean> changeDevice(@Valid @RequestBody MemberWaterDeviceChangeReqVO reqVO) {
        return success(deviceService.changeDevice(reqVO.getOriginalDeviceCode(), reqVO.getNewDeviceCode(),
                reqVO.getOriginalTotalData()));
    }

    @PostMapping("/upload-mode")
    @Operation(summary = "设置设备上传周期")
    @PreAuthorize("@ss.hasPermission('member:water-device:update')")
    public CommonResult<Boolean> setUploadMode(@Valid @RequestBody MemberWaterDeviceUploadModeReqVO reqVO) {
        return success(deviceService.setUploadMode(reqVO.getDeviceCode(), reqVO.getUploadType(), reqVO.getValue()));
    }
}
