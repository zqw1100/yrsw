package cn.iocoder.yudao.module.member.service.water;

import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFaultPageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFaultRespVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFaultStatusUpdateReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterFaultCreateReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterFaultInitRespVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterFaultPageReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterFaultRespVO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

public interface MemberWaterFaultService {

    AppWaterFaultInitRespVO getFaultInit(Long userId, String deviceNo);

    Long createFault(Long userId, AppWaterFaultCreateReqVO createReqVO);

    PageResult<AppWaterFaultRespVO> getFaultPage(Long userId, AppWaterFaultPageReqVO pageReqVO);

    PageResult<MemberWaterFaultRespVO> getFaultPage(MemberWaterFaultPageReqVO pageReqVO);

    void updateFaultStatus(MemberWaterFaultStatusUpdateReqVO updateReqVO);
}
