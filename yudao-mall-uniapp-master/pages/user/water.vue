<template>
  <s-layout
      title="我的水务"
      class="water-wrap"
      :bgStyle="{ color: '#f2f5fa' }"
      navbar="inner"
      tabbar="/pages/index/user"
  >
    <view class="header">
      <view class="user-card ss-flex ss-col-center">
        <image class="avatar" :src="avatarUrl" mode="aspectFill" @tap="onEditProfile" />
        <view class="user-meta ss-flex-col ss-row-center">
          <view class="nickname">{{ displayName }}</view>
          <view v-if="displayAccount" class="account">{{ displayAccount }}</view>
        </view>
        <view class="wechat-badge">微信授权</view>
      </view>
    </view>

    <view class="card quick-card">
      <view v-for="item in quickMenus" :key="item.title" class="quick-item" @tap="item.action">
        <view class="icon-circle">
          <uni-icons :type="item.icon" size="22" color="#3c7eff" />
        </view>
        <view class="title">{{ item.title }}</view>
      </view>
    </view>

    <view class="card list-card">
      <view class="list-item" @tap="onPlaceholder">
        <view class="item-left">
          <uni-icons type="notification" size="20" color="#3c7eff" />
          <text class="item-title">消息订阅</text>
        </view>
        <uni-icons type="right" size="18" color="#bfbfbf" />
      </view>
      <view class="list-item" @tap="onPlaceholder">
        <view class="item-left">
          <uni-icons type="help" size="20" color="#3c7eff" />
          <text class="item-title">帮助中心</text>
        </view>
        <uni-icons type="right" size="18" color="#bfbfbf" />
      </view>
      <view class="list-item" @tap="onGoSetting">
        <view class="item-left">
          <uni-icons type="gear" size="20" color="#3c7eff" />
          <text class="item-title">系统设置</text>
        </view>
        <uni-icons type="right" size="18" color="#bfbfbf" />
      </view>
      <view v-if="isLogin" class="list-item" @tap="onLogout">
        <view class="item-left">
          <uni-icons type="clear" size="20" color="#3c7eff" />
          <text class="item-title">退出登录</text>
        </view>
        <uni-icons type="right" size="18" color="#bfbfbf" />
      </view>
    </view>
  </s-layout>
</template>

<script setup>
import { computed } from 'vue';
import { onShow } from '@dcloudio/uni-app';
import sheep from '@/sheep';
import AuthUtil from '@/sheep/api/member/auth';
import { showAuthModal } from '@/sheep/hooks/useModal';

const userInfo = computed(() => sheep.$store('user').userInfo);
const isLogin = computed(() => sheep.$store('user').isLogin);

const avatarUrl = computed(() =>
    isLogin.value && userInfo.value.avatar
        ? sheep.$url.cdn(userInfo.value.avatar)
        : sheep.$url.static('/static/img/shop/default_avatar.png')
);
const displayName = computed(() => userInfo.value.nickname || '未登录');
const displayAccount = computed(() => userInfo.value.mobile || '');

const quickMenus = [
  { title: '居民报装', icon: 'compose', action: () => sheep.$router.go('/pages/user/water-apply') },
  { title: '报装记录', icon: 'list', action: () => sheep.$router.go('/pages/user/water-record') },
  { title: '用水历史', icon: 'clock', action: onPlaceholder },
];

onShow(() => {
  sheep.$store('user').updateUserData();
});

function onGoSetting() {
  sheep.$router.go('/pages/public/setting');
}

function onEditProfile() {
  if (!isLogin.value) {
    showAuthModal();
    return;
  }
  sheep.$router.go('/pages/user/info');
}

function onPlaceholder() {
  uni.showToast({
    title: '功能建设中',
    icon: 'none',
  });
}

function onLogout() {
  uni.showModal({
    title: '提示',
    content: '确认退出账号？',
    success: async function (res) {
      if (!res.confirm) {
        return;
      }
      const { code } = await AuthUtil.logout();
      if (code !== 0) {
        return;
      }
      sheep.$store('user').logout();
      sheep.$router.go('/pages/index/user');
    },
  });
}
</script>

<style lang="scss" scoped>
.water-wrap {
  .header {
    padding: 40rpx 30rpx 20rpx;
    background: linear-gradient(180deg, #4ea7ff 0%, #6fb7ff 100%);
  }

  .user-card {
    padding: 24rpx;
    border-radius: 20rpx;
    background-color: #ffffff;
    box-shadow: 0 12rpx 24rpx rgba(0, 0, 0, 0.08);
  }

  .avatar {
    width: 100rpx;
    height: 100rpx;
    border-radius: 50%;
    margin-right: 24rpx;
    background-color: #f0f2f5;
  }

  .user-meta {
    flex: 1;
  }

  .nickname {
    font-size: 34rpx;
    font-weight: 600;
    color: #333333;
    margin-bottom: 8rpx;
  }

  .account {
    font-size: 24rpx;
    color: #8c8c8c;
  }

  .wechat-badge {
    font-size: 22rpx;
    color: #3c7eff;
    background: #e8f1ff;
    padding: 6rpx 16rpx;
    border-radius: 999rpx;
  }

  .card {
    margin: 20rpx 30rpx 0;
    padding: 20rpx;
    border-radius: 20rpx;
    background-color: #ffffff;
    box-shadow: 0 8rpx 20rpx rgba(0, 0, 0, 0.06);
  }

  .quick-card {
    display: flex;
    justify-content: space-between;
  }

  .quick-item {
    flex: 1;
    text-align: center;
  }

  .icon-circle {
    width: 72rpx;
    height: 72rpx;
    border-radius: 36rpx;
    background: #e8f1ff;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto 12rpx;
  }

  .title {
    font-size: 26rpx;
    color: #333333;
  }

  .list-card {
    padding: 0;
  }

  .list-item {
    padding: 26rpx 24rpx;
    display: flex;
    align-items: center;
    justify-content: space-between;
    border-bottom: 1rpx solid #f0f0f0;
  }

  .list-item:last-child {
    border-bottom: none;
  }

  .item-left {
    display: flex;
    align-items: center;
  }

  .item-title {
    font-size: 28rpx;
    color: #333333;
    margin-left: 16rpx;
  }
}
</style>
