package cn.iocoder.yudao.module.member.service.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFeeBillPageReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFeeBillDO;

/**
 * 水费结算流水 Service
 */
public interface MemberWaterFeeBillService {

    MemberWaterFeeBillDO getFeeBill(Long id);

    PageResult<MemberWaterFeeBillDO> getFeeBillPage(MemberWaterFeeBillPageReqVO pageReqVO);
}
