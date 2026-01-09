package cn.iocoder.yudao.module.member.service.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFeeBillPageReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFeeBillDO;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterFeeBillMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;

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
        return feeBillMapper.selectPage(pageReqVO);
    }
}
