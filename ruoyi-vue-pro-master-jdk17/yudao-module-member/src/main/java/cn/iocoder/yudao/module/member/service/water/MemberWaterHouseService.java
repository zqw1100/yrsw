package cn.iocoder.yudao.module.member.service.water;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterHouseCreateReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterHouseImportExcelVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterHouseImportRespVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterHousePageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.water.vo.MemberWaterHouseUpdateReqVO;
import cn.iocoder.yudao.module.member.controller.app.water.vo.AppWaterHouseRoomRespVO;
import cn.iocoder.yudao.module.member.dal.dataobject.water.MemberWaterHouseDO;

import java.util.List;

/**
 * 居民报装房屋信息 Service
 */
public interface MemberWaterHouseService {

    Long createMemberWaterHouse(MemberWaterHouseCreateReqVO createReqVO);

    void updateMemberWaterHouse(MemberWaterHouseUpdateReqVO updateReqVO);

    void deleteMemberWaterHouse(Long id);

    MemberWaterHouseDO getMemberWaterHouse(Long id);

    PageResult<MemberWaterHouseDO> getMemberWaterHousePage(MemberWaterHousePageReqVO pageReqVO);

    List<String> getCommunityNameList(Long areaId);

    List<String> getBuildingNameList(Long areaId, String communityName);

    List<String> getUnitNameList(Long areaId, String communityName, String buildingName);

    List<AppWaterHouseRoomRespVO> getRoomList(Long areaId, String communityName, String buildingName, String unitName);

    MemberWaterHouseImportRespVO importWaterHouseList(List<MemberWaterHouseImportExcelVO> importList, boolean updateSupport);

    List<MemberWaterHouseImportExcelVO> getImportTemplate();
}
