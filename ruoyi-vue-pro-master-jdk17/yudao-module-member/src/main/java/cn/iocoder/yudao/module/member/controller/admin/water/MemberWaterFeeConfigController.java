package cn.iocoder.yudao.module.member.controller.admin.water;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFeeConfigCreateReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFeeConfigPageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFeeConfigRespVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFeeConfigUpdateReqVO;
import cn.iocoder.yudao.module.member.convert.water.MemberWaterFeeConfigConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFeeConfigDO;
import cn.iocoder.yudao.module.member.service.water.MemberWaterFeeConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 水费配置")
@RestController
@RequestMapping("/member/water-fee-config")
@Validated
public class MemberWaterFeeConfigController {

    @Resource
    private MemberWaterFeeConfigService feeConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建水费配置")
    @PreAuthorize("@ss.hasPermission('member:water-fee-config:create')")
    public CommonResult<Long> createFeeConfig(@Valid @RequestBody MemberWaterFeeConfigCreateReqVO createReqVO) {
        return success(feeConfigService.createFeeConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新水费配置")
    @PreAuthorize("@ss.hasPermission('member:water-fee-config:update')")
    public CommonResult<Boolean> updateFeeConfig(@Valid @RequestBody MemberWaterFeeConfigUpdateReqVO updateReqVO) {
        feeConfigService.updateFeeConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除水费配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:water-fee-config:delete')")
    public CommonResult<Boolean> deleteFeeConfig(@RequestParam("id") Long id) {
        feeConfigService.deleteFeeConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得水费配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:water-fee-config:query')")
    public CommonResult<MemberWaterFeeConfigRespVO> getFeeConfig(@RequestParam("id") Long id) {
        MemberWaterFeeConfigDO config = feeConfigService.getFeeConfig(id);
        return success(MemberWaterFeeConfigConvert.INSTANCE.convert(config));
    }

    @GetMapping("/page")
    @Operation(summary = "获得水费配置分页")
    @PreAuthorize("@ss.hasPermission('member:water-fee-config:query')")
    public CommonResult<PageResult<MemberWaterFeeConfigRespVO>> getFeeConfigPage(@Valid MemberWaterFeeConfigPageReqVO pageReqVO) {
        PageResult<MemberWaterFeeConfigDO> pageResult = feeConfigService.getFeeConfigPage(pageReqVO);
        return success(MemberWaterFeeConfigConvert.INSTANCE.convertPage(pageResult));
    }
}
