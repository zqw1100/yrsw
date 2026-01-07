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
    { title: '用水历史', icon: 'calendar', action: onPlaceholder },
    { title: '消息通知', icon: 'notification', action: onGoNotice },
    { title: '在线缴费', icon: 'wallet', action: onPayRecharge },
  ];

  onShow(async () => {
    sheep.$store('user').updateUserData();
    await waterDeviceStore.fetchDevices();
    await waterDeviceStore.fetchWallet();
    await getLatestNotice();
  });

  function onPlaceholder() {
    uni.showToast({
      title: '功能建设中',
      icon: 'none',
    });
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
