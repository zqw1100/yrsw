<template>
  <s-layout
    title="水务首页"
    class="water-home"
    :bgStyle="{ color: '#f3f6fb' }"
    navbar="inner"
    tabbar="/pages/index/user"
  >
    <view class="header">
      <view class="header-top ss-flex ss-col-center ss-row-between">
        <view>
          <view class="hello">欢迎您，{{ displayName }}</view>
          <view class="sub-hello">水务服务尽在掌握</view>
        </view>
        <view class="header-actions ss-flex">
<!--          <view class="action-circle" @tap="onGoNotice">-->
<!--            <uni-icons type="notification" size="20" color="#ffffff" />-->
<!--          </view>-->

        </view>
      </view>

      <view class="device-card ss-flex ss-col-center ss-row-between">
        <view class="device-info">
          <view class="device-title">当前设备</view>
          <view class="device-value">{{ activeDeviceLabel }}</view>
          <view v-if="activeDeviceAddress" class="device-address">{{ activeDeviceAddress }}</view>
        </view>
        <button class="ss-reset-button device-switch" @tap="onSwitchDevice">切换设备</button>
      </view>

      <view class="quick-card">
        <view v-for="item in quickMenus" :key="item.title" class="quick-item" @tap="item.action">
          <view class="quick-icon">
            <uni-icons :type="item.icon" size="22" color="#3c7eff" />
          </view>
          <view class="quick-title">{{ item.title }}</view>
        </view>
      </view>
    </view>

      <view class="service-card">
      <view class="service-tabs ss-flex ss-col-center ss-row-between">
        <view class="service-tab is-active">服务充值</view>
        <view class="service-tab" @tap="onGoFaultReport">客户报修</view>
      </view>
      <view class="service-body">
        <view class="service-info">
          <view class="service-label">账户余额</view>
          <view class="service-value">¥{{ fen2yuan(deviceWallet.balance) || '0.00' }}</view>
          <view class="service-time">
            更新时间：{{ balanceUpdateTime || '--' }}
          </view>
        </view>
        <view class="service-action" @tap="onPayRecharge">立即充值</view>
      </view>
      <view class="service-stats">
        <view class="stat-item">
          <view class="stat-value">0</view>
          <view class="stat-label">上月用水量(吨)</view>
        </view>
        <view class="stat-item">
          <view class="stat-value">0</view>
          <view class="stat-label">上月水费(元)</view>
        </view>
      </view>
    </view>

    <view class="history-card">
      <view class="history-title-row ss-flex ss-row-between ss-col-center">
        <view class="history-title">用水历史</view>
        <view class="history-more" @tap="onGoHistoryPage">更多</view>
      </view>
      <view class="history-summary">
        <view class="summary-item summary-blue">
          <view class="summary-label">昨日用水详情</view>
          <view class="summary-value">总用水量：{{ formatVolume(historySummary.yesterday) }} L</view>
        </view>
        <view class="summary-item summary-purple">
          <view class="summary-label">本月用水详情</view>
          <view class="summary-value">总用水量：{{ formatVolume(historySummary.month) }} L</view>
        </view>
        <view class="summary-item summary-green">
          <view class="summary-label">上月用水详情</view>
          <view class="summary-value">总用水量：{{ formatVolume(historySummary.lastMonth) }} L</view>
        </view>
        <view class="summary-item summary-orange">
          <view class="summary-label">本年用水详情</view>
          <view class="summary-value">总用水量：{{ formatVolume(historySummary.year) }} L</view>
        </view>
      </view>
      <view class="history-table">
        <view class="history-row history-header">
          <view class="history-cell">本期示数(L)</view>
          <view class="history-cell">用水量(L)</view>
          <view class="history-cell">费用(元)</view>
          <view class="history-cell">日期</view>
        </view>
        <view v-if="historyPreview.length">
          <view v-for="item in historyPreview" :key="item.id" class="history-row">
            <view class="history-cell">{{ formatVolume(item.deviceTotalData) }}</view>
            <view class="history-cell">{{ formatVolume(getUsageValue(item)) }}</view>
            <view class="history-cell">{{ formatFee(item) }}</view>
            <view class="history-cell">{{ formatHistoryTime(item) }}</view>
          </view>
        </view>
        <view v-else class="history-empty">暂无用水历史</view>
      </view>
    </view>

    <view class="notice-card">
      <view class="notice-header ss-flex ss-col-center ss-row-between">
        <view class="notice-title ss-flex ss-col-center">
          <uni-icons type="sound" size="18" color="#ff6b6b" />
          <text>信息公告</text>
        </view>
        <view class="notice-more" @tap="onGoNotice">更多</view>
      </view>
      <view v-if="latestNotice" class="notice-item" @tap="onOpenNotice">
        <view class="notice-dot"></view>
        <view class="notice-content">
          <view class="notice-text">{{ latestNotice.title }}</view>
          <view class="notice-time">{{ formatNoticeTime(latestNotice.createTime) }}</view>
        </view>
      </view>
      <view v-else class="notice-empty">暂无公告</view>

    </view>
  </s-layout>
</template>

<script setup>
  import { computed, ref } from 'vue';
  import { onShow } from '@dcloudio/uni-app';
  import sheep from '@/sheep';
  import { fen2yuan } from '@/sheep/hooks/useGoods';
  import { formatDate } from '@/sheep/helper/utils';
  import ArticleApi from '@/sheep/api/promotion/article';
  import WaterHistoryApi from '@/sheep/api/water/history';

  const NOTICE_CATEGORY_ID = 4;

  const userInfo = computed(() => sheep.$store('user').userInfo);
  const waterDeviceStore = sheep.$store('waterDevice');
  const deviceWallet = computed(() => waterDeviceStore.deviceWallet);
  const lastUpdateTime = computed(() => waterDeviceStore.lastWalletUpdate);
  const displayName = computed(() => userInfo.value.nickname || '未登录');
  const balanceUpdateTime = computed(() =>
    lastUpdateTime.value ? formatDate(lastUpdateTime.value) : ''
  );
  const latestNotice = ref(null);
  const historyList = ref([]);
  const historyPreview = computed(() => historyList.value.slice(0, 3));

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

  const quickMenus = [
    { title: '用水历史', icon: 'calendar', action: onGoHistory },
    { title: '消息通知', icon: 'notification', action: onGoNotice },
    { title: '在线缴费', icon: 'wallet', action: onPayRecharge },
  ];

  onShow(async () => {
    const userStore = sheep.$store('user');
    await userStore.updateUserData();
    if (!userStore.isLogin) {
      return;
    }
    await waterDeviceStore.fetchDevices();
    await waterDeviceStore.fetchWallet();
    await fetchHistory();
    await getLatestNotice();
  });

  function onPlaceholder() {
    uni.showToast({
      title: '功能建设中',
      icon: 'none',
    });
  }

  function onGoHistory() {
    uni.pageScrollTo({ selector: '.history-card', duration: 300 });
  }

  function onGoHistoryPage() {
    sheep.$router.go('/pages/water/history');
  }

  function onPayRecharge() {
    if (!waterDeviceStore.activeDeviceNo) {
      uni.showToast({ title: '暂无可用设备', icon: 'none' });
      return;
    }
    sheep.$router.go('/pages/pay/recharge');
  }

  function onGoNotice() {
    sheep.$router.go('/pages/water/notice-list');
  }

  function onGoFaultReport() {
    if (!waterDeviceStore.activeDeviceNo) {
      uni.showToast({ title: '暂无可用设备', icon: 'none' });
      return;
    }
    sheep.$router.go('/pages/water/fault-report');
  }

  function onSwitchDevice() {
    if (!waterDeviceStore.deviceList.length) {
      uni.showToast({ title: '暂无可切换设备', icon: 'none' });
      return;
    }
    const labels = waterDeviceStore.deviceList.map((item) => {
      const address = waterDeviceStore.formatAddress(item);
      return address ? `${item.deviceNo} (${address})` : item.deviceNo;
    });
    uni.showActionSheet({
      itemList: labels,
      success: async (res) => {
        const selected = waterDeviceStore.deviceList[res.tapIndex];
        if (!selected) return;
        waterDeviceStore.setActiveDeviceNo(selected.deviceNo);
        await waterDeviceStore.fetchWallet();
        await fetchHistory();
      },
    });
  }

  function onOpenNotice() {
    if (!latestNotice.value) {
      return;
    }
    sheep.$router.go('/pages/public/richtext', {
      id: latestNotice.value.id,
      title: latestNotice.value.title,
    });
  }

  function formatNoticeTime(time) {
    return time ? sheep.$helper.timeFormat(time, 'yyyy-mm-dd') : '';
  }

  async function getLatestNotice() {
    const { code, data } = await ArticleApi.getArticlePage({
      pageNo: 1,
      pageSize: 1,
      categoryId: NOTICE_CATEGORY_ID,
    });
    if (code !== 0) {
      latestNotice.value = null;
      return;
    }
    latestNotice.value = data.list?.[0] || null;
  }

  const historySummary = computed(() => {
    const now = new Date();
    const todayStart = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    const yesterdayStart = new Date(todayStart);
    yesterdayStart.setDate(todayStart.getDate() - 1);
    const monthStart = new Date(now.getFullYear(), now.getMonth(), 1);
    const lastMonthStart = new Date(now.getFullYear(), now.getMonth() - 1, 1);
    const lastMonthEnd = new Date(now.getFullYear(), now.getMonth(), 0, 23, 59, 59, 999);
    const yearStart = new Date(now.getFullYear(), 0, 1);
    const summary = {
      yesterday: 0,
      month: 0,
      lastMonth: 0,
      year: 0,
    };
    historyList.value.forEach((item) => {
      const timeValue = getHistoryTime(item);
      if (!timeValue) return;
      const time = new Date(timeValue);
      const usage = getUsageValue(item);
      if (Number.isNaN(time.getTime())) return;
      if (time >= yesterdayStart && time < todayStart) {
        summary.yesterday += usage;
      }
      if (time >= monthStart) {
        summary.month += usage;
      }
      if (time >= lastMonthStart && time <= lastMonthEnd) {
        summary.lastMonth += usage;
      }
      if (time >= yearStart) {
        summary.year += usage;
      }
    });
    return summary;
  });

  async function fetchHistory() {
    if (!waterDeviceStore.activeDeviceNo) {
      historyList.value = [];
      return;
    }
    const { code, data } = await WaterHistoryApi.getHistoryPage({
      pageNo: 1,
      pageSize: 100,
      deviceNo: waterDeviceStore.activeDeviceNo,
    });
    if (code !== 0) {
      historyList.value = [];
      return;
    }
    historyList.value = data.list || [];
  }

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
</script>

<style lang="scss" scoped>
  .water-home {
    .header {
      padding: 40rpx 30rpx 26rpx;
      background: linear-gradient(180deg, #4ea7ff 0%, #6fb7ff 100%);
    }

    .header-top {
      color: #ffffff;
      margin-bottom: 24rpx;
    }

    .hello {
      font-size: 32rpx;
      font-weight: 600;
    }

    .sub-hello {
      margin-top: 8rpx;
      font-size: 24rpx;
      opacity: 0.85;
    }

    .header-actions {
      gap: 16rpx;
    }

    .device-card {
      margin-top: 20rpx;
      padding: 20rpx 24rpx;
      border-radius: 16rpx;
      background: rgba(255, 255, 255, 0.2);
      color: #ffffff;
    }

    .device-info {
      flex: 1;
      margin-right: 12rpx;
    }

    .device-title {
      font-size: 22rpx;
      opacity: 0.85;
      margin-bottom: 6rpx;
    }

    .device-value {
      font-size: 28rpx;
      font-weight: 600;
    }

    .device-address {
      font-size: 22rpx;
      opacity: 0.85;
      margin-top: 6rpx;
    }

    .device-switch {
      padding: 10rpx 20rpx;
      border-radius: 999rpx;
      border: 1rpx solid rgba(255, 255, 255, 0.9);
      color: #ffffff;
      font-size: 22rpx;
      line-height: 1;
    }

    .action-circle {
      width: 56rpx;
      height: 56rpx;
      border-radius: 50%;
      background: rgba(255, 255, 255, 0.2);
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .quick-card {
      background: #ffffff;
      border-radius: 20rpx;
      padding: 20rpx 10rpx;
      display: flex;
      justify-content: space-between;
      box-shadow: 0 8rpx 20rpx rgba(0, 0, 0, 0.06);
    }

    .quick-item {
      flex: 1;
      text-align: center;
    }

    .quick-icon {
      width: 72rpx;
      height: 72rpx;
      border-radius: 36rpx;
      background: #e8f1ff;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0 auto 12rpx;
    }

    .quick-title {
      font-size: 26rpx;
      color: #333333;
    }

    .service-card {
      margin: 20rpx 30rpx 0;
      border-radius: 24rpx;
      padding: 24rpx;
      background: linear-gradient(180deg, #4a98ff 0%, #6bb1ff 100%);
      color: #ffffff;
      box-shadow: 0 10rpx 24rpx rgba(60, 126, 255, 0.25);
    }

    .service-tabs {
      font-size: 24rpx;
      margin-bottom: 20rpx;
    }

    .service-tab {
      opacity: 0.7;
    }

    .service-tab.is-active {
      font-weight: 600;
      opacity: 1;
    }

    .service-body {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20rpx;
    }

    .service-label {
      font-size: 24rpx;
      opacity: 0.85;
    }

    .service-value {
      font-size: 48rpx;
      font-weight: 700;
      margin: 8rpx 0;
    }

    .service-time {
      font-size: 22rpx;
      opacity: 0.85;
    }

    .service-action {
      padding: 12rpx 26rpx;
      border-radius: 999rpx;
      background: #ffffff;
      color: #3c7eff;
      font-size: 24rpx;
      font-weight: 600;
      box-shadow: 0 6rpx 16rpx rgba(0, 0, 0, 0.12);
    }

    .service-stats {
      display: flex;
      background: rgba(255, 255, 255, 0.2);
      border-radius: 16rpx;
      padding: 16rpx 10rpx;
      text-align: center;
    }

    .stat-item {
      flex: 1;
    }

    .stat-item + .stat-item {
      border-left: 1rpx solid rgba(255, 255, 255, 0.35);
    }

    .stat-value {
      font-size: 30rpx;
      font-weight: 600;
      margin-bottom: 6rpx;
    }

    .stat-label {
      font-size: 22rpx;
      opacity: 0.85;
    }

    .history-card {
      margin: 20rpx 30rpx 0;
      border-radius: 20rpx;
      background: #ffffff;
      padding: 24rpx;
      box-shadow: 0 8rpx 20rpx rgba(0, 0, 0, 0.06);
    }

    .history-title {
      font-size: 30rpx;
      font-weight: 600;
      color: #333333;
    }

    .history-title-row {
      margin-bottom: 20rpx;
    }

    .history-more {
      font-size: 24rpx;
      color: #3c7eff;
    }

    .history-summary {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 16rpx;
      margin-bottom: 20rpx;
    }

    .summary-item {
      border-radius: 16rpx;
      padding: 18rpx;
      color: #ffffff;
      min-height: 120rpx;
    }

    .summary-blue {
      background: linear-gradient(135deg, #2f8cff, #5bb4ff);
    }

    .summary-purple {
      background: linear-gradient(135deg, #7b6dff, #a78bfa);
    }

    .summary-green {
      background: linear-gradient(135deg, #1ecb6b, #2ed57b);
    }

    .summary-orange {
      background: linear-gradient(135deg, #ff8b3d, #ffb04a);
    }

    .summary-label {
      font-size: 24rpx;
      font-weight: 600;
      margin-bottom: 10rpx;
    }

    .summary-value {
      font-size: 22rpx;
    }

    .history-table {
      border-radius: 16rpx;
      overflow: hidden;
      border: 1rpx solid #e9f0fa;
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

    .history-empty {
      text-align: center;
      padding: 20rpx 0;
      font-size: 24rpx;
      color: #9aa5b1;
    }

    .notice-card {
      margin: 20rpx 30rpx 40rpx;
      border-radius: 20rpx;
      background: #ffffff;
      padding: 20rpx 24rpx 24rpx;
      box-shadow: 0 8rpx 20rpx rgba(0, 0, 0, 0.06);
    }

    .notice-header {
      margin-bottom: 16rpx;
      font-size: 26rpx;
      color: #333333;
    }

    .notice-title text {
      margin-left: 12rpx;
      font-weight: 600;
    }

    .notice-more {
      font-size: 24rpx;
      color: #9b9b9b;
    }

    .notice-item {
      display: flex;
      align-items: flex-start;
      margin-bottom: 20rpx;
    }

    .notice-dot {
      width: 10rpx;
      height: 10rpx;
      margin-top: 10rpx;
      border-radius: 50%;
      background: #3c7eff;
      margin-right: 16rpx;
    }

    .notice-text {
      font-size: 26rpx;
      color: #333333;
      margin-bottom: 6rpx;
    }

    .notice-time {
      font-size: 22rpx;
      color: #9b9b9b;
    }

    .notice-empty {
      font-size: 24rpx;
      color: #9b9b9b;
      text-align: center;
      padding: 16rpx 0 24rpx;
    }

    .notice-footer {
      text-align: center;
      font-size: 22rpx;
      color: #c0c0c0;
    }
  }
</style>
