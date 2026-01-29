package cn.iocoder.yudao.module.statistics.service.watermeter;

import cn.iocoder.yudao.module.statistics.controller.admin.watermeter.vo.WaterMeterFinanceDailyRespVO;
import cn.iocoder.yudao.module.statistics.controller.admin.watermeter.vo.WaterMeterFinanceMonthlyRespVO;
import cn.iocoder.yudao.module.statistics.controller.admin.watermeter.vo.WaterMeterFinanceReportReqVO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WaterMeterFinanceReportServiceImpl implements WaterMeterFinanceReportService {

    private static final double WECHAT_FEE_RATE = 0.006;

    @Override
    public List<WaterMeterFinanceMonthlyRespVO> getMonthlyReport(WaterMeterFinanceReportReqVO reqVO) {
        return buildMonthlyData().stream()
                .filter(row -> matchCommonFilters(row.getCity(), row.getCounty(), row.getCommunity(), row.getYear(), reqVO))
                .collect(Collectors.toList());
    }

    @Override
    public List<WaterMeterFinanceDailyRespVO> getDailyReport(WaterMeterFinanceReportReqVO reqVO) {
        return buildDailyData().stream()
                .filter(row -> matchCommonFilters(row.getCity(), row.getCounty(), row.getCommunity(), row.getYear(), reqVO))
                .filter(row -> matchDateRange(row.getDate(), reqVO.getDateRange()))
                .collect(Collectors.toList());
    }

    private boolean matchCommonFilters(String city, String county, String community, Integer year,
                                       WaterMeterFinanceReportReqVO reqVO) {
        if (reqVO == null) {
            return true;
        }
        if (reqVO.getYear() != null && !Objects.equals(year, reqVO.getYear())) {
            return false;
        }
        if (reqVO.getCity() != null && !city.contains(reqVO.getCity())) {
            return false;
        }
        if (reqVO.getCounty() != null && !county.contains(reqVO.getCounty())) {
            return false;
        }
        if (reqVO.getCommunity() != null && !community.contains(reqVO.getCommunity())) {
            return false;
        }
        return true;
    }

    private boolean matchDateRange(LocalDate date, LocalDate[] dateRange) {
        if (dateRange == null || dateRange.length != 2 || date == null) {
            return true;
        }
        LocalDate start = dateRange[0];
        LocalDate end = dateRange[1];
        if (start == null || end == null) {
            return true;
        }
        return !date.isBefore(start) && !date.isAfter(end);
    }

    private List<WaterMeterFinanceMonthlyRespVO> buildMonthlyData() {
        return List.of(
                buildMonthlyRow(2025, "10", 6, 22, 11410, 4154, 11348.39, 22, 11410, 0),
                buildMonthlyRow(2025, "11", 17, 74, 39210, 12261, 38998.27, 74, 39210, 0),
                buildMonthlyRow(2025, "12", 7, 17, 8200, 2119, 8155.72, 17, 8200, 0),
                buildMonthlyRow(2026, "01", 6, 57, 32400, 9452.5, 32223.42, 57, 32400, 0)
        );
    }

    private WaterMeterFinanceMonthlyRespVO buildMonthlyRow(Integer year, String month, Integer billingDays, Integer orderCount,
                                                           double orderAmount, double deliveryAmount, double paidAmount,
                                                           Integer wechatCount, double wechatAmount, double refundAmount) {
        WaterMeterFinanceMonthlyRespVO row = new WaterMeterFinanceMonthlyRespVO();
        row.setCity("烟台市");
        row.setCounty("山东省/烟台市/芝罘区");
        row.setCommunity("逸城山色");
        row.setYear(year);
        row.setMonth(month);
        row.setBillingDays(billingDays);
        row.setOrderCount(orderCount);
        row.setOrderAmount(orderAmount);
        row.setDeliveryAmount(deliveryAmount);
        row.setPaidAmount(paidAmount);
        row.setWechatCount(wechatCount);
        row.setWechatAmount(wechatAmount);
        row.setWechatFee(roundTwoDecimals(wechatAmount * WECHAT_FEE_RATE));
        row.setRefundAmount(refundAmount);
        return row;
    }

    private List<WaterMeterFinanceDailyRespVO> buildDailyData() {
        return List.of(
                buildDailyRow(LocalDate.of(2026, 1, 27), 6, 3200, 942, 3182.72, 6, 3200, 0),
                buildDailyRow(LocalDate.of(2026, 1, 26), 4, 3000, 1238.5, 2983.8, 4, 3000, 0),
                buildDailyRow(LocalDate.of(2026, 1, 25), 23, 13000, 3661, 12928.18, 23, 13000, 0),
                buildDailyRow(LocalDate.of(2026, 1, 24), 22, 11900, 3179, 11835.74, 22, 11900, 0),
                buildDailyRow(LocalDate.of(2026, 1, 22), 1, 1000, 400, 994.6, 1, 1000, 0),
                buildDailyRow(LocalDate.of(2026, 1, 19), 1, 300, 32, 298.38, 1, 300, 0),
                buildDailyRow(LocalDate.of(2025, 12, 23), 1, 300, 32, 298.38, 1, 300, 0),
                buildDailyRow(LocalDate.of(2025, 12, 21), 4, 1900, 503, 1889.74, 4, 1900, 0)
        );
    }

    private WaterMeterFinanceDailyRespVO buildDailyRow(LocalDate date, Integer orderCount, double orderAmount,
                                                       double deliveryAmount, double paidAmount, Integer wechatCount,
                                                       double wechatAmount, double refundAmount) {
        WaterMeterFinanceDailyRespVO row = new WaterMeterFinanceDailyRespVO();
        row.setCity("烟台市");
        row.setCounty("山东省/烟台市/芝罘区");
        row.setCommunity("逸城山色");
        row.setYear(date.getYear());
        row.setMonth(String.format("%02d", date.getMonthValue()));
        row.setDate(date);
        row.setOrderCount(orderCount);
        row.setOrderAmount(orderAmount);
        row.setDeliveryAmount(deliveryAmount);
        row.setPaidAmount(paidAmount);
        row.setWechatCount(wechatCount);
        row.setWechatAmount(wechatAmount);
        row.setWechatFee(roundTwoDecimals(wechatAmount * WECHAT_FEE_RATE));
        row.setRefundAmount(refundAmount);
        return row;
    }

    private double roundTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
