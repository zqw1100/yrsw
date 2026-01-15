import request from '@/sheep/request';

const WaterHistoryApi = {
  // 获得设备用水历史分页
  getHistoryPage: (params) => {
    return request({
      url: '/member/water-fee-bill/page',
      method: 'GET',
      params,
    });
  },
  // 获得设备日用水统计
  getDailyStats: (params) => {
    return request({
      url: '/member/water-fee-bill/stats/daily',
      method: 'GET',
      params,
    });
  },
  // 获得设备月度水费统计
  getMonthlyStats: (params) => {
    return request({
      url: '/member/water-fee-bill/stats/monthly',
      method: 'GET',
      params,
    });
  },
};

export default WaterHistoryApi;
