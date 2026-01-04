package cn.iocoder.yudao.module.member.dal.mysql.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterFaultPageReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFaultDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberWaterFaultMapper extends BaseMapperX<MemberWaterFaultDO> {

    default PageResult<MemberWaterFaultDO> selectPageByUserId(Long userId, AppWaterFaultPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MemberWaterFaultDO>()
                .eq(MemberWaterFaultDO::getUserId, userId)
                .orderByDesc(MemberWaterFaultDO::getId));
    }
}
