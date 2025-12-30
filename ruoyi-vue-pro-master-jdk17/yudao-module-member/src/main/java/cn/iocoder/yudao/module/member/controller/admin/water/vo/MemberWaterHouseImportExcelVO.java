package cn.iocoder.yudao.module.member.controller.admin.water.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 居民报装房屋信息 Excel 导入 VO
 */
@Data
@ExcelIgnoreUnannotated
public class MemberWaterHouseImportExcelVO {

    @ExcelProperty("地区编号")
    private Long areaId;

    @ExcelProperty("地区名称")
    private String areaName;

    @ExcelProperty("小区名称")
    private String communityName;

    @ExcelProperty("楼栋名称")
    private String buildingName;

    @ExcelProperty("单元名称")
    private String unitName;

    @ExcelProperty("房间号")
    private String roomNo;

    @ExcelProperty("报装状态(0未报装/1已报装)")
    private Integer installStatus;

    @ExcelProperty("排序")
    private Integer sort;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("描述")
    private String description;
}
