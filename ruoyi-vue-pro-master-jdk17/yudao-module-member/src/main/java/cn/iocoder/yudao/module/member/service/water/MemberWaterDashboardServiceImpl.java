package cn.iocoder.yudao.module.member.service.water;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.util.number.MoneyUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDashboardRespVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterApplyDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterDeviceDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFaultDO;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterApplyMapper;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterDashboardMapper;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterDeviceMapper;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterFaultMapper;
import cn.iocoder.yudao.module.member.dal.mysql.water.vo.MemberWaterDashboardRechargeStatVO;
import cn.iocoder.yudao.module.member.dal.mysql.water.vo.MemberWaterDashboardRechargeSummaryVO;
import cn.iocoder.yudao.module.member.dal.mysql.water.vo.MemberWaterDashboardUsageFeeStatVO;
import cn.iocoder.yudao.module.member.dal.mysql.water.vo.MemberWaterDashboardUsageFeeSummaryVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemberWaterDashboardServiceImpl implements MemberWaterDashboardService {

    private static final long LOW_BALANCE_THRESHOLD_FEN = 1000L;

    @Resource
    private MemberWaterDashboardMapper dashboardMapper;
    @Resource
    private MemberWaterDeviceMapper deviceMapper;
    @Resource
    private MemberWaterApplyMapper applyMapper;
    @Resource
    private MemberWaterFaultMapper faultMapper;

    @Override
    public MemberWaterDashboardRespVO getDashboard() {
        MemberWaterDashboardRespVO respVO = new MemberWaterDashboardRespVO();
        respVO.setSummary(buildSummary());
        respVO.setDailyTrend(buildDailyTrend());
        respVO.setMonthlyTrend(buildMonthlyTrend());
        respVO.setValveStatusStats(buildValveStatusStats());
        return respVO;
    }

    private MemberWaterDashboardRespVO.Summary buildSummary() {
        MemberWaterDashboardUsageFeeSummaryVO usageFeeSummary = ObjectUtil.defaultIfNull(
                dashboardMapper.selectUsageFeeSummary(), new MemberWaterDashboardUsageFeeSummaryVO());
        MemberWaterDashboardRechargeSummaryVO rechargeSummary = ObjectUtil.defaultIfNull(
                dashboardMapper.selectRechargeSummary(), new MemberWaterDashboardRechargeSummaryVO());
        MemberWaterDashboardRespVO.Summary summary = new MemberWaterDashboardRespVO.Summary();
        summary.setTotalUsage(ObjectUtil.defaultIfNull(usageFeeSummary.getUsageAmount(), 0L));
        summary.setTotalFeeAmount(MoneyUtils.fenToYuan(ObjectUtil.defaultIfNull(usageFeeSummary.getFee(), 0)));
        summary.setTotalRechargeAmount(MoneyUtils.fenToYuan(ObjectUtil.defaultIfNull(rechargeSummary.getPayAmount(), 0)));
        summary.setTotalRechargeBonusAmount(MoneyUtils.fenToYuan(ObjectUtil.defaultIfNull(rechargeSummary.getBonusAmount(), 0)));
        summary.setRechargeCount(ObjectUtil.defaultIfNull(rechargeSummary.getRechargeCount(), 0L));
        summary.setDeviceCount(deviceMapper.selectCount(null));
        summary.setLowBalanceDeviceCount(deviceMapper.selectCount(new LambdaQueryWrapperX<MemberWaterDeviceDO>()
                .lt(MemberWaterDeviceDO::getDeviceBalance, LOW_BALANCE_THRESHOLD_FEN)));
        summary.setPendingApplyCount(applyMapper.selectCount(new LambdaQueryWrapperX<MemberWaterApplyDO>()
                .eq(MemberWaterApplyDO::getProcessStatus, 0)));
        summary.setPendingFaultCount(faultMapper.selectCount(new LambdaQueryWrapperX<MemberWaterFaultDO>()
                .eq(MemberWaterFaultDO::getProcessStatus, 0)));
        return summary;
    }

    private List<MemberWaterDashboardRespVO.Trend> buildDailyTrend() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);
        LocalDateTime startTime = startDate.atStartOfDay();
        LocalDateTime endTime = endDate.plusDays(1).atStartOfDay();
        Map<LocalDate, MemberWaterDashboardUsageFeeStatVO> usageFeeMap = new HashMap<>();
        for (MemberWaterDashboardUsageFeeStatVO stat : dashboardMapper.selectDailyUsageFee(startDate, endDate)) {
            usageFeeMap.put(stat.getStatDate(), stat);
        }
        Map<LocalDate, MemberWaterDashboardRechargeStatVO> rechargeMap = new HashMap<>();
        for (MemberWaterDashboardRechargeStatVO stat : dashboardMapper.selectDailyRecharge(startTime, endTime)) {
            rechargeMap.put(stat.getStatDate(), stat);
        }
        List<MemberWaterDashboardRespVO.Trend> trends = new ArrayList<>();
        LocalDate cursor = startDate;
        while (!cursor.isAfter(endDate)) {
            MemberWaterDashboardUsageFeeStatVO usageFee = usageFeeMap.get(cursor);
            MemberWaterDashboardRechargeStatVO recharge = rechargeMap.get(cursor);
            trends.add(buildTrend(cursor.toString(), usageFee, recharge));
            cursor = cursor.plusDays(1);
        }
        return trends;
    }

    private List<MemberWaterDashboardRespVO.Trend> buildMonthlyTrend() {
        YearMonth endMonth = YearMonth.now();
        YearMonth startMonth = endMonth.minusMonths(5);
        LocalDate startDate = startMonth.atDay(1);
        LocalDate endDate = endMonth.atEndOfMonth();
        LocalDateTime startTime = startDate.atStartOfDay();
        LocalDateTime endTime = endMonth.plusMonths(1).atDay(1).atStartOfDay();
        Map<String, MemberWaterDashboardUsageFeeStatVO> usageFeeMap = new HashMap<>();
        for (MemberWaterDashboardUsageFeeStatVO stat : dashboardMapper.selectMonthlyUsageFee(startDate, endDate)) {
            usageFeeMap.put(stat.getStatMonth(), stat);
        }
        Map<String, MemberWaterDashboardRechargeStatVO> rechargeMap = new HashMap<>();
        for (MemberWaterDashboardRechargeStatVO stat : dashboardMapper.selectMonthlyRecharge(startTime, endTime)) {
            rechargeMap.put(stat.getStatMonth(), stat);
        }
        List<MemberWaterDashboardRespVO.Trend> trends = new ArrayList<>();
        YearMonth cursor = startMonth;
        while (!cursor.isAfter(endMonth)) {
            String monthKey = cursor.toString();
            trends.add(buildTrend(monthKey, usageFeeMap.get(monthKey), rechargeMap.get(monthKey)));
            cursor = cursor.plusMonths(1);
        }
        return trends;
    }

    private List<MemberWaterDashboardRespVO.PieItem> buildValveStatusStats() {
        List<MemberWaterDashboardPieStatVO> stats = dashboardMapper.selectValveStatusStats();
        List<MemberWaterDashboardRespVO.PieItem> result = new ArrayList<>();
        for (MemberWaterDashboardPieStatVO stat : stats) {
            MemberWaterDashboardRespVO.PieItem item = new MemberWaterDashboardRespVO.PieItem();
            item.setName(stat.getName());
            item.setValue(ObjectUtil.defaultIfNull(stat.getValue(), 0L));
            result.add(item);
        }
        return result;
    }

    private MemberWaterDashboardRespVO.Trend buildTrend(String time,
                                                      MemberWaterDashboardUsageFeeStatVO usageFee,
                                                      MemberWaterDashboardRechargeStatVO recharge) {
        MemberWaterDashboardRespVO.Trend trend = new MemberWaterDashboardRespVO.Trend();
        trend.setTime(time);
        trend.setUsage(usageFee != null ? ObjectUtil.defaultIfNull(usageFee.getUsageAmount(), 0L) : 0L);
        trend.setFeeAmount(toYuan(usageFee != null ? usageFee.getFee() : null));
        trend.setRechargeAmount(toYuan(recharge != null ? recharge.getAmount() : null));
        return trend;
    }

    private BigDecimal toYuan(Integer fen) {
        return MoneyUtils.fenToYuan(ObjectUtil.defaultIfNull(fen, 0));
    }
}
