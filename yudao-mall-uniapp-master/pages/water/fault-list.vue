<template>
  <s-layout title="维修列表" class="fault-list" :bgStyle="{ color: '#f4f6fb' }" navbar="inner">
    <s-empty v-if="state.pagination.total === 0" text="暂无报修信息" icon="/static/data-empty.png" />
    <view v-else class="list-wrap">
      <view v-for="item in state.pagination.list" :key="item.id" class="fault-card">
        <view class="card-header ss-flex ss-row-between ss-col-center">
          <view class="title">故障报修</view>
          <view class="status-pill" :class="statusClass(item.processStatus)">
            {{ statusLabel(item.processStatus) }}
          </view>
        </view>
        <view class="info-text">故障原因：{{ faultLabel(item.faultCode) }}</view>
        <view class="info-text">联系电话：{{ item.contactMobile || '-' }}</view>
        <view class="info-text">设备号：{{ item.deviceNo || '-' }}</view>
        <view class="info-text">报修地址：{{ formatAddress(item) }}</view>
        <view class="info-text">优先级：{{ priorityLabel(item.priority) }}</view>
        <view v-if="item.createTime" class="info-text">报修时间：{{ formatTime(item.createTime) }}</view>
      </view>
    </view>
    <uni-load-more
      v-if="state.pagination.total > 0"
      :status="state.loadStatus"
      :content-text="{ contentdown: '上拉加载更多' }"
    />
  </s-layout>
</template>

<script setup>
  import { computed, reactive } from 'vue';
  import { onLoad, onReachBottom } from '@dcloudio/uni-app';
  import { concat } from 'lodash-es';
  import sheep from '@/sheep';
  import DictApi from '@/sheep/api/system/dict';
  import WaterFaultApi from '@/sheep/api/water/fault';

  const state = reactive({
    pagination: {
      list: [],
      total: 0,
      pageNo: 1,
      pageSize: 10,
    },
    loadStatus: '',
    faultOptions: [],
  });

  const faultMap = computed(() => {
    const map = {};
    state.faultOptions.forEach((item) => {
      map[item.value] = item;
    });
    return map;
  });

  const faultLabel = (value) => {
    return faultMap.value[value]?.label || value || '-';
  };

  const statusLabel = (value) => {
    const statusMap = {
      0: '待处理',
      1: '处理中',
      2: '已完成',
    };
    return statusMap[value] || '处理中';
  };

  const statusClass = (value) => {
    return `status-${value ?? 'default'}`;
  };

  const priorityLabel = (value) => {
    const map = { 1: '低', 2: '中', 3: '高' };
    return map[value] || '-';
  };

  const formatAddress = (item) => {
    return [item.areaName, item.communityName, item.buildingName, item.unitName, item.roomNo]
      .filter(Boolean)
      .join(' ');
  };

  const formatTime = (time) => {
    return sheep.$helper.timeFormat(time, 'yyyy-mm-dd hh:MM:ss');
  };

  async function getFaultOptions() {
    const { code, data } = await DictApi.getDictDataListByType('device-fault-code');
    if (code !== 0) return;
    state.faultOptions = data || [];
  }

  async function getFaultList() {
    state.loadStatus = 'loading';
    const { code, data } = await WaterFaultApi.getFaultPage({
      pageNo: state.pagination.pageNo,
      pageSize: state.pagination.pageSize,
    });
    if (code !== 0) {
      state.loadStatus = '';
      return;
    }
    state.pagination.list = concat(state.pagination.list, data.list || []);
    state.pagination.total = data.total || 0;
    state.loadStatus = state.pagination.list.length < state.pagination.total ? 'more' : 'noMore';
  }

  onLoad(async () => {
    await getFaultOptions();
    await getFaultList();
  });

  onReachBottom(() => {
    if (state.loadStatus === 'noMore') return;
    state.pagination.pageNo++;
    getFaultList();
  });
</script>

<style lang="scss" scoped>
  .fault-list {
    .list-wrap {
      padding: 20rpx 24rpx 40rpx;
    }

    .fault-card {
      background: #ffffff;
      border-radius: 20rpx;
      padding: 24rpx;
      margin-bottom: 20rpx;
      box-shadow: 0 8rpx 20rpx rgba(0, 0, 0, 0.06);
    }

    .card-header {
      margin-bottom: 16rpx;
    }

    .title {
      font-size: 28rpx;
      font-weight: 600;
      color: #333333;
    }

    .status-pill {
      font-size: 22rpx;
      padding: 6rpx 16rpx;
      border-radius: 999rpx;
    }

    .status-0 {
      color: #fa8c16;
      background: #fff7e6;
    }

    .status-1 {
      color: #1677ff;
      background: #e6f4ff;
    }

    .status-2 {
      color: #52c41a;
      background: #f6ffed;
    }

    .status-default {
      color: #8c8c8c;
      background: #f5f5f5;
    }

    .info-text {
      font-size: 24rpx;
      color: #555555;
      line-height: 1.6;
      margin-bottom: 8rpx;
    }
  }
</style>
