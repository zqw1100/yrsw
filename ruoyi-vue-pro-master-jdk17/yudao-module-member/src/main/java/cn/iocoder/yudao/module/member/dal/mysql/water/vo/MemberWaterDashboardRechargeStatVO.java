package cn.iocoder.yudao.module.member.dal.mysql.water.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MemberWaterDashboardRechargeStatVO {

    private LocalDate statDate;

    private String statMonth;

    private Integer amount;

    private Integer bonus;
}
