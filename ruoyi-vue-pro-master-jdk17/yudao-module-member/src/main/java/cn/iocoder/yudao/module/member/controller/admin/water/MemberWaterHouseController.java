package cn.iocoder.yudao.module.member.controller.admin.water;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterHouseCreateReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterCommunityOptionRespVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterHouseImportExcelVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterHouseImportRespVO;
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
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    @GetMapping("/community-options")
    @Operation(summary = "获得小区下拉选项")
    @PreAuthorize("@ss.hasPermission('member:water-house:query')")
    public CommonResult<List<MemberWaterCommunityOptionRespVO>> getCommunityOptions(
            @RequestParam(value = "areaId", required = false) Long areaId) {
        return success(waterHouseService.getCommunityOptions(areaId));
    }

    @GetMapping("/get-import-template")
    @Operation(summary = "获得居民报装房屋信息导入模板")
    public void importTemplate(HttpServletResponse response) throws IOException {
        List<MemberWaterHouseImportExcelVO> list = waterHouseService.getImportTemplate();
        ExcelUtils.write(response, "居民报装房屋导入模板.xls", "房屋信息", MemberWaterHouseImportExcelVO.class, list);
    }

    @PostMapping("/import")
    @Operation(summary = "导入居民报装房屋信息")
    @PreAuthorize("@ss.hasPermission('member:water-house:import')")
    public CommonResult<MemberWaterHouseImportRespVO> importExcel(@RequestParam("file") MultipartFile file,
                                                                  @RequestParam(value = "updateSupport", required = false, defaultValue = "false") Boolean updateSupport) throws Exception {
        List<MemberWaterHouseImportExcelVO> list = ExcelUtils.read(file, MemberWaterHouseImportExcelVO.class);
        return success(waterHouseService.importWaterHouseList(list, updateSupport));
    }
}
