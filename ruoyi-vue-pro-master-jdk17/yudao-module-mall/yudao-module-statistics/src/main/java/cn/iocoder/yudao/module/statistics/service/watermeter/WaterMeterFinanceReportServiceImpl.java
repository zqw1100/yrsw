package cn.iocoder.yudao.module.statistics.service.watermeter;

import cn.iocoder.yudao.module.statistics.controller.admin.watermeter.vo.WaterMeterFinanceDailyRespVO;
import cn.iocoder.yudao.module.statistics.controller.admin.watermeter.vo.WaterMeterFinanceMonthlyRespVO;
import cn.iocoder.yudao.module.statistics.controller.admin.watermeter.vo.WaterMeterFinanceReportReqVO;
import cn.iocoder.yudao.module.statistics.dal.dataobject.watermeter.WaterMeterFeeConfigDO;
import cn.iocoder.yudao.module.statistics.dal.mysql.watermeter.WaterMeterFeeConfigMapper;
import cn.iocoder.yudao.module.statistics.dal.mysql.watermeter.WaterMeterFinanceReportMapper;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class WaterMeterFinanceReportServiceImpl implements WaterMeterFinanceReportService {

    private static final BigDecimal DEFAULT_FEE_RATE = new BigDecimal("0.006");

    @Resource
    private WaterMeterFinanceReportMapper waterMeterFinanceReportMapper;

    @Resource
    private WaterMeterFeeConfigMapper waterMeterFeeConfigMapper;

    @Override
    public List<WaterMeterFinanceMonthlyRespVO> getMonthlyReport(WaterMeterFinanceReportReqVO reqVO) {
        List<WaterMeterFinanceMonthlyRespVO> list = waterMeterFinanceReportMapper.selectMonthlyReport(
                reqVO.getYear(), reqVO.getCity(), reqVO.getCounty(), reqVO.getCommunity());
        applyWechatFee(list, getFeeRate());
        return list;
    }

    @Override
    public List<WaterMeterFinanceDailyRespVO> getDailyReport(WaterMeterFinanceReportReqVO reqVO) {
        LocalDate startDate = null;
        LocalDate endDate = null;
        if (reqVO.getDateRange() != null && reqVO.getDateRange().length == 2) {
            startDate = reqVO.getDateRange()[0];
            endDate = reqVO.getDateRange()[1];
        }
        List<WaterMeterFinanceDailyRespVO> list = waterMeterFinanceReportMapper.selectDailyReport(
                reqVO.getYear(), reqVO.getCity(), reqVO.getCounty(), reqVO.getCommunity(), startDate, endDate);
        applyWechatFee(list, getFeeRate());
        return list;
    }

    private BigDecimal getFeeRate() {
        WaterMeterFeeConfigDO config = waterMeterFeeConfigMapper.selectLatest();
        return config != null && config.getFeeRate() != null ? config.getFeeRate() : DEFAULT_FEE_RATE;
    }

    private void applyWechatFee(List<WaterMeterFinanceMonthlyRespVO> list, BigDecimal feeRate) {
        list.forEach(item -> {
            BigDecimal amount = BigDecimal.valueOf(item.getWechatAmount() == null ? 0.0 : item.getWechatAmount());
            item.setWechatFee(calculateFee(amount, feeRate));
        });
    }

    private void applyWechatFee(List<WaterMeterFinanceDailyRespVO> list, BigDecimal feeRate) {
        list.forEach(item -> {
            BigDecimal amount = BigDecimal.valueOf(item.getWechatAmount() == null ? 0.0 : item.getWechatAmount());
            item.setWechatFee(calculateFee(amount, feeRate));
        });
    }

    private double calculateFee(BigDecimal amount, BigDecimal rate) {
        return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
