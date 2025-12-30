package cn.iocoder.yudao.module.member.controller.app.water;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterRechargePackageRespVO;
import cn.iocoder.yudao.module.member.convert.water.MemberWaterRechargePackageConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterRechargePackageDO;
import cn.iocoder.yudao.module.member.service.water.MemberWaterRechargePackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 居民报装充值套餐")
@RestController
@RequestMapping("/member/water-recharge-package")
public class AppWaterRechargePackageController {

    @Resource
    private MemberWaterRechargePackageService rechargePackageService;

    @GetMapping("/list")
    @Operation(summary = "获得居民报装充值套餐列表")
    public CommonResult<List<AppWaterRechargePackageRespVO>> getRechargePackageList() {
        List<MemberWaterRechargePackageDO> list = rechargePackageService.getEnableRechargePackageList();
        return success(MemberWaterRechargePackageConvert.INSTANCE.convertAppList(list));
    }
}
