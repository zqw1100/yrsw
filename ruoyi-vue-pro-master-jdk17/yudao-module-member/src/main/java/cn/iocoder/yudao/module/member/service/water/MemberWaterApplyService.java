package cn.iocoder.yudao.module.member.service.water;

import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterApplyCompleteReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterApplyCreateReqVO;

/**
 * 居民报装申请 Service
 */
public interface MemberWaterApplyService {

    /**
     * 创建居民报装申请
     *
     * @param createReqVO 创建信息
     * @return 申请编号
     */
    Long createApply(AppWaterApplyCreateReqVO createReqVO);

    /**
     * 补充居民报装资料
     *
     * @param completeReqVO 补充资料
     */
    void completeApply(AppWaterApplyCompleteReqVO completeReqVO);
}
