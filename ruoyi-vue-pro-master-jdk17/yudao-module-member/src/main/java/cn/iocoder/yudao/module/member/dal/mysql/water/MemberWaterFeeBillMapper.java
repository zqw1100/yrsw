package cn.iocoder.yudao.module.member.dal.mysql.water;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterFeeBillDailyStatsRespVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterFeeBillMonthlyStatsRespVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFeeBillPageReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFeeBillDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

/**
 * 水费结算流水 Mapper
 */
@Mapper
public interface MemberWaterFeeBillMapper extends BaseMapperX<MemberWaterFeeBillDO> {

    default PageResult<MemberWaterFeeBillDO> selectPage(MemberWaterFeeBillPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MemberWaterFeeBillDO>()
                .likeIfPresent(MemberWaterFeeBillDO::getDeviceNo, reqVO.getDeviceNo())
                .betweenIfPresent(MemberWaterFeeBillDO::getStatDate, reqVO.getStatDate())
                .orderByDesc(MemberWaterFeeBillDO::getId));
    }

    default MemberWaterFeeBillDO selectLatestByDeviceNo(String deviceNo) {
        return selectOne(new LambdaQueryWrapperX<MemberWaterFeeBillDO>()
                .eq(MemberWaterFeeBillDO::getDeviceNo, deviceNo)
                .orderByDesc(MemberWaterFeeBillDO::getStatDate)
                .orderByDesc(MemberWaterFeeBillDO::getId)
                .last("LIMIT 1"));
    }

    @Select("""
            SELECT stat_date AS statDate,
                   total_usage AS totalUsage,
                   last_total_usage AS lastTotalUsage,
                   usage_diff AS usageDiff,
                   fee
            FROM member_water_fee_bill
            WHERE device_no = #{deviceNo}
              AND stat_date >= #{startDate}
              AND stat_date <= #{endDate}
            ORDER BY stat_date
            """)
    List<AppWaterFeeBillDailyStatsRespVO> selectDailyStats(@Param("deviceNo") String deviceNo,
                                                          @Param("startDate") LocalDate startDate,
                                                          @Param("endDate") LocalDate endDate);

    @Select("""
            SELECT MONTH(stat_date) AS month,
                   SUM(fee) AS fee
            FROM member_water_fee_bill
            WHERE device_no = #{deviceNo}
              AND stat_date >= #{startDate}
              AND stat_date <= #{endDate}
            GROUP BY MONTH(stat_date)
            ORDER BY MONTH(stat_date)
            """)
    List<AppWaterFeeBillMonthlyStatsRespVO> selectMonthlyStats(@Param("deviceNo") String deviceNo,
                                                               @Param("startDate") LocalDate startDate,
                                                               @Param("endDate") LocalDate endDate);
}
