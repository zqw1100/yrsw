package cn.iocoder.yudao.module.member.service.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterHouseCreateReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterHousePageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterHouseUpdateReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterHouseRoomRespVO;
import cn.iocoder.yudao.module.member.convert.water.MemberWaterHouseConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterHouseDO;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterHouseMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.WATER_HOUSE_NOT_EXISTS;

/**
 * 居民报装房屋信息 Service 实现类
 */
@Service
@Validated
public class MemberWaterHouseServiceImpl implements MemberWaterHouseService {

    @Resource
    private MemberWaterHouseMapper waterHouseMapper;

    @Override
    public Long createMemberWaterHouse(MemberWaterHouseCreateReqVO createReqVO) {
        MemberWaterHouseDO waterHouse = MemberWaterHouseConvert.INSTANCE.convert(createReqVO);
        waterHouseMapper.insert(waterHouse);
        return waterHouse.getId();
    }

    @Override
    public void updateMemberWaterHouse(MemberWaterHouseUpdateReqVO updateReqVO) {
        validateWaterHouseExists(updateReqVO.getId());
        MemberWaterHouseDO updateObj = MemberWaterHouseConvert.INSTANCE.convert(updateReqVO);
        waterHouseMapper.updateById(updateObj);
    }

    @Override
    public void deleteMemberWaterHouse(Long id) {
        validateWaterHouseExists(id);
        waterHouseMapper.deleteById(id);
    }

    @Override
    public MemberWaterHouseDO getMemberWaterHouse(Long id) {
        return waterHouseMapper.selectById(id);
    }

    @Override
    public PageResult<MemberWaterHouseDO> getMemberWaterHousePage(MemberWaterHousePageReqVO pageReqVO) {
        return waterHouseMapper.selectPage(pageReqVO);
    }

    @Override
    public List<String> getCommunityNameList(Long areaId) {
        return waterHouseMapper.selectList(new cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX<MemberWaterHouseDO>()
                .eq(MemberWaterHouseDO::getAreaId, areaId)
                .select(MemberWaterHouseDO::getCommunityName))
                .stream()
                .map(MemberWaterHouseDO::getCommunityName)
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getBuildingNameList(Long areaId, String communityName) {
        return waterHouseMapper.selectList(new cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX<MemberWaterHouseDO>()
                .eq(MemberWaterHouseDO::getAreaId, areaId)
                .eq(MemberWaterHouseDO::getCommunityName, communityName)
                .select(MemberWaterHouseDO::getBuildingName))
                .stream()
                .map(MemberWaterHouseDO::getBuildingName)
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getUnitNameList(Long areaId, String communityName, String buildingName) {
        return waterHouseMapper.selectList(new cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX<MemberWaterHouseDO>()
                .eq(MemberWaterHouseDO::getAreaId, areaId)
                .eq(MemberWaterHouseDO::getCommunityName, communityName)
                .eq(MemberWaterHouseDO::getBuildingName, buildingName)
                .select(MemberWaterHouseDO::getUnitName))
                .stream()
                .map(MemberWaterHouseDO::getUnitName)
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<AppWaterHouseRoomRespVO> getRoomList(Long areaId, String communityName, String buildingName, String unitName) {
        List<MemberWaterHouseDO> list = waterHouseMapper.selectList(new cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX<MemberWaterHouseDO>()
                .eq(MemberWaterHouseDO::getAreaId, areaId)
                .eq(MemberWaterHouseDO::getCommunityName, communityName)
                .eq(MemberWaterHouseDO::getBuildingName, buildingName)
                .eq(MemberWaterHouseDO::getUnitName, unitName));
        return list.stream()
                .sorted(Comparator.comparing(MemberWaterHouseDO::getSort, Comparator.nullsLast(Integer::compareTo))
                        .thenComparing(MemberWaterHouseDO::getRoomNo, Comparator.nullsLast(String::compareTo)))
                .map(MemberWaterHouseConvert.INSTANCE::convert)
                .collect(Collectors.toList());
    }

    private void validateWaterHouseExists(Long id) {
        if (waterHouseMapper.selectById(id) == null) {
            throw exception(WATER_HOUSE_NOT_EXISTS);
        }
    }
}
