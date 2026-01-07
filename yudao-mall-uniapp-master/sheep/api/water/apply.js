import request from '@/sheep/request';

const WaterApplyApi = {
  // 创建居民报装申请
  createApply: (data) => {
    return request({
      url: '/member/water-apply/create',
      method: 'POST',
      data,
    });
  },
  // 补充居民报装资料
  completeApply: (data) => {
    return request({
      url: '/member/water-apply/complete',
      method: 'PUT',
      data,
    });
  },
  // 获得居民报装记录分页
  getApplyPage: (params) => {
    return request({
      url: '/member/water-apply/page',
      method: 'GET',
      params,
    });
  },
  // 校验设备号是否已绑定
  checkDeviceNo: (params) => {
    return request({
      url: '/member/water-apply/check-device',
      method: 'GET',
      params,
    });
  },
  // 获得充值套餐列表
  getRechargePackageList: () => {
    return request({
      url: '/member/water-recharge-package/list',
      method: 'GET',
    });
  },
};

export default WaterApplyApi;
