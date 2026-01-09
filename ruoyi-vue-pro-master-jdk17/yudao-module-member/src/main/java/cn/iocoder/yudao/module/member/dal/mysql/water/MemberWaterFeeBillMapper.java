package cn.iocoder.yudao.module.member.dal.mysql.water;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterFeeBillPageReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterFeeBillDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 水费结算流水 Mapper
 */
@Mapper
public interface MemberWaterFeeBillMapper extends BaseMapperX<MemberWaterFeeBillDO> {

    default PageResult<MemberWaterFeeBillDO> selectPage(MemberWaterFeeBillPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MemberWaterFeeBillDO>()
                .likeIfPresent(MemberWaterFeeBillDO::getDeviceNo, reqVO.getDeviceNo())
                .betweenIfPresent(MemberWaterFeeBillDO::getStatDate, reqVO.getStatDate())
                .orderByDesc(MemberWaterFeeBillDO::getId));
    }

    default MemberWaterFeeBillDO selectLatestByDeviceNo(String deviceNo) {
        return selectOne(new LambdaQueryWrapperX<MemberWaterFeeBillDO>()
                .eq(MemberWaterFeeBillDO::getDeviceNo, deviceNo)
                .orderByDesc(MemberWaterFeeBillDO::getStatDate)
                .orderByDesc(MemberWaterFeeBillDO::getId)
                .last("LIMIT 1"));
    }
}
