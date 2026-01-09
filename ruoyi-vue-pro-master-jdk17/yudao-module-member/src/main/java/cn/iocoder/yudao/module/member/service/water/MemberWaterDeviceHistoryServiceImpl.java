package cn.iocoder.yudao.module.member.service.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDeviceHistoryPageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterDeviceHistoryRespVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterDeviceHistoryDO;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterDeviceHistoryMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 居民设备用水历史 Service 实现类
 */
@Service
@Validated
public class MemberWaterDeviceHistoryServiceImpl implements MemberWaterDeviceHistoryService {

    @Resource
    private MemberWaterDeviceHistoryMapper historyMapper;

    @Override
    public PageResult<MemberWaterDeviceHistoryRespVO> getHistoryPage(MemberWaterDeviceHistoryPageReqVO pageReqVO) {
        PageResult<MemberWaterDeviceHistoryDO> pageResult = historyMapper.selectPage(pageReqVO);
        return BeanUtils.toBean(pageResult, MemberWaterDeviceHistoryRespVO.class);
    }
}
