package cn.iocoder.yudao.module.member.controller.admin.water;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterHouseCreateReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterHousePageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterHouseRespVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterHouseUpdateReqVO;
import cn.iocoder.yudao.module.member.convert.water.MemberWaterHouseConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterHouseDO;
import cn.iocoder.yudao.module.member.service.water.MemberWaterHouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 居民报装房屋信息")
@RestController
@RequestMapping("/member/water-house")
@Validated
public class MemberWaterHouseController {

    @Resource
    private MemberWaterHouseService waterHouseService;

    @PostMapping("/create")
    @Operation(summary = "创建居民报装房屋信息")
    @PreAuthorize("@ss.hasPermission('member:water-house:create')")
    public CommonResult<Long> createWaterHouse(@Valid @RequestBody MemberWaterHouseCreateReqVO createReqVO) {
        return success(waterHouseService.createMemberWaterHouse(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新居民报装房屋信息")
    @PreAuthorize("@ss.hasPermission('member:water-house:update')")
    public CommonResult<Boolean> updateWaterHouse(@Valid @RequestBody MemberWaterHouseUpdateReqVO updateReqVO) {
        waterHouseService.updateMemberWaterHouse(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除居民报装房屋信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:water-house:delete')")
    public CommonResult<Boolean> deleteWaterHouse(@RequestParam("id") Long id) {
        waterHouseService.deleteMemberWaterHouse(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得居民报装房屋信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:water-house:query')")
    public CommonResult<MemberWaterHouseRespVO> getWaterHouse(@RequestParam("id") Long id) {
        MemberWaterHouseDO data = waterHouseService.getMemberWaterHouse(id);
        return success(MemberWaterHouseConvert.INSTANCE.convert(data));
    }

    @GetMapping("/page")
    @Operation(summary = "获得居民报装房屋信息分页")
    @PreAuthorize("@ss.hasPermission('member:water-house:query')")
    public CommonResult<PageResult<MemberWaterHouseRespVO>> getWaterHousePage(@Valid MemberWaterHousePageReqVO pageReqVO) {
        PageResult<MemberWaterHouseDO> pageResult = waterHouseService.getMemberWaterHousePage(pageReqVO);
        return success(MemberWaterHouseConvert.INSTANCE.convertPage(pageResult));
    }
}
