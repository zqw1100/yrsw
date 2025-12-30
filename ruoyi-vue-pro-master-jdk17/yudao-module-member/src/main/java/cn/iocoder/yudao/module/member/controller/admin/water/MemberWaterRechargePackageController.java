package cn.iocoder.yudao.module.member.controller.admin.water;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterRechargePackageCreateReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterRechargePackagePageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterRechargePackageRespVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterRechargePackageUpdateReqVO;
import cn.iocoder.yudao.module.member.convert.water.MemberWaterRechargePackageConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterRechargePackageDO;
import cn.iocoder.yudao.module.member.service.water.MemberWaterRechargePackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 居民报装充值套餐")
@RestController
@RequestMapping("/member/water-recharge-package")
@Validated
public class MemberWaterRechargePackageController {

    @Resource
    private MemberWaterRechargePackageService rechargePackageService;

    @PostMapping("/create")
    @Operation(summary = "创建居民报装充值套餐")
    @PreAuthorize("@ss.hasPermission('member:water-recharge-package:create')")
    public CommonResult<Long> createRechargePackage(@Valid @RequestBody MemberWaterRechargePackageCreateReqVO createReqVO) {
        return success(rechargePackageService.createRechargePackage(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新居民报装充值套餐")
    @PreAuthorize("@ss.hasPermission('member:water-recharge-package:update')")
    public CommonResult<Boolean> updateRechargePackage(@Valid @RequestBody MemberWaterRechargePackageUpdateReqVO updateReqVO) {
        rechargePackageService.updateRechargePackage(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除居民报装充值套餐")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:water-recharge-package:delete')")
    public CommonResult<Boolean> deleteRechargePackage(@RequestParam("id") Long id) {
        rechargePackageService.deleteRechargePackage(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得居民报装充值套餐")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:water-recharge-package:query')")
    public CommonResult<MemberWaterRechargePackageRespVO> getRechargePackage(@RequestParam("id") Long id) {
        MemberWaterRechargePackageDO rechargePackage = rechargePackageService.getRechargePackage(id);
        return success(MemberWaterRechargePackageConvert.INSTANCE.convert(rechargePackage));
    }

    @GetMapping("/page")
    @Operation(summary = "获得居民报装充值套餐分页")
    @PreAuthorize("@ss.hasPermission('member:water-recharge-package:query')")
    public CommonResult<PageResult<MemberWaterRechargePackageRespVO>> getRechargePackagePage(
            @Valid MemberWaterRechargePackagePageReqVO pageReqVO) {
        PageResult<MemberWaterRechargePackageDO> pageResult = rechargePackageService.getRechargePackagePage(pageReqVO);
        return success(MemberWaterRechargePackageConvert.INSTANCE.convertPage(pageResult));
    }
}
