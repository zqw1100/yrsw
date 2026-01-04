import request from '@/sheep/request';

const WaterFaultApi = {
  // 初始化故障报修信息
  getFaultInit: () => {
    return request({
      url: '/member/water-fault/init',
      method: 'GET',
    });
  },
  // 创建故障报修
  createFault: (data) => {
    return request({
      url: '/member/water-fault/create',
      method: 'POST',
      data,
    });
  },
  // 获得故障报修分页
  getFaultPage: (params) => {
    return request({
      url: '/member/water-fault/page',
      method: 'GET',
      params,
    });
  },
};

export default WaterFaultApi;
