package cn.iocoder.yudao.module.member.controller.admin.water.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 居民报装申请 Response VO")
@Data
public class MemberWaterApplyRespVO {

    @Schema(description = "编号", example = "1024")
    private Long id;

    @Schema(description = "用户编号", example = "1024")
    private Long userId;

    @Schema(description = "报装房屋编号", example = "2048")
    private Long waterHouseId;

    @Schema(description = "地区编号（区县）", example = "110105")
    private Long areaId;

    @Schema(description = "地区名称", example = "北京市 朝阳区")
    private String areaName;

    @Schema(description = "小区名称", example = "城市花园")
    private String communityName;

    @Schema(description = "小区编号", example = "e10adc3949ba59abbe56e057f20f883e")
    private String communityId;

    @Schema(description = "楼栋名称", example = "1号楼")
    private String buildingName;

    @Schema(description = "单元名称", example = "1单元")
    private String unitName;

    @Schema(description = "房间号", example = "302")
    private String roomNo;

    @Schema(description = "联系人姓名", example = "张三")
    private String contactName;

    @Schema(description = "联系人手机号", example = "13312345678")
    private String contactMobile;

    @Schema(description = "推荐人", example = "A001")
    private String referrer;

    @Schema(description = "申请状态（0 待补充资料 1 已提交）", example = "1")
    private Integer applyStatus;

    @Schema(description = "处理状态", example = "0")
    private Integer processStatus;

    @Schema(description = "设备号（物联网水表唯一编号）", example = "WM10001")
    private String deviceNo;

    @Schema(description = "首次充值套餐编号", example = "1")
    private Long rechargePackageId;

    @Schema(description = "备注", example = "备注")
    private String remark;

    @Schema(description = "户主姓名", example = "李四")
    private String ownerName;

    @Schema(description = "户主身份证号", example = "110101199001019999")
    private String ownerIdCard;

    @Schema(description = "合同图片地址列表")
    private List<String> contractImageUrls;

    @Schema(description = "施工前图片")
    private List<String> beforeImageUrls;

    @Schema(description = "施工前备注")
    private String beforeRemark;

    @Schema(description = "施工后图片")
    private List<String> afterImageUrls;

    @Schema(description = "施工后备注")
    private String afterRemark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
