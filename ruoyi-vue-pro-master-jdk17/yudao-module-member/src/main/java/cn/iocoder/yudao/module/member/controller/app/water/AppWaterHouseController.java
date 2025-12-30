package cn.iocoder.yudao.module.member.controller.app.water;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterHouseRoomRespVO;
import cn.iocoder.yudao.module.member.service.water.MemberWaterHouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 居民报装房屋信息")
@RestController
@RequestMapping("/member/water-house")
@Validated
public class AppWaterHouseController {

    @Resource
    private MemberWaterHouseService waterHouseService;

    @GetMapping("/community-list")
    @Operation(summary = "获得小区列表")
    @Parameter(name = "areaId", description = "地区编号", required = true)
    public CommonResult<List<String>> getCommunityList(@RequestParam("areaId") Long areaId) {
        return success(waterHouseService.getCommunityNameList(areaId));
    }

    @GetMapping("/building-list")
    @Operation(summary = "获得楼栋列表")
    @Parameter(name = "areaId", description = "地区编号", required = true)
    @Parameter(name = "communityName", description = "小区名称", required = true)
    public CommonResult<List<String>> getBuildingList(@RequestParam("areaId") Long areaId,
                                                      @RequestParam("communityName") String communityName) {
        return success(waterHouseService.getBuildingNameList(areaId, communityName));
    }

    @GetMapping("/unit-list")
    @Operation(summary = "获得单元列表")
    @Parameter(name = "areaId", description = "地区编号", required = true)
    @Parameter(name = "communityName", description = "小区名称", required = true)
    @Parameter(name = "buildingName", description = "楼栋名称", required = true)
    public CommonResult<List<String>> getUnitList(@RequestParam("areaId") Long areaId,
                                                  @RequestParam("communityName") String communityName,
                                                  @RequestParam("buildingName") String buildingName) {
        return success(waterHouseService.getUnitNameList(areaId, communityName, buildingName));
    }

    @GetMapping("/room-list")
    @Operation(summary = "获得房间列表")
    @Parameter(name = "areaId", description = "地区编号", required = true)
    @Parameter(name = "communityName", description = "小区名称", required = true)
    @Parameter(name = "buildingName", description = "楼栋名称", required = true)
    @Parameter(name = "unitName", description = "单元名称", required = true)
    public CommonResult<List<AppWaterHouseRoomRespVO>> getRoomList(@RequestParam("areaId") Long areaId,
                                                                   @RequestParam("communityName") String communityName,
                                                                   @RequestParam("buildingName") String buildingName,
                                                                   @RequestParam("unitName") String unitName) {
        return success(waterHouseService.getRoomList(areaId, communityName, buildingName, unitName));
    }
}
