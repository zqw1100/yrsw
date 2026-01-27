package cn.iocoder.yudao.module.member.service.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterWorkOrderAcceptReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterWorkOrderAssignReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterWorkOrderFinishReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterWorkOrderPageReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterWorkOrderRespVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterWorkOrderRevokeReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterWorkOrderStartReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterApplyDO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFaultDO;

public interface MemberWaterWorkOrderService {

    PageResult<AppWaterWorkOrderRespVO> getWorkOrderPage(Long userId, AppWaterWorkOrderPageReqVO pageReqVO);

    AppWaterWorkOrderRespVO getWorkOrder(Long userId, Long id);

    void assignWorkOrder(Long userId, AppWaterWorkOrderAssignReqVO reqVO);

    void revokeWorkOrder(Long userId, AppWaterWorkOrderRevokeReqVO reqVO);

    void acceptWorkOrder(Long userId, AppWaterWorkOrderAcceptReqVO reqVO);

    void startWorkOrder(Long userId, AppWaterWorkOrderStartReqVO reqVO);

    void finishWorkOrder(Long userId, AppWaterWorkOrderFinishReqVO reqVO);

    void createForApply(MemberWaterApplyDO apply);

    void createForFault(MemberWaterFaultDO fault);

    void updateForFault(Long id, Integer processStatus);

    void updateForApply(Long id, Integer processStatus);
}
