import request from '@/sheep/request';

const WaterHistoryApi = {
  // 获得设备用水历史分页
  getHistoryPage: (params) => {
    return request({
      url: '/member/water-device-history/page',
      method: 'GET',
      params,
    });
  },
};

export default WaterHistoryApi;
