package cn.iocoder.yudao.module.member.dal.mysql.water.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MemberWaterDashboardUsageFeeStatVO {

    private LocalDate statDate;

    private String statMonth;

    private Long usage;

    private Integer fee;
}
