<template>
  <s-layout title="用水历史" class="water-history" :bgStyle="{ color: '#f4f6fb' }" navbar="inner">
    <view class="device-summary">
      <view class="device-title">当前设备</view>
      <view class="device-value">{{ activeDeviceLabel }}</view>
      <view v-if="activeDeviceAddress" class="device-address">{{ activeDeviceAddress }}</view>
    </view>
    <s-empty v-if="state.pagination.total === 0" text="暂无用水历史" icon="/static/data-empty.png" />
    <view v-else class="history-table">
      <view class="history-row history-header">
        <view class="history-cell">本期示数(L)</view>
        <view class="history-cell">用水量(L)</view>
        <view class="history-cell">费用(元)</view>
        <view class="history-cell">日期</view>
      </view>
      <view v-for="item in state.pagination.list" :key="item.id" class="history-row">
        <view class="history-cell">{{ formatVolume(item.deviceTotalData) }}</view>
        <view class="history-cell">{{ formatVolume(getUsageValue(item)) }}</view>
        <view class="history-cell">{{ formatFee(item) }}</view>
        <view class="history-cell">{{ formatHistoryTime(item) }}</view>
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
  import WaterHistoryApi from '@/sheep/api/water/history';

  const waterDeviceStore = sheep.$store('waterDevice');
  const state = reactive({
    pagination: {
      list: [],
      total: 0,
      pageNo: 1,
      pageSize: 20,
    },
    loadStatus: '',
  });

  const activeDevice = computed(() => waterDeviceStore.activeDevice);
  const activeDeviceLabel = computed(() => {
    if (!waterDeviceStore.activeDeviceNo) {
      return '暂无设备';
    }
    return waterDeviceStore.activeDeviceNo;
  });
  const activeDeviceAddress = computed(() =>
    waterDeviceStore.formatAddress(activeDevice.value)
  );

  function getHistoryTime(item) {
    return item.deviceUpdateTime || item.createTime || item.deviceClock || '';
  }

  function formatHistoryTime(item) {
    const value = getHistoryTime(item);
    return value ? sheep.$helper.timeFormat(value, 'mm-dd hh:MM') : '--';
  }

  function getUsageValue(item) {
    return Number(item.deviceSettleDayData ?? item.deviceCurrentData ?? 0);
  }

  function formatVolume(value) {
    if (value === null || value === undefined || value === '') {
      return '--';
    }
    const numberValue = Number(value);
    if (Number.isNaN(numberValue)) {
      return '--';
    }
    return numberValue.toFixed(2);
  }

  function parseCycleFee(content) {
    if (!content) return null;
    try {
      const data = JSON.parse(content);
      const feeValue = data?.fee ?? data?.amount ?? data?.waterFee ?? data?.feeAmount;
      return feeValue ?? null;
    } catch (error) {
      return null;
    }
  }

  function formatFee(item) {
    const feeValue = parseCycleFee(item.cycleReportContent);
    if (feeValue === null || feeValue === undefined || feeValue === '') {
      return '--';
    }
    const feeNumber = Number(feeValue);
    if (Number.isNaN(feeNumber)) {
      return '--';
    }
    return feeNumber.toFixed(2);
  }

  async function getHistoryList() {
    if (!waterDeviceStore.activeDeviceNo) {
      state.pagination.list = [];
      state.pagination.total = 0;
      state.loadStatus = '';
      return;
    }
    state.loadStatus = 'loading';
    const { code, data } = await WaterHistoryApi.getHistoryPage({
      pageNo: state.pagination.pageNo,
      pageSize: state.pagination.pageSize,
      deviceNo: waterDeviceStore.activeDeviceNo,
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
    await waterDeviceStore.fetchDevices();
    await getHistoryList();
  });

  onReachBottom(() => {
    if (state.loadStatus === 'noMore') return;
    state.pagination.pageNo++;
    getHistoryList();
  });
</script>

<style lang="scss" scoped>
  .water-history {
    .device-summary {
      margin: 20rpx 24rpx 0;
      padding: 20rpx 24rpx;
      border-radius: 16rpx;
      background: #ffffff;
      box-shadow: 0 8rpx 20rpx rgba(0, 0, 0, 0.06);
    }

    .device-title {
      font-size: 22rpx;
      color: #8c8c8c;
      margin-bottom: 6rpx;
    }

    .device-value {
      font-size: 28rpx;
      font-weight: 600;
      color: #333333;
    }

    .device-address {
      font-size: 22rpx;
      color: #666666;
      margin-top: 6rpx;
    }

    .history-table {
      margin: 20rpx 24rpx 40rpx;
      border-radius: 16rpx;
      overflow: hidden;
      border: 1rpx solid #e9f0fa;
      background: #ffffff;
    }

    .history-row {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      padding: 16rpx 8rpx;
      text-align: center;
      font-size: 24rpx;
      color: #345;
      background: #f7fbff;
    }

    .history-row:nth-child(odd) {
      background: #eef6ff;
    }

    .history-header {
      font-size: 22rpx;
      font-weight: 600;
      color: #7a8794;
      background: #f4f7fb;
    }

    .history-cell {
      padding: 0 6rpx;
    }
  }
</style>
