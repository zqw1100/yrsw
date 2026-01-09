package cn.iocoder.yudao.module.member.controller.app.water;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFeeBillPageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFeeBillRespVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterFeeBillPageReqVO;
import cn.iocoder.yudao.module.member.convert.water.MemberWaterFeeBillConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFeeBillDO;
import cn.iocoder.yudao.module.member.service.water.MemberWaterFeeBillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 水费结算流水")
@RestController
@RequestMapping("/member/water-fee-bill")
@Validated
public class AppWaterFeeBillController {

    @Resource
    private MemberWaterFeeBillService feeBillService;

    @GetMapping("/page")
    @Operation(summary = "获得水费结算流水分页")
    public CommonResult<PageResult<MemberWaterFeeBillRespVO>> getFeeBillPage(
            @Valid AppWaterFeeBillPageReqVO pageReqVO) {
        MemberWaterFeeBillPageReqVO reqVO = BeanUtils.toBean(pageReqVO, MemberWaterFeeBillPageReqVO.class);
        PageResult<MemberWaterFeeBillDO> pageResult = feeBillService.getFeeBillPage(reqVO);
        return success(MemberWaterFeeBillConvert.INSTANCE.convertPage(pageResult));
    }
}
