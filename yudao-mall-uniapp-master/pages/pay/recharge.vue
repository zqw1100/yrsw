<!-- 充值界面 -->
<template>
  <s-layout title="充值" class="withdraw-wrap" navbar="inner">
    <view
      class="wallet-num-box ss-flex ss-col-center ss-row-between"
      :style="[
        {
          marginTop: '-' + Number(statusBarHeight + 88) + 'rpx',
          paddingTop: Number(statusBarHeight + 108) + 'rpx',
        },
      ]"
    >
      <view class="">
        <view class="num-title">当前余额（元）</view>
        <view class="wallet-num">{{ fen2yuan(deviceWallet.balance) }}</view>
      </view>
      <button class="ss-reset-button log-btn" @tap="sheep.$router.go('/pages/pay/recharge-log')">
        充值记录
      </button>
    </view>
    <view class="recharge-box">
      <view class="device-box">
        <view class="device-title">当前设备</view>
        <view class="device-value">{{ activeDeviceLabel }}</view>
        <view v-if="activeDeviceAddress" class="device-address">{{ activeDeviceAddress }}</view>
      </view>
      <view class="recharge-card-box">
        <view class="recharge-tabs ss-flex">
          <button
            class="ss-reset-button tab-btn"
            :class="{ active: state.rechargeTab === 'package' }"
            @tap="onSwitchTab('package')"
          >
            套餐充值
          </button>
          <button
            class="ss-reset-button tab-btn"
            :class="{ active: state.rechargeTab === 'custom' }"
            @tap="onSwitchTab('custom')"
          >
            自定义充值
          </button>
        </view>
        <template v-if="state.rechargeTab === 'custom'">
          <view class="input-label ss-m-b-50">自定义金额</view>
          <view class="input-box ss-flex border-bottom ss-p-b-20">
            <view class="unit">￥</view>
            <uni-easyinput
              v-model="state.recharge_money"
              type="digit"
              placeholder="请输入充值金额"
              :inputBorder="false"
            />
          </view>
        </template>
        <template v-else>
          <view class="input-label ss-m-b-50">套餐金额</view>
          <view class="face-value-box ss-flex ss-flex-wrap ss-m-y-40">
            <button
              class="ss-reset-button face-value-btn"
              v-for="item in state.packageList"
              :key="item.money"
              :class="[{ 'btn-active': state.recharge_money === fen2yuan(item.payPrice) }]"
              @tap="onCard(item.payPrice)"
            >
              <text class="face-value-title">{{ fen2yuan(item.payPrice) }}</text>
              <view v-if="item.bonusPrice" class="face-value-tag">
                送 {{ fen2yuan(item.bonusPrice) }} 元
              </view>
            </button>
          </view>
        </template>
        <button class="ss-reset-button save-btn ss-m-t-60" @tap="onConfirm">
          确认充值
        </button>
      </view>
    </view>
  </s-layout>
</template>

<script setup>
  import { computed, reactive } from 'vue';
  import sheep from '@/sheep';
  import { onLoad, onShow } from '@dcloudio/uni-app';
  import { fen2yuan } from '@/sheep/hooks/useGoods';
  import PayWalletApi from '@/sheep/api/pay/wallet';
  import { WxaSubscribeTemplate } from '@/sheep/helper/const';

  const waterDeviceStore = sheep.$store('waterDevice');
  const deviceWallet = computed(() => waterDeviceStore.deviceWallet);
  const statusBarHeight = sheep.$platform.device.statusBarHeight * 2;
  const state = reactive({
    recharge_money: '', // 输入的充值金额
    packageList: [],
    rechargeTab: 'package',
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

  // 点击卡片，选择充值金额
  function onCard(e) {
    state.recharge_money = fen2yuan(e);
  }

  function onSwitchTab(tab) {
    if (state.rechargeTab === tab) {
      return;
    }
    state.rechargeTab = tab;
    state.recharge_money = '';
  }

  // 获得钱包充值套餐列表
  async function getRechargeTabs() {
    const { code, data } = await PayWalletApi.getWalletRechargePackageList();
    if (code !== 0) {
      return;
    }
    state.packageList = data;
  }

  // 发起支付
  async function onConfirm() {
    if (!waterDeviceStore.activeDeviceNo) {
      uni.showToast({ title: '暂无可充值设备', icon: 'none' });
      return;
    }
    if (state.rechargeTab === 'package' && !state.recharge_money) {
      uni.showToast({ title: '请选择套餐金额', icon: 'none' });
      return;
    }
    if (state.rechargeTab === 'custom' && !state.recharge_money) {
      uni.showToast({ title: '请输入充值金额', icon: 'none' });
      return;
    }
    const { code, data } = await PayWalletApi.createWalletRecharge({
      packageId: state.packageList.find((item) => fen2yuan(item.payPrice) === state.recharge_money)
        ?.id,
      payPrice: state.recharge_money * 100,
      deviceNo: waterDeviceStore.activeDeviceNo,
    });
    if (code !== 0) {
      return;
    }
    // #ifdef MP
    sheep.$platform
      .useProvider('wechat')
      .subscribeMessage(WxaSubscribeTemplate.PAY_WALLET_RECHARGER_SUCCESS);
    // #endif
    sheep.$router.go('/pages/pay/index', {
      id: data.payOrderId,
      orderType: 'recharge',
    });
  }

  onLoad(() => {
    waterDeviceStore.fetchDevices();
    waterDeviceStore.fetchWallet();
    getRechargeTabs();
  });

  onShow(() => {
    waterDeviceStore.fetchWallet();
  });
</script>

<style lang="scss" scoped>
  :deep() {
    .uni-input-input {
      font-family: OPPOSANS !important;
    }
  }

  .wallet-num-box {
    padding: 0 40rpx 80rpx;
    background: linear-gradient(180deg, #4ea7ff 0%, #6fb7ff 100%);
    border-radius: 0 0 5% 5%;

    .num-title {
      font-size: 26rpx;
      font-weight: 500;
      color: $white;
      margin-bottom: 20rpx;
    }

    .wallet-num {
      font-size: 60rpx;
      font-weight: 500;
      color: $white;
      font-family: OPPOSANS;
    }

    .log-btn {
      width: 170rpx;
      height: 60rpx;
      line-height: 60rpx;
      border: 1rpx solid $white;
      border-radius: 30rpx;
      padding: 0;
      font-size: 26rpx;
      font-weight: 500;
      color: $white;
    }
  }

  .recharge-box {
    position: relative;
    padding: 0 30rpx;
    margin-top: -60rpx;
  }

  .device-box {
    background: #ffffff;
    border-radius: 20rpx;
    padding: 20rpx 24rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 8rpx 20rpx rgba(0, 0, 0, 0.06);
  }

  .device-title {
    font-size: 24rpx;
    color: #8c8c8c;
    margin-bottom: 6rpx;
  }

  .device-value {
    font-size: 30rpx;
    font-weight: 600;
    color: #333333;
  }

  .device-address {
    font-size: 24rpx;
    color: #666666;
    margin-top: 6rpx;
  }

  .save-btn {
    width: 620rpx;
    height: 86rpx;
    border-radius: 44rpx;
    font-size: 30rpx;
    background: linear-gradient(90deg, #3c7eff 0%, #6bb1ff 100%);
    color: $white;
  }

  .recharge-card-box {
    width: 690rpx;
    background: var(--ui-BG);
    border-radius: 20rpx;
    padding: 30rpx;
    box-sizing: border-box;

    .input-label {
      font-size: 30rpx;
      font-weight: 500;
      color: #333;
    }

    .recharge-tabs {
      margin-bottom: 30rpx;
      background: #f5f7fb;
      border-radius: 16rpx;
      padding: 6rpx;
    }

    .tab-btn {
      flex: 1;
      height: 68rpx;
      line-height: 68rpx;
      border-radius: 12rpx;
      font-size: 28rpx;
      color: #8a8a8a;
    }

    .tab-btn.active {
      background: #ffffff;
      color: #3c7eff;
      font-weight: 600;
      box-shadow: 0 6rpx 16rpx rgba(60, 126, 255, 0.15);
    }

    .unit {
      display: flex;
      align-items: center;
      font-size: 48rpx;
      font-weight: 500;
    }

    .uni-easyinput__placeholder-class {
      font-size: 30rpx;
      height: 60rpx;
      display: flex;
      align-items: center;
    }

    :deep(.uni-easyinput__content-input) {
      font-size: 48rpx;
    }

    .face-value-btn {
      width: 200rpx;
      height: 144rpx;
      border: 1px solid #3c7eff;
      border-radius: 10rpx;
      position: relative;
      z-index: 1;
      margin-bottom: 15rpx;
      margin-right: 15rpx;

      &:nth-of-type(3n) {
        margin-right: 0;
      }

      .face-value-title {
        font-size: 36rpx;
        font-weight: 500;
        color: #3c7eff;
        font-family: OPPOSANS;

        &::after {
          content: '元';
          font-size: 24rpx;
          margin-left: 6rpx;
        }
      }

      .face-value-tag {
        position: absolute;
        z-index: 2;
        height: 40rpx;
        line-height: 40rpx;
        background: #3c7eff;
        opacity: 0.8;
        border-radius: 10rpx 0 20rpx 0;
        top: 0;
        left: -2rpx;
        padding: 0 16rpx;
        font-size: 22rpx;
        color: $white;
        font-family: OPPOSANS;
      }

      &::before {
        position: absolute;
        content: ' ';
        width: 100%;
        height: 100%;
        background: #3c7eff;
        opacity: 0.1;
        z-index: 0;
        left: 0;
        top: 0;
      }
    }

    .btn-active {
      z-index: 1;

      &::before {
        content: '';
        background: #3c7eff;
        opacity: 1;
      }

      .face-value-title {
        color: $white;
        position: relative;
        z-index: 1;
        font-family: OPPOSANS;
      }

      .face-value-tag {
        background: $white;
        color: #3c7eff;
        font-family: OPPOSANS;
      }
    }
  }
</style>
