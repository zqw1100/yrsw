package cn.iocoder.yudao.module.member.controller.admin.water;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFeeBillPageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFeeBillRespVO;
import cn.iocoder.yudao.module.member.convert.water.MemberWaterFeeBillConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFeeBillDO;
import cn.iocoder.yudao.module.member.service.water.MemberWaterFeeBillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 水费结算流水")
@RestController
@RequestMapping("/member/water-fee-bill")
@Validated
public class MemberWaterFeeBillController {

    @Resource
    private MemberWaterFeeBillService feeBillService;

    @GetMapping("/get")
    @Operation(summary = "获得水费结算流水")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:water-fee-bill:query')")
    public CommonResult<MemberWaterFeeBillRespVO> getFeeBill(@RequestParam("id") Long id) {
        MemberWaterFeeBillDO bill = feeBillService.getFeeBill(id);
        return success(MemberWaterFeeBillConvert.INSTANCE.convert(bill));
    }

    @GetMapping("/page")
    @Operation(summary = "获得水费结算流水分页")
    @PreAuthorize("@ss.hasPermission('member:water-fee-bill:query')")
    public CommonResult<PageResult<MemberWaterFeeBillRespVO>> getFeeBillPage(@Valid MemberWaterFeeBillPageReqVO pageReqVO) {
        PageResult<MemberWaterFeeBillDO> pageResult = feeBillService.getFeeBillPage(pageReqVO);
        return success(MemberWaterFeeBillConvert.INSTANCE.convertPage(pageResult));
    }
}
