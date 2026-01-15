package cn.iocoder.yudao.module.member.dal.mysql.water.vo;

import lombok.Data;

@Data
public class MemberWaterDashboardRechargeSummaryVO {

    private Integer payAmount;

    private Integer bonusAmount;

    private Long rechargeCount;
}
