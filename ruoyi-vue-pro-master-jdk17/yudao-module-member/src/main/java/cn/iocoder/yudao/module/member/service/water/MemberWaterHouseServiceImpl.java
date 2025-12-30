package cn.iocoder.yudao.module.member.service.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.common.util.validation.ValidationUtils;
import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.*;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterHouseRoomRespVO;
import cn.iocoder.yudao.module.member.convert.water.MemberWaterHouseConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterHouseDO;
import cn.iocoder.yudao.module.member.dal.mysql.water.MemberWaterHouseMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import jakarta.validation.ConstraintViolationException;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.WATER_HOUSE_NOT_EXISTS;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.WATER_HOUSE_IMPORT_LIST_IS_EMPTY;

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
        List<MemberWaterHouseRespVO> respList = list.stream()
                .sorted(
                        Comparator
                                .comparing(MemberWaterHouseDO::getSort, Comparator.nullsLast(Integer::compareTo))
                                .thenComparing(MemberWaterHouseDO::getRoomNo, Comparator.nullsLast(String::compareTo))
                )
                .map(MemberWaterHouseConvert.INSTANCE::convert)
                .collect(Collectors.toList());
        return MemberWaterHouseConvert.INSTANCE.convertToAppRoomList(respList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MemberWaterHouseImportRespVO importWaterHouseList(List<MemberWaterHouseImportExcelVO> importList, boolean updateSupport) {
        if (CollUtil.isEmpty(importList)) {
            throw exception(WATER_HOUSE_IMPORT_LIST_IS_EMPTY);
        }
        MemberWaterHouseImportRespVO respVO = MemberWaterHouseImportRespVO.builder()
                .createKeys(new ArrayList<>())
                .updateKeys(new ArrayList<>())
                .failureKeys(new LinkedHashMap<>())
                .build();
        importList.forEach(importItem -> {
            String key = buildKey(importItem);
            MemberWaterHouseCreateReqVO createReqVO = BeanUtils.toBean(importItem, MemberWaterHouseCreateReqVO.class);
            if (createReqVO.getInstallStatus() == null) {
                createReqVO.setInstallStatus(0);
            }
            if (createReqVO.getRemark() == null) {
                createReqVO.setRemark("");
            }
            if (createReqVO.getDescription() == null) {
                createReqVO.setDescription("");
            }
            try {
                ValidationUtils.validate(createReqVO);
            } catch (ConstraintViolationException ex) {
                respVO.getFailureKeys().put(key, ex.getMessage());
                return;
            }
            MemberWaterHouseDO exist = waterHouseMapper.selectByUnique(
                    createReqVO.getAreaId(),
                    createReqVO.getCommunityName(),
                    createReqVO.getBuildingName(),
                    createReqVO.getUnitName(),
                    createReqVO.getRoomNo());
            if (exist == null) {
                waterHouseMapper.insert(MemberWaterHouseConvert.INSTANCE.convert(createReqVO));
                respVO.getCreateKeys().add(key);
                return;
            }
            if (!updateSupport) {
                respVO.getFailureKeys().put(key, "数据已存在");
                return;
            }
            MemberWaterHouseUpdateReqVO updateReqVO = BeanUtils.toBean(createReqVO, MemberWaterHouseUpdateReqVO.class);
            updateReqVO.setId(exist.getId());
            waterHouseMapper.updateById(MemberWaterHouseConvert.INSTANCE.convert(updateReqVO));
            respVO.getUpdateKeys().add(key);
        });
        return respVO;
    }

    @Override
    public List<MemberWaterHouseImportExcelVO> getImportTemplate() {
        MemberWaterHouseImportExcelVO demo = new MemberWaterHouseImportExcelVO();
        demo.setAreaId(110105L);
        demo.setAreaName("北京市 朝阳区");
        demo.setCommunityName("城市花园");
        demo.setBuildingName("1号楼");
        demo.setUnitName("1单元");
        demo.setRoomNo("302");
        demo.setInstallStatus(0);
        demo.setSort(0);
        demo.setRemark("一期");
        demo.setDescription("示例数据");
        return List.of(demo);
    }

    private void validateWaterHouseExists(Long id) {
        if (waterHouseMapper.selectById(id) == null) {
            throw exception(WATER_HOUSE_NOT_EXISTS);
        }
    }

    private String buildKey(MemberWaterHouseImportExcelVO importItem) {
        return String.format("%s-%s-%s-%s-%s",
                Objects.toString(importItem.getAreaName(), ""),
                Objects.toString(importItem.getCommunityName(), ""),
                Objects.toString(importItem.getBuildingName(), ""),
                Objects.toString(importItem.getUnitName(), ""),
                Objects.toString(importItem.getRoomNo(), ""));
    }
}
