package cn.iocoder.yudao.module.member.service.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFeeConfigCreateReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFeeConfigPageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFeeConfigUpdateReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFeeConfigDO;

/**
 * 水费配置 Service
 */
public interface MemberWaterFeeConfigService {

    Long createFeeConfig(MemberWaterFeeConfigCreateReqVO createReqVO);

    void updateFeeConfig(MemberWaterFeeConfigUpdateReqVO updateReqVO);

    void deleteFeeConfig(Long id);

    MemberWaterFeeConfigDO getFeeConfig(Long id);

    PageResult<MemberWaterFeeConfigDO> getFeeConfigPage(MemberWaterFeeConfigPageReqVO pageReqVO);
}
