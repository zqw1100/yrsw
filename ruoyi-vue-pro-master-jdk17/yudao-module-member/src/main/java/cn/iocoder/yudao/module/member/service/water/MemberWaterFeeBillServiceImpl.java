package cn.iocoder.yudao.module.member.service.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterFeeBillDailyStatsRespVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterFeeBillMonthlyStatsRespVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFeeBillPageReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFeeBillDO;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterFeeBillMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;

/**
 * 水费结算流水 Service 实现类
 */
@Service
@Validated
public class MemberWaterFeeBillServiceImpl implements MemberWaterFeeBillService {

    @Resource
    private MemberWaterFeeBillMapper feeBillMapper;

    @Override
    public MemberWaterFeeBillDO getFeeBill(Long id) {
        return feeBillMapper.selectById(id);
    }

    @Override
    public PageResult<MemberWaterFeeBillDO> getFeeBillPage(MemberWaterFeeBillPageReqVO pageReqVO) {
        return feeBillMapper.selectPage(pageReqVO, new LambdaQueryWrapperX<MemberWaterFeeBillDO>()
                .likeIfPresent(MemberWaterFeeBillDO::getDeviceNo, pageReqVO.getDeviceNo())
                .eqIfPresent(MemberWaterFeeBillDO::getCommunityId, pageReqVO.getCommunityId())
                .betweenIfPresent(MemberWaterFeeBillDO::getStatDate, pageReqVO.getStatDate())
                .orderByDesc(MemberWaterFeeBillDO::getId));
    }

    @Override
    public List<AppWaterFeeBillDailyStatsRespVO> getDailyStats(String deviceNo, String month) {
        YearMonth yearMonth = YearMonth.parse(month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        return feeBillMapper.selectDailyStats(deviceNo, startDate, endDate);
    }

    @Override
    public List<AppWaterFeeBillMonthlyStatsRespVO> getMonthlyStats(String deviceNo, String year) {
        Year parsedYear = Year.parse(year);
        LocalDate startDate = parsedYear.atDay(1);
        LocalDate endDate = parsedYear.atMonth(12).atEndOfMonth();
        return feeBillMapper.selectMonthlyStats(deviceNo, startDate, endDate);
    }
}
