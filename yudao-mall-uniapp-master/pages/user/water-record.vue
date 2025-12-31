<template>
  <s-layout title="报装记录" class="water-record" :bgStyle="{ color: '#f4f6fb' }" navbar="inner">
    <s-empty v-if="state.pagination.total === 0" text="暂无报装记录" icon="/static/data-empty.png" />
    <view v-else class="record-list">
      <view v-for="item in state.pagination.list" :key="item.id" class="record-card">
        <view class="card-header ss-flex ss-row-between ss-col-center">
          <view class="title">报装状态</view>
          <view class="status-pill" :class="statusClass(item.processStatus)">
            {{ statusLabel(item.processStatus) }}
          </view>
        </view>
        <view class="info-text">报装地址：{{ formatAddress(item) }}</view>
        <view class="info-text">联系人：{{ item.contactName || '-' }}</view>
        <view class="info-text">联系方式：{{ item.contactMobile || '-' }}</view>
        <view v-if="item.createTime" class="info-text">
          申请时间：{{ formatTime(item.createTime) }}
        </view>
        <view v-if="isCompleted(item.processStatus)" class="info-text device-no">
          设备号：{{ item.deviceNo || '-' }}
        </view>
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
  import WaterApplyApi from '@/sheep/api/water/apply';
  import DictApi from '@/sheep/api/system/dict';

  const COMPLETE_STATUS = [3,4];

  const state = reactive({
    pagination: {
      list: [],
      total: 0,
      pageNo: 1,
      pageSize: 10,
    },
    loadStatus: '',
    statusOptions: [],
  });

  const statusMap = computed(() => {
    const map = {};
    state.statusOptions.forEach((item) => {
      map[Number(item.value)] = item;
    });
    return map;
  });

  const statusLabel = (value) => {
    return statusMap.value[value]?.label || '处理中';
  };

  const statusClass = (value) => {
    return `status-${value ?? 'default'}`;
  };

  const isCompleted = (value) => COMPLETE_STATUS.includes(Number(value))

  const formatAddress = (item) => {
    return [item.areaName, item.communityName, item.buildingName, item.unitName, item.roomNo]
      .filter(Boolean)
      .join(' ');
  };

  const formatTime = (time) => {
    return sheep.$helper.timeFormat(time, 'yyyy-mm-dd hh:MM:ss');
  };

  async function getStatusOptions() {
    const { code, data } = await DictApi.getDictDataListByType('member_water_apply_status');
    if (code !== 0) {
      return;
    }
    state.statusOptions = data || [];
  }

  async function getApplyList() {
    state.loadStatus = 'loading';
    const { code, data } = await WaterApplyApi.getApplyPage({
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
    await getStatusOptions();
    await getApplyList();
  });

  onReachBottom(() => {
    if (state.loadStatus === 'noMore') {
      return;
    }
    state.pagination.pageNo++;
    getApplyList();
  });
</script>

<style lang="scss" scoped>
  .water-record {
    .record-list {
      padding: 20rpx 24rpx 40rpx;
    }

    .record-card {
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
      color: #2f54eb;
      background: #f0f5ff;
    }

    .status-3 {
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

    .device-no {
      color: #3c7eff;
      font-weight: 600;
    }
  }
</style>
