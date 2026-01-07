<template>
  <s-layout title="故障报修" class="fault-report" :bgStyle="{ color: '#f4f6fb' }" navbar="inner">
    <uni-forms ref="formRef" :modelValue="state.model" :rules="rules" label-width="0">
      <view class="card device-card">
        <view class="info-row">
          <text class="label">当前设备：</text>
          <text class="value">{{ activeDeviceLabel }}</text>
        </view>
        <view v-if="activeDeviceAddress" class="info-row">
          <text class="label">设备地址：</text>
          <text class="value">{{ activeDeviceAddress }}</text>
        </view>
      </view>

      <view class="card owner-card">
        <view class="info-row">
          <text class="label">户主：</text>
          <text class="value">{{ state.profile.ownerName || '-' }}</text>
        </view>
        <view class="info-row">
          <text class="label">用水户号：</text>
          <text class="value">{{ state.profile.deviceNo || '-' }}</text>
        </view>
        <view class="info-row">
          <text class="label">地址：</text>
          <text class="value">{{ formatAddress(state.profile) || '-' }}</text>
        </view>
      </view>

      <view class="card form-card">
        <uni-forms-item name="contactMobile" class="form-item">
          <view class="field">
            <text class="label">联系电话：</text>
            <view class="field-value input">
              <uni-easyinput
                v-model="state.model.contactMobile"
                :inputBorder="false"
                placeholder="请输入联系电话"
              />
            </view>
          </view>
        </uni-forms-item>

        <uni-forms-item name="faultCode" class="form-item">
          <view class="field">
            <text class="label">故障描述：</text>
            <picker
              class="field-value picker"
              :range="state.faultOptions"
              range-key="label"
              @change="onFaultChange"
            >
              <view class="picker-display" :class="{ placeholder: !selectedFaultLabel }">
                <text>{{ selectedFaultLabel || '请选择选项' }}</text>
                <uni-icons type="right" size="16" color="#c4c4c4" />
              </view>
            </picker>
          </view>
        </uni-forms-item>

        <uni-forms-item name="feedback" class="form-item">
          <view class="field textarea">
            <text class="label">问题反馈：</text>
            <view class="field-value input">
              <uni-easyinput
                type="textarea"
                autoHeight
                :inputBorder="false"
                v-model="state.model.feedback"
                placeholder="请输入反馈信息"
              />
            </view>
          </view>
        </uni-forms-item>

        <uni-forms-item name="priority" class="form-item">
          <view class="field">
            <text class="label">优先级：</text>
            <view class="field-value radio-group">
              <radio-group @change="onPriorityChange">
                <label v-for="item in priorityOptions" :key="item.value" class="radio-item">
                  <radio :value="String(item.value)" :checked="state.model.priority === item.value" />
                  <text>{{ item.label }}</text>
                </label>
              </radio-group>
            </view>
          </view>
        </uni-forms-item>

        <uni-forms-item name="imageUrls" class="form-item">
          <view class="field">
            <text class="label">图片上传：</text>
            <view class="field-value uploader">
              <s-uploader
                v-model:url="state.model.imageUrls"
                fileMediatype="image"
                limit="3"
                mode="grid"
                :imageStyles="{ width: '168rpx', height: '168rpx' }"
              />
            </view>
          </view>
        </uni-forms-item>
      </view>
    </uni-forms>

    <view class="footer">
      <button class="outline-btn" @tap="onGoList">维修列表</button>
      <button class="primary-btn" @tap="onSubmit">信息提交</button>
    </view>
  </s-layout>
</template>

<script setup>
  import { computed, reactive, ref } from 'vue';
  import { onLoad } from '@dcloudio/uni-app';
  import sheep from '@/sheep';
  import DictApi from '@/sheep/api/system/dict';
  import WaterFaultApi from '@/sheep/api/water/fault';

  const formRef = ref(null);
  const waterDeviceStore = sheep.$store('waterDevice');
  const state = reactive({
    profile: {
      ownerName: '',
      deviceNo: '',
      areaName: '',
      communityName: '',
      buildingName: '',
      unitName: '',
      roomNo: '',
      contactMobile: '',
    },
    faultOptions: [],
    model: {
      contactMobile: '',
      faultCode: '',
      feedback: '',
      priority: 1,
      imageUrls: [],
    },
  });

  const priorityOptions = [
    { label: '低', value: 1 },
    { label: '中', value: 2 },
    { label: '高', value: 3 },
  ];

  const rules = {
    contactMobile: {
      rules: [{ required: true, errorMessage: '请输入联系电话' }],
    },
    faultCode: {
      rules: [{ required: true, errorMessage: '请选择故障描述' }],
    },
    feedback: {
      rules: [{ required: true, errorMessage: '请输入问题反馈' }],
    },
  };

  const selectedFaultLabel = computed(() => {
    return state.faultOptions.find((item) => item.value === state.model.faultCode)?.label || '';
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

  const formatAddress = (profile) => {
    return [profile.areaName, profile.communityName, profile.buildingName, profile.unitName, profile.roomNo]
      .filter(Boolean)
      .join(' ');
  };

  const onPriorityChange = (event) => {
    state.model.priority = Number(event.detail.value);
  };

  const onFaultChange = (event) => {
    const index = Number(event.detail.value);
    const selected = state.faultOptions[index];
    if (selected) {
      state.model.faultCode = selected.value;
    }
  };

  const onGoList = () => {
    sheep.$router.go('/pages/water/fault-list');
  };

  const onSubmit = async () => {
    const valid = await formRef.value?.validate().catch(() => false);
    if (!valid) return;
    const { code } = await WaterFaultApi.createFault({
      deviceNo: waterDeviceStore.activeDeviceNo,
      contactMobile: state.model.contactMobile,
      faultCode: state.model.faultCode,
      feedback: state.model.feedback,
      priority: state.model.priority,
      imageUrls: state.model.imageUrls,
    });
    if (code !== 0) return;
    uni.showToast({ title: '提交成功', icon: 'success' });
    setTimeout(() => onGoList(), 600);
  };

  const fetchFaultOptions = async () => {
    const { code, data } = await DictApi.getDictDataListByType('device-fault-code');
    if (code !== 0) return;
    state.faultOptions = data || [];
  };

  const fetchProfile = async () => {
    if (!waterDeviceStore.activeDeviceNo) {
      uni.showToast({ title: '暂无可报修设备', icon: 'none' });
      setTimeout(() => uni.navigateBack(), 800);
      return;
    }
    const { code, data } = await WaterFaultApi.getFaultInit({
      deviceNo: waterDeviceStore.activeDeviceNo,
    });
    if (code !== 0) {
      uni.showToast({ title: '暂无可报修的报装信息', icon: 'none' });
      setTimeout(() => uni.navigateBack(), 800);
      return;
    }
    state.profile = data || state.profile;
    state.model.contactMobile = data?.contactMobile || '';
  };

  onLoad(async () => {
    await waterDeviceStore.fetchDevices();
    await Promise.all([fetchFaultOptions(), fetchProfile()]);
  });
</script>

<style lang="scss" scoped>
  .fault-report {
    padding-bottom: 120rpx;

    .card {
      margin: 20rpx 24rpx;
      padding: 24rpx;
      border-radius: 20rpx;
      background: #ffffff;
      box-shadow: 0 8rpx 20rpx rgba(0, 0, 0, 0.06);
    }

    .device-card {
      background: #f4f8ff;
    }

    .info-row {
      font-size: 26rpx;
      color: #333333;
      line-height: 1.8;
      display: flex;

      .label {
        color: #666666;
        min-width: 140rpx;
      }

      .value {
        flex: 1;
        color: #222222;
      }
    }

    .form-item {
      margin-bottom: 10rpx;
    }

    .field {
      display: flex;
      align-items: center;
      font-size: 26rpx;
      color: #333333;

      &.textarea {
        align-items: flex-start;
      }

      .label {
        min-width: 140rpx;
        color: #666666;
      }

      .field-value {
        flex: 1;
      }

      .input {
        background: #f7f8fa;
        border-radius: 12rpx;
        padding: 4rpx 16rpx;
      }
    }

    .picker-display {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 16rpx 0;
      color: #333333;

      &.placeholder {
        color: #c0c0c0;
      }
    }

    .radio-group {
      display: flex;
      align-items: center;
    }

    .radio-item {
      display: flex;
      align-items: center;
      margin-right: 24rpx;
      font-size: 26rpx;
      color: #444444;
    }

    .uploader {
      padding-top: 10rpx;
    }

    .footer {
      position: fixed;
      left: 0;
      right: 0;
      bottom: 0;
      display: flex;
      gap: 20rpx;
      padding: 20rpx 24rpx calc(env(safe-area-inset-bottom) + 20rpx);
      background: #ffffff;
      box-shadow: 0 -6rpx 20rpx rgba(0, 0, 0, 0.06);
    }

    .outline-btn,
    .primary-btn {
      flex: 1;
      height: 84rpx;
      border-radius: 42rpx;
      font-size: 30rpx;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .outline-btn {
      color: #3c7eff;
      background: #eef5ff;
      border: 1px solid #3c7eff;
    }

    .primary-btn {
      color: #ffffff;
      background: linear-gradient(90deg, #3c7eff, #5aa7ff);
    }
  }
</style>
