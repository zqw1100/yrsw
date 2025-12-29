import request from '@/sheep/request';

const WaterHouseApi = {
  // 获得小区列表
  getCommunityList: (areaId) => {
    return request({
      url: '/member/water-house/community-list',
      method: 'GET',
      params: { areaId },
    });
  },
  // 获得楼栋列表
  getBuildingList: (areaId, communityName) => {
    return request({
      url: '/member/water-house/building-list',
      method: 'GET',
      params: { areaId, communityName },
    });
  },
  // 获得单元列表
  getUnitList: (areaId, communityName, buildingName) => {
    return request({
      url: '/member/water-house/unit-list',
      method: 'GET',
      params: { areaId, communityName, buildingName },
    });
  },
  // 获得房间列表
  getRoomList: (params) => {
    return request({
      url: '/member/water-house/room-list',
      method: 'GET',
      params,
    });
  },
};

export default WaterHouseApi;
