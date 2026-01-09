package cn.iocoder.yudao.module.member.service.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFeeConfigCreateReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFeeConfigPageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFeeConfigUpdateReqVO;
import cn.iocoder.yudao.module.member.convert.water.MemberWaterFeeConfigConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFeeConfigDO;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterFeeConfigMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.WATER_FEE_CONFIG_NOT_EXISTS;

/**
 * 水费配置 Service 实现类
 */
@Service
@Validated
public class MemberWaterFeeConfigServiceImpl implements MemberWaterFeeConfigService {

    @Resource
    private MemberWaterFeeConfigMapper feeConfigMapper;

    @Override
    public Long createFeeConfig(MemberWaterFeeConfigCreateReqVO createReqVO) {
        MemberWaterFeeConfigDO config = MemberWaterFeeConfigConvert.INSTANCE.convert(createReqVO);
        fillDefaultValues(config);
        feeConfigMapper.insert(config);
        return config.getId();
    }

    @Override
    public void updateFeeConfig(MemberWaterFeeConfigUpdateReqVO updateReqVO) {
        validateFeeConfigExists(updateReqVO.getId());
        MemberWaterFeeConfigDO updateObj = MemberWaterFeeConfigConvert.INSTANCE.convert(updateReqVO);
        fillDefaultValues(updateObj);
        feeConfigMapper.updateById(updateObj);
    }

    @Override
    public void deleteFeeConfig(Long id) {
        validateFeeConfigExists(id);
        feeConfigMapper.deleteById(id);
    }

    @Override
    public MemberWaterFeeConfigDO getFeeConfig(Long id) {
        return feeConfigMapper.selectById(id);
    }

    @Override
    public PageResult<MemberWaterFeeConfigDO> getFeeConfigPage(MemberWaterFeeConfigPageReqVO pageReqVO) {
        return feeConfigMapper.selectPage(pageReqVO);
    }

    private void validateFeeConfigExists(Long id) {
        if (feeConfigMapper.selectById(id) == null) {
            throw exception(WATER_FEE_CONFIG_NOT_EXISTS);
        }
    }

    private void fillDefaultValues(MemberWaterFeeConfigDO config) {
        if (config.getRemark() == null) {
            config.setRemark("");
        }
    }
}
