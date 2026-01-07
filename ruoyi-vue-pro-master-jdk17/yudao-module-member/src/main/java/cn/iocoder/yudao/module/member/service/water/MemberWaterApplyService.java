package cn.iocoder.yudao.module.member.service.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterApplyPageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterApplyRespVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterApplyStatusUpdateReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterApplyCompleteReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterApplyCreateReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterApplyPageReqVO;

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

    /**
     * 获得居民报装申请分页
     *
     * @param pageReqVO 分页查询
     * @return 分页结果
     */
    PageResult<MemberWaterApplyRespVO> getApplyPage(MemberWaterApplyPageReqVO pageReqVO);

    /**
     * 获得用户居民报装申请分页
     *
     * @param userId 用户编号
     * @param pageReqVO 分页查询
     * @return 分页结果
     */
    PageResult<MemberWaterApplyRespVO> getApplyPage(Long userId, AppWaterApplyPageReqVO pageReqVO);

    /**
     * 更新居民报装处理状态
     *
     * @param updateReqVO 更新信息
     */
    void updateApplyStatus(MemberWaterApplyStatusUpdateReqVO updateReqVO);

    /**
     * 校验设备号是否已绑定
     *
     * @param deviceNo 设备号
     * @param excludeApplyId 排除的申请编号
     * @return 是否已绑定
     */
    boolean isDeviceNoUsed(String deviceNo, Long excludeApplyId);
}
