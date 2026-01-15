package cn.iocoder.yudao.module.member.dal.mysql.water;

import cn.iocoder.yudao.module.member.dal.mysql.water.vo.MemberWaterDashboardPieStatVO;
import cn.iocoder.yudao.module.member.dal.mysql.water.vo.MemberWaterDashboardRechargeStatVO;
import cn.iocoder.yudao.module.member.dal.mysql.water.vo.MemberWaterDashboardRechargeSummaryVO;
import cn.iocoder.yudao.module.member.dal.mysql.water.vo.MemberWaterDashboardUsageFeeStatVO;
import cn.iocoder.yudao.module.member.dal.mysql.water.vo.MemberWaterDashboardUsageFeeSummaryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MemberWaterDashboardMapper {

    @Select("""
            SELECT COALESCE(SUM(usage_diff), 0) AS usageAmount,
                   COALESCE(SUM(fee), 0) AS fee
            FROM member_water_fee_bill
            """)
    MemberWaterDashboardUsageFeeSummaryVO selectUsageFeeSummary();

    @Select("""
            SELECT stat_date AS statDate,
                   SUM(usage_diff) AS usageAmount,
                   SUM(fee) AS fee
            FROM member_water_fee_bill
            WHERE stat_date >= #{startDate}
              AND stat_date <= #{endDate}
            GROUP BY stat_date
            ORDER BY stat_date
            """)
    List<MemberWaterDashboardUsageFeeStatVO> selectDailyUsageFee(@Param("startDate") LocalDate startDate,
                                                                 @Param("endDate") LocalDate endDate);

    @Select("""
            SELECT DATE_FORMAT(stat_date, '%Y-%m') AS statMonth,
                   SUM(usage_diff) AS usageAmount,
                   SUM(fee) AS fee
            FROM member_water_fee_bill
            WHERE stat_date >= #{startDate}
              AND stat_date <= #{endDate}
            GROUP BY DATE_FORMAT(stat_date, '%Y-%m')
            ORDER BY statMonth
            """)
    List<MemberWaterDashboardUsageFeeStatVO> selectMonthlyUsageFee(@Param("startDate") LocalDate startDate,
                                                                   @Param("endDate") LocalDate endDate);

    @Select("""
            SELECT COALESCE(SUM(pay_price), 0) AS payAmount,
                   COALESCE(SUM(bonus_price), 0) AS bonusAmount,
                   COUNT(1) AS rechargeCount
            FROM pay_wallet_recharge
            WHERE pay_status = 1
            """)
    MemberWaterDashboardRechargeSummaryVO selectRechargeSummary();

    @Select("""
            SELECT DATE(pay_time) AS statDate,
                   SUM(pay_price) AS amount,
                   SUM(bonus_price) AS bonus
            FROM pay_wallet_recharge
            WHERE pay_status = 1
              AND pay_time >= #{startTime}
              AND pay_time < #{endTime}
            GROUP BY DATE(pay_time)
            ORDER BY statDate
            """)
    List<MemberWaterDashboardRechargeStatVO> selectDailyRecharge(@Param("startTime") LocalDateTime startTime,
                                                                 @Param("endTime") LocalDateTime endTime);

    @Select("""
            SELECT DATE_FORMAT(pay_time, '%Y-%m') AS statMonth,
                   SUM(pay_price) AS amount,
                   SUM(bonus_price) AS bonus
            FROM pay_wallet_recharge
            WHERE pay_status = 1
              AND pay_time >= #{startTime}
              AND pay_time < #{endTime}
            GROUP BY DATE_FORMAT(pay_time, '%Y-%m')
            ORDER BY statMonth
            """)
    List<MemberWaterDashboardRechargeStatVO> selectMonthlyRecharge(@Param("startTime") LocalDateTime startTime,
                                                                   @Param("endTime") LocalDateTime endTime);

    @Select("""
            SELECT CASE
                     WHEN valve_status = 1 THEN '开阀'
                     WHEN valve_status = 0 THEN '关阀'
                     ELSE '未知'
                   END AS name,
                   COUNT(1) AS value
            FROM member_water_device
            GROUP BY valve_status
            ORDER BY value DESC
            """)
    List<MemberWaterDashboardPieStatVO> selectValveStatusStats();
}
