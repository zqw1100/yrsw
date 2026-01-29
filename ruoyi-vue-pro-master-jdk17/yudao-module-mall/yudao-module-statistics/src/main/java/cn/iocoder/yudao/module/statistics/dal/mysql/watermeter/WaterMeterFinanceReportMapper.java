package cn.iocoder.yudao.module.statistics.dal.mysql.watermeter;

import cn.iocoder.yudao.module.statistics.controller.admin.watermeter.vo.WaterMeterFinanceDailyRespVO;
import cn.iocoder.yudao.module.statistics.controller.admin.watermeter.vo.WaterMeterFinanceMonthlyRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface WaterMeterFinanceReportMapper {

    List<WaterMeterFinanceMonthlyRespVO> selectMonthlyReport(@Param("year") Integer year,
                                                             @Param("city") String city,
                                                             @Param("county") String county,
                                                             @Param("community") String community);

    List<WaterMeterFinanceDailyRespVO> selectDailyReport(@Param("year") Integer year,
                                                         @Param("city") String city,
                                                         @Param("county") String county,
                                                         @Param("community") String community,
                                                         @Param("startDate") LocalDate startDate,
                                                         @Param("endDate") LocalDate endDate);
}
