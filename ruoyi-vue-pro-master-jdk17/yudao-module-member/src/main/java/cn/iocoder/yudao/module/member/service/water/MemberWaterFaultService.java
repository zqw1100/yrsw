package cn.iocoder.yudao.module.member.service.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterFaultCreateReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterFaultInitRespVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterFaultPageReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterFaultRespVO;

public interface MemberWaterFaultService {

    AppWaterFaultInitRespVO getFaultInit(Long userId);

    Long createFault(Long userId, AppWaterFaultCreateReqVO createReqVO);

    PageResult<AppWaterFaultRespVO> getFaultPage(Long userId, AppWaterFaultPageReqVO pageReqVO);
}
