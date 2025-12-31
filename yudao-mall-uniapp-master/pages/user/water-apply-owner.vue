<template>
  <s-layout title="资料上传" class="water-apply-owner" :bgStyle="{ color: '#f4f6fb' }" navbar="inner">
    <uni-forms ref="formRef" :modelValue="state.model" :rules="rules" label-width="0">
      <view class="card">
        <view class="section-title">户主信息</view>
        <uni-forms-item name="ownerName" class="form-item">
          <view class="field">
            <text class="label">户主</text>
            <view class="field-value input">
              <uni-easyinput v-model="state.model.ownerName" :inputBorder="false" placeholder="请输入户主姓名" />
            </view>
          </view>
        </uni-forms-item>
        <uni-forms-item name="ownerIdCard" class="form-item">
          <view class="field">
            <text class="label">户主身份证号</text>
            <view class="field-value input">
              <uni-easyinput
                v-model="state.model.ownerIdCard"
                :inputBorder="false"
                placeholder="请输入户主身份证号"
              />
            </view>
          </view>
        </uni-forms-item>
      </view>

      <view class="card">
        <view class="section-title">合同扫描件</view>
        <view class="section-desc">请上传已签署的合同扫描件或照片</view>
        <uni-forms-item name="contractImageUrls">
          <s-uploader
            v-model:url="state.model.contractImageUrls"
            fileMediatype="image"
            limit="3"
            mode="grid"
            :imageStyles="{ width: '168rpx', height: '168rpx' }"
          />
        </uni-forms-item>
      </view>

      <view class="card">
        <view class="section-title">套餐选择</view>
        <uni-forms-item name="rechargePackageId">
          <view class="package-list">
            <view
              v-for="item in state.packageList"
              :key="item.id"
              class="package-card"
              :class="{ active: state.model.rechargePackageId === item.id }"
              @tap="state.model.rechargePackageId = item.id"
            >
              <view class="package-left">
                <view class="package-name">{{ item.name }}</view>
                <view class="package-desc">
                  ¥{{ fen2yuan(item.payPrice) }} <text v-if="item.bonusPrice">· {{ formatPackageGift(item) }}</text>
                </view>
              </view>
              <view class="package-right">
                <view class="radio-dot" :class="{ checked: state.model.rechargePackageId === item.id }"></view>
              </view>
            </view>
          </view>
        </uni-forms-item>
      </view>

      <view class="card">
        <view class="section-title">备注信息</view>
        <uni-forms-item name="remark" class="form-item">
          <uni-easyinput
            type="textarea"
            :inputBorder="false"
            v-model="state.model.remark"
            maxlength="120"
            autoHeight
            placeholder="请输入备注信息（选填）"
          />
        </uni-forms-item>
      </view>
    </uni-forms>

    <view class="footer">
      <button class="primary-btn" @tap="onSubmit">提交资料</button>
    </view>
  </s-layout>
</template>

<script setup>
  import { onLoad } from '@dcloudio/uni-app';
  import { reactive, ref } from 'vue';
  import { WxaSubscribeTemplate } from '@/sheep/helper/const';
  import sheep from '@/sheep';
  import PayWalletApi from '@/sheep/api/pay/wallet';
  import WaterApplyApi from '@/sheep/api/water/apply';
  import { fen2yuan } from '@/sheep/hooks/useGoods';

  const formRef = ref(null);
  const state = reactive({
    packageList: [],
    applyDraft: null,
    model: {
      id: undefined,
      ownerName: '',
      ownerIdCard: '',
      contractImageUrls: [],
      rechargePackageId: undefined,
      remark: '',
    },
  });

  const rules = {
    ownerName: {
      rules: [{ required: true, errorMessage: '请输入户主姓名' }],
    },
    ownerIdCard: {
      rules: [{ required: true, errorMessage: '请输入户主身份证号' }],
    },
    contractImageUrls: {
      rules: [{ required: true, errorMessage: '请上传合同扫描件' }],
    },
    rechargePackageId: {
      rules: [{ required: true, errorMessage: '请选择首次充值套餐' }],
    },
  };

  const formatPackageGift = (item) => {
    if (item.bonusPrice && Number(item.bonusPrice) > 0) {
      return `赠送 ¥${fen2yuan(item.bonusPrice)}`;
    }
    return '无赠送';
  };

  const fetchPackageList = async () => {
    const { code, data } = await PayWalletApi.getWalletRechargePackageList();
    if (code === 0) {
      state.packageList = data || [];
    }
  };

  const onSubmit = async () => {
    const valid = await formRef.value?.validate().catch(() => false);
    if (!valid) return;
    let applyId = state.model.id;
    if (!applyId) {
      if (!state.applyDraft) {
        uni.showToast({ title: '缺少报装信息，请重新填写', icon: 'none' });
        return;
      }
      const { code, data } = await WaterApplyApi.createApply(state.applyDraft);
      if (code !== 0) return;
      applyId = data;
    }
    const { code } = await WaterApplyApi.completeApply({
      ...state.model,
      id: applyId,
    });
    if (code !== 0) return;
    const selectedPackage = state.packageList.find(
      (item) => Number(item.id) === Number(state.model.rechargePackageId),
    );
    if (!selectedPackage) {
      uni.showToast({ title: '充值套餐不存在', icon: 'none' });
      return;
    }
    const { code: rechargeCode, data: rechargeData } = await PayWalletApi.createWalletRecharge({
      packageId: selectedPackage.id,
      payPrice: selectedPackage.payPrice,
    });
    if (rechargeCode !== 0) return;
    uni.removeStorageSync('waterApplyDraft');
    // #ifdef MP
    sheep.$platform
      .useProvider('wechat')
      .subscribeMessage(WxaSubscribeTemplate.PAY_WALLET_RECHARGER_SUCCESS);
    // #endif
    sheep.$router.go('/pages/pay/index', {
      id: rechargeData.payOrderId,
      orderType: 'recharge',
    });
  };

  onLoad((options) => {
    if (options?.id) {
      state.model.id = Number(options.id);
    } else {
      const draft = uni.getStorageSync('waterApplyDraft');
      if (draft) {
        state.applyDraft = draft;
      }
    }
    if (!state.model.id && !state.applyDraft) {
      uni.showToast({ title: '缺少申请信息', icon: 'none' });
      setTimeout(() => uni.navigateBack(), 800);
    }
    fetchPackageList();
  });
</script>

<style lang="scss" scoped>
  .water-apply-owner {
    padding-bottom: 40rpx;

    .card {
      margin: 24rpx 30rpx 0;
      padding: 24rpx;
      border-radius: 24rpx;
      background-color: #ffffff;
      box-shadow: 0 10rpx 24rpx rgba(0, 0, 0, 0.06);
    }

    .section-title {
      font-size: 30rpx;
      font-weight: 600;
      color: #333333;
      margin-bottom: 16rpx;
    }

    .section-desc {
      font-size: 24rpx;
      color: #999999;
      margin-bottom: 16rpx;
    }

    .form-item {
      padding: 18rpx 0;
      border-bottom: 1rpx solid #f0f0f0;
    }

    .form-item:last-child {
      border-bottom: none;
    }

    .field {
      display: flex;
      align-items: center;
      justify-content: space-between;
    }

    .label {
      width: 160rpx;
      font-size: 28rpx;
      color: #333333;
    }

    .field-value.input {
      flex: 1;
    }

    .package-list {
      display: flex;
      flex-direction: column;
      gap: 16rpx;
    }

    .package-card {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 20rpx;
      border-radius: 16rpx;
      border: 1rpx solid #e8e8e8;
      background: #f9fbff;
    }

    .package-card.active {
      border-color: #4b8bff;
      background: #eef4ff;
    }

    .package-name {
      font-size: 28rpx;
      font-weight: 600;
      color: #333333;
    }

    .package-desc {
      margin-top: 6rpx;
      font-size: 24rpx;
      color: #666666;
    }

    .radio-dot {
      width: 28rpx;
      height: 28rpx;
      border-radius: 50%;
      border: 2rpx solid #c4c4c4;
    }

    .radio-dot.checked {
      border-color: #4b8bff;
      background: #4b8bff;
    }

    .footer {
      margin: 30rpx;
    }

    .primary-btn {
      width: 100%;
      height: 84rpx;
      border-radius: 42rpx;
      background: linear-gradient(135deg, #4b8bff, #4fc0ff);
      color: #ffffff;
      font-size: 28rpx;
    }
  }
</style>
