import request from '@/sheep/request';

const WorkOrderApi = {
  // 获得施工工单分页
  getWorkOrderPage: (params) => {
    return request({
      url: '/member/water-work-order/page',
      method: 'GET',
      params,
      custom: {
        showLoading: true,
        auth: true,
      },
    });
  },
  // 获得施工工单详情
  getWorkOrder: (id) => {
    return request({
      url: '/member/water-work-order/get',
      method: 'GET',
      params: { id },
      custom: {
        showLoading: true,
        auth: true,
      },
    });
  },
  // 指派施工人员
  assignWorkOrder: (data) => {
    return request({
      url: '/member/water-work-order/assign',
      method: 'POST',
      data,
      custom: {
        showLoading: true,
        auth: true,
        showSuccess: true,
        successMsg: '指派成功',
      },
    });
  },
  // 撤回指派
  revokeWorkOrder: (data) => {
    return request({
      url: '/member/water-work-order/revoke',
      method: 'POST',
      data,
      custom: {
        showLoading: true,
        auth: true,
        showSuccess: true,
        successMsg: '已撤回',
      },
    });
  },
  // 接受工单
  acceptWorkOrder: (data) => {
    return request({
      url: '/member/water-work-order/accept',
      method: 'POST',
      data,
      custom: {
        showLoading: true,
        auth: true,
        showSuccess: true,
        successMsg: '已接受',
      },
    });
  },
  // 施工前提交
  startWorkOrder: (data) => {
    return request({
      url: '/member/water-work-order/start',
      method: 'POST',
      data,
      custom: {
        showLoading: true,
        auth: true,
        showSuccess: true,
        successMsg: '已提交施工前信息',
      },
    });
  },
  // 施工完成提交
  finishWorkOrder: (data) => {
    return request({
      url: '/member/water-work-order/finish',
      method: 'POST',
      data,
      custom: {
        showLoading: true,
        auth: true,
        showSuccess: true,
        successMsg: '施工完成',
      },
    });
  },
};

export default WorkOrderApi;
