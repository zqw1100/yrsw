<template>
  <s-layout title="用水历史" class="water-history" :bgStyle="{ color: '#f4f6fb' }" navbar="inner">
    <view class="device-summary">
      <view class="device-title">当前设备</view>
      <view class="device-value">{{ activeDeviceLabel }}</view>
      <view v-if="activeDeviceAddress" class="device-address">{{ activeDeviceAddress }}</view>
    </view>

    <view class="history-tabs">
      <view
        v-for="item in tabOptions"
        :key="item.value"
        class="history-tab"
        :class="{ active: state.activeTab === item.value }"
        @tap="onTabChange(item.value)"
      >
        {{ item.label }}
      </view>
    </view>

    <view v-if="state.activeTab === 'history'">
      <s-empty v-if="state.pagination.total === 0" text="暂无用水历史" icon="/static/data-empty.png" />
      <view v-else class="history-table">
        <view class="history-row history-header">
          <view class="history-cell">本期示数(L)</view>
          <view class="history-cell">用水量(L)</view>
          <view class="history-cell">费用(元)</view>
          <view class="history-cell">日期</view>
        </view>
        <view v-for="item in state.pagination.list" :key="item.id" class="history-row">
          <view class="history-cell">{{ formatVolume(getTotalUsage(item)) }}</view>
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
    </view>

    <view v-else class="stats-wrapper">
      <view class="stats-card">
        <view class="stats-row">
          <view class="stats-label">用水户号：</view>
          <view class="stats-value">{{ activeDeviceLabel }}</view>
        </view>
        <view class="stats-row">
          <view class="stats-label">户主：</view>
          <view class="stats-value">{{ activeDeviceOwner }}</view>
        </view>
        <view class="stats-row">
          <view class="stats-label">地址：</view>
          <view class="stats-value">{{ activeDeviceAddress || '-' }}</view>
        </view>
      </view>

      <view class="stats-tabs">
        <view
          v-for="item in statsTabs"
          :key="item.value"
          class="stats-tab"
          :class="{ active: state.statsTab === item.value }"
          @tap="onStatsTabChange(item.value)"
        >
          {{ item.label }}
        </view>
      </view>

      <view v-if="state.statsTab === 'daily'" class="stats-panel">
        <view class="stats-panel-header">
          <view class="nav-btn" @tap="changeMonth(-1)">◀</view>
          <view class="panel-title">{{ dayMonthLabel }}</view>
          <view class="nav-btn" @tap="changeMonth(1)">▶</view>
          <view class="unit-pill">单位：L</view>
        </view>
        <view class="calendar-header">
          <view v-for="item in weekLabels" :key="item" class="calendar-week">{{ item }}</view>
        </view>
        <view class="calendar-grid">
          <view
            v-for="(item, index) in dailyCalendar"
            :key="`day-${index}`"
            class="calendar-cell"
            :class="{ empty: !item.day, highlight: item.highlight }"
          >
            <view v-if="item.day" class="day-number">{{ item.day }}</view>
            <view v-if="item.day" class="day-value">{{ formatVolume(item.usage) }}</view>
          </view>
        </view>
        <s-empty v-if="dailyCalendar.length === 0" text="暂无日用水数据" icon="/static/data-empty.png" />
      </view>

      <view v-else class="stats-panel">
        <view class="stats-panel-header center">
          <picker mode="date" fields="year" :value="state.monthYear" @change="onYearPick">
            <view class="year-pill">{{ state.monthYear }}年</view>
          </picker>
        </view>
        <view class="chart-card">
          <view v-if="state.monthlyData.length === 0" class="chart-empty">
            <s-empty text="暂无月度数据" icon="/static/data-empty.png" />
          </view>
          <view v-else class="chart">
            <view
              v-for="item in state.monthlyData"
              :key="item.month"
              class="chart-row"
            >
              <view class="chart-month">{{ item.month }}月</view>
              <view class="chart-line">
                <view class="chart-bar" :style="{ width: `${item.percent}%` }" />
              </view>
              <view class="chart-value">{{ formatFeeAmount(item.fee) }}</view>
            </view>
          </view>
          <view class="chart-legend">月度水费</view>
        </view>
      </view>
    </view>
  </s-layout>
</template>

<script setup>
  import { computed, reactive } from 'vue';
  import { onLoad, onReachBottom } from '@dcloudio/uni-app';
  import { concat } from 'lodash-es';
  import dayjs from 'dayjs';
  import sheep from '@/sheep';
  import WaterHistoryApi from '@/sheep/api/water/history';

  const waterDeviceStore = sheep.$store('waterDevice');
  const tabOptions = [
    { label: '历史明细', value: 'history' },
    { label: '个人统计', value: 'stats' },
  ];
  const statsTabs = [
    { label: '日用水量', value: 'daily' },
    { label: '月度水费', value: 'monthly' },
  ];
  const weekLabels = ['日', '一', '二', '三', '四', '五', '六'];
  const state = reactive({
    pagination: {
      list: [],
      total: 0,
      pageNo: 1,
      pageSize: 20,
    },
    loadStatus: '',
    activeTab: 'history',
    statsTab: 'daily',
    dayMonth: dayjs().format('YYYY-MM'),
    monthYear: dayjs().format('YYYY'),
    dailyStats: [],
    monthlyStats: [],
    monthlyData: [],
  });

  const activeDevice = computed(() => waterDeviceStore.activeDevice);
  const activeDeviceLabel = computed(() => {
    if (!waterDeviceStore.activeDeviceNo) {
      return '暂无设备';
    }
    return waterDeviceStore.activeDeviceNo;
  });
  const activeDeviceOwner = computed(() => activeDevice.value?.contactName || '-');
  const activeDeviceAddress = computed(() =>
    waterDeviceStore.formatAddress(activeDevice.value)
  );
  const dayMonthLabel = computed(() => {
    if (!state.dayMonth) {
      return '--';
    }
    const [year, month] = state.dayMonth.split('-');
    return `${year}年${Number(month)}月`;
  });
  const dailyCalendar = computed(() => buildCalendar(state.dayMonth, state.dailyStats));

  function getHistoryTime(item) {
    return item.statDate || item.createTime || '';
  }

  function formatHistoryTime(item) {
    const value = getHistoryTime(item);
    return value ? sheep.$helper.timeFormat(value, 'mm-dd hh:MM') : '--';
  }

  function getUsageValue(item) {
    if (item.usageDiff !== undefined && item.usageDiff !== null) {
      return Number(item.usageDiff);
    }
    if (item.totalUsage !== undefined && item.lastTotalUsage !== undefined) {
      return Number(item.totalUsage) - Number(item.lastTotalUsage);
    }
    return Number(item.deviceSettleDayData ?? item.deviceCurrentData ?? 0);
  }

  function getTotalUsage(item) {
    return item.totalUsage ?? item.deviceTotalData ?? null;
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

  function formatFee(item) {
    const feeValue = item.fee ?? item.feeAmount ?? null;
    if (feeValue === null || feeValue === undefined || feeValue === '') {
      return '--';
    }
    const feeNumber = Number(feeValue);
    if (Number.isNaN(feeNumber)) {
      return '--';
    }
    return (feeNumber / 100).toFixed(2);
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

  async function getDailyStats() {
    if (!waterDeviceStore.activeDeviceNo) {
      state.dailyStats = [];
      return;
    }
    const { code, data } = await WaterHistoryApi.getDailyStats({
      deviceNo: waterDeviceStore.activeDeviceNo,
      month: state.dayMonth,
    });
    if (code !== 0) {
      state.dailyStats = [];
      return;
    }
    state.dailyStats = data || [];
  }

  async function getMonthlyStats() {
    if (!waterDeviceStore.activeDeviceNo) {
      state.monthlyStats = [];
      state.monthlyData = [];
      return;
    }
    const { code, data } = await WaterHistoryApi.getMonthlyStats({
      deviceNo: waterDeviceStore.activeDeviceNo,
      year: state.monthYear,
    });
    if (code !== 0) {
      state.monthlyStats = [];
      state.monthlyData = [];
      return;
    }
    state.monthlyStats = data || [];
    state.monthlyData = buildMonthlyChart(state.monthlyStats);
  }

  function buildMonthlyChart(list) {
    const maxValue = Math.max(
      1,
      ...list.map((item) => Number(item.fee || 0))
    );
    return list.map((item) => ({
      ...item,
      percent: Math.round((Number(item.fee || 0) / maxValue) * 100),
    }));
  }

  function buildCalendar(month, list) {
    if (!month) return [];
    const start = dayjs(`${month}-01`);
    if (!start.isValid()) return [];
    const daysInMonth = start.daysInMonth();
    const firstDay = start.day();
    const lookup = new Map();
    list.forEach((item) => {
      const day = dayjs(item.statDate).date();
      if (day) {
        lookup.set(day, item);
      }
    });
    const cells = [];
    for (let i = 0; i < firstDay; i++) {
      cells.push({ day: '', usage: null, highlight: false });
    }
    for (let day = 1; day <= daysInMonth; day++) {
      const match = lookup.get(day);
      const usage = match ? getUsageValue(match) : null;
      cells.push({
        day,
        usage,
        highlight: usage !== null && Number(usage) > 0,
      });
    }
    return cells;
  }

  function onTabChange(value) {
    state.activeTab = value;
    if (value === 'stats') {
      getDailyStats();
      getMonthlyStats();
    }
  }

  function onStatsTabChange(value) {
    state.statsTab = value;
    if (value === 'daily') {
      getDailyStats();
    } else {
      getMonthlyStats();
    }
  }

  function changeMonth(step) {
    state.dayMonth = dayjs(state.dayMonth).add(step, 'month').format('YYYY-MM');
    getDailyStats();
  }

  function onYearPick(event) {
    const value = event.detail.value;
    if (value) {
      state.monthYear = value;
      getMonthlyStats();
    }
  }

  function formatFeeAmount(value) {
    if (value === null || value === undefined || value === '') {
      return '--';
    }
    const feeNumber = Number(value);
    if (Number.isNaN(feeNumber)) {
      return '--';
    }
    return (feeNumber / 100).toFixed(2);
  }

  onLoad(async () => {
    await waterDeviceStore.fetchDevices();
    await getHistoryList();
    await getDailyStats();
    await getMonthlyStats();
  });

  onReachBottom(() => {
    if (state.activeTab !== 'history') return;
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

    .history-tabs {
      margin: 20rpx 24rpx 0;
      background: #efefef;
      border-radius: 30rpx;
      padding: 6rpx;
      display: flex;
      gap: 12rpx;
    }

    .history-tab {
      flex: 1;
      text-align: center;
      font-size: 26rpx;
      color: #6e6e6e;
      padding: 12rpx 0;
      border-radius: 24rpx;
      background: transparent;
    }

    .history-tab.active {
      background: #ffffff;
      color: #1f2d3d;
      font-weight: 600;
      box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.08);
    }

    .stats-wrapper {
      padding: 20rpx 24rpx 40rpx;
    }

    .stats-card {
      background: #ffffff;
      border-radius: 18rpx;
      padding: 20rpx 24rpx;
      box-shadow: 0 8rpx 20rpx rgba(0, 0, 0, 0.06);
      margin-bottom: 20rpx;
    }

    .stats-row {
      display: flex;
      align-items: center;
      font-size: 26rpx;
      color: #333;
      margin-bottom: 12rpx;
    }

    .stats-row:last-child {
      margin-bottom: 0;
    }

    .stats-label {
      width: 140rpx;
      color: #8c8c8c;
    }

    .stats-value {
      flex: 1;
    }

    .stats-tabs {
      display: flex;
      background: #efefef;
      border-radius: 30rpx;
      padding: 6rpx;
      margin-bottom: 20rpx;
      gap: 12rpx;
    }

    .stats-tab {
      flex: 1;
      text-align: center;
      font-size: 26rpx;
      color: #6e6e6e;
      padding: 12rpx 0;
      border-radius: 24rpx;
      background: transparent;
    }

    .stats-tab.active {
      background: #ffffff;
      color: #1f2d3d;
      font-weight: 600;
      box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.08);
    }

    .stats-panel {
      background: #ffffff;
      border-radius: 18rpx;
      padding: 20rpx 16rpx 24rpx;
      box-shadow: 0 8rpx 20rpx rgba(0, 0, 0, 0.06);
    }

    .stats-panel-header {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 16rpx;
      position: relative;
      padding-bottom: 12rpx;
    }

    .stats-panel-header.center {
      justify-content: center;
    }

    .nav-btn {
      width: 44rpx;
      height: 44rpx;
      border-radius: 50%;
      background: #f2f6ff;
      color: #2a62d5;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 22rpx;
    }

    .panel-title {
      font-size: 32rpx;
      font-weight: 600;
      color: #333;
    }

    .unit-pill {
      position: absolute;
      right: 0;
      top: 0;
      background: #e5f2ff;
      color: #2a62d5;
      font-size: 22rpx;
      padding: 6rpx 16rpx;
      border-radius: 20rpx;
    }

    .calendar-header {
      display: grid;
      grid-template-columns: repeat(7, 1fr);
      text-align: center;
      padding: 12rpx 0;
      color: #777;
      font-size: 24rpx;
    }

    .calendar-grid {
      display: grid;
      grid-template-columns: repeat(7, 1fr);
      gap: 10rpx;
      padding: 0 10rpx 10rpx;
    }

    .calendar-cell {
      background: #f7f7f7;
      border-radius: 12rpx;
      padding: 10rpx 6rpx;
      min-height: 90rpx;
      text-align: center;
      font-size: 22rpx;
      color: #666;
    }

    .calendar-cell.highlight {
      background: #e8f4ff;
      color: #1b5bbb;
      font-weight: 600;
    }

    .calendar-cell.empty {
      background: transparent;
    }

    .day-number {
      font-size: 24rpx;
      color: #555;
    }

    .day-value {
      margin-top: 6rpx;
      font-size: 22rpx;
    }

    .year-pill {
      border: 2rpx solid #2a7de1;
      color: #2a7de1;
      border-radius: 40rpx;
      padding: 10rpx 40rpx;
      font-size: 26rpx;
      font-weight: 600;
    }

    .chart-card {
      margin-top: 10rpx;
    }

    .chart-row {
      display: grid;
      grid-template-columns: 80rpx 1fr 120rpx;
      align-items: center;
      gap: 12rpx;
      margin-bottom: 18rpx;
    }

    .chart-month {
      font-size: 24rpx;
      color: #666;
    }

    .chart-line {
      background: #eef2f7;
      border-radius: 12rpx;
      height: 12rpx;
      overflow: hidden;
    }

    .chart-bar {
      height: 12rpx;
      border-radius: 12rpx;
      background: linear-gradient(90deg, #2a7de1, #5bb1ff);
    }

    .chart-value {
      text-align: right;
      font-size: 24rpx;
      color: #333;
    }

    .chart-legend {
      text-align: center;
      font-size: 24rpx;
      color: #7a8794;
      margin-top: 16rpx;
    }
  }
</style>
