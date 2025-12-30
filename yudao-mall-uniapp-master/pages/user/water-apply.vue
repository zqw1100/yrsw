<template>
  <s-layout title="居民报装" class="water-apply" :bgStyle="{ color: '#f4f6fb' }" navbar="inner">
    <uni-forms ref="formRef" :modelValue="state.model" :rules="rules" label-width="0">
      <view class="card">
        <view class="section-title">位置信息</view>
        <uni-forms-item name="areaName" class="form-item" @tap="state.showRegion = true">
          <view class="field">
            <text class="label">城市</text>
            <view class="field-value">
              <text :class="state.model.areaName ? 'value' : 'placeholder'">
                {{ state.model.areaName || '请选择城市' }}
              </text>
              <uni-icons type="right" size="16" color="#c4c4c4" />
            </view>
          </view>
        </uni-forms-item>
        <uni-forms-item name="communityName" class="form-item" @tap="onSelectCommunity">
          <view class="field">
            <text class="label">小区</text>
            <view class="field-value">
              <text :class="state.model.communityName ? 'value' : 'placeholder'">
                {{ state.model.communityName || '请选择小区' }}
              </text>
              <uni-icons type="right" size="16" color="#c4c4c4" />
            </view>
          </view>
        </uni-forms-item>
        <view class="field-row">
          <uni-forms-item name="buildingName" class="form-item half" @tap="onSelectBuilding">
            <view class="field">
              <text class="label">楼栋</text>
              <view class="field-value">
                <text :class="state.model.buildingName ? 'value' : 'placeholder'">
                  {{ state.model.buildingName || '请选择楼栋' }}
                </text>
                <uni-icons type="right" size="16" color="#c4c4c4" />
              </view>
            </view>
          </uni-forms-item>
          <uni-forms-item name="unitName" class="form-item half" @tap="onSelectUnit">
            <view class="field">
              <text class="label">单元</text>
              <view class="field-value">
                <text :class="state.model.unitName ? 'value' : 'placeholder'">
                  {{ state.model.unitName || '请选择单元' }}
                </text>
                <uni-icons type="right" size="16" color="#c4c4c4" />
              </view>
            </view>
          </uni-forms-item>
        </view>
        <uni-forms-item name="roomNo" class="form-item" @tap="onSelectRoom">
          <view class="field">
            <text class="label">房间号</text>
            <view class="field-value">
              <text :class="state.model.roomNo ? 'value' : 'placeholder'">
                {{ state.model.roomNo || '请选择房间号' }}
              </text>
              <uni-icons type="right" size="16" color="#c4c4c4" />
            </view>
          </view>
        </uni-forms-item>
      </view>

      <view class="card">
        <view class="section-title">联系信息</view>
        <uni-forms-item name="contactName" class="form-item">
          <view class="field">
            <text class="label">联系人</text>
            <view class="field-value input">
              <uni-easyinput
                v-model="state.model.contactName"
                :inputBorder="false"
                placeholder="请输入联系人姓名"
              />
            </view>
          </view>
        </uni-forms-item>
        <uni-forms-item name="contactMobile" class="form-item">
          <view class="field">
            <text class="label">联系方式</text>
            <view class="field-value input">
              <uni-easyinput
                v-model="state.model.contactMobile"
                type="number"
                :inputBorder="false"
                placeholder="请输入手机号"
              />
            </view>
          </view>
        </uni-forms-item>
        <uni-forms-item name="referrer" class="form-item">
          <view class="field">
            <text class="label">推荐人</text>
            <view class="field-value input">
              <uni-easyinput
                v-model="state.model.referrer"
                :inputBorder="false"
                placeholder="请输入推荐人工号"
              />
            </view>
          </view>
        </uni-forms-item>
      </view>
    </uni-forms>

    <view class="agreement">
      <checkbox-group class="checkbox" @change="onAgreementChange">
        <checkbox value="agree" :checked="state.agreementChecked" color="#3c7eff" />
        <text>我已阅读并同意</text>
      </checkbox-group>
      <text class="agreement-link" @tap="openAgreement">《开通协议》</text>
    </view>

    <view class="footer">
      <button class="ghost-btn" @tap="onMyContract">我的签约</button>
      <button class="primary-btn" @tap="onSubmit">信息提交</button>
    </view>

    <su-region-picker
      :show="state.showRegion"
      @cancel="state.showRegion = false"
      @confirm="onRegionConfirm"
    />

    <uni-popup ref="agreementPopup" type="center">
      <view class="agreement-popup">
        <view class="popup-title">开通协议</view>
        <scroll-view scroll-y class="popup-content">
          <text class="popup-text">
            为了保障供水方和用水方在本次供水服务中的权利与义务，您需仔细阅读并同意本协议后方可进行水务报装申请。
            若您已了解并认可以下内容，请点击确认继续下一步。

            一、用户基本信息
            （一）用水地址：以您选择的小区、楼栋、单元、房间号为准。
            （二）联系人信息：以您填写的联系人姓名及联系方式为准。

            二、用水安装约定
            （一）如实填写相关信息并确认后提交申请。
            （二）如房间号显示已报装，需联系水务工作人员确认。

            三、其他
            本协议最终解释权归平台所有。
          </text>
        </scroll-view>
        <button class="primary-btn" @tap="onConfirmAgreement">确认协议内容</button>
      </view>
    </uni-popup>
  </s-layout>
</template>

<script setup>
  import { reactive, ref } from 'vue';
  import { onLoad } from '@dcloudio/uni-app';
  import AreaApi from '@/sheep/api/system/area';
  import WaterHouseApi from '@/sheep/api/water/house';

  const formRef = ref(null);
  const agreementPopup = ref(null);
  const state = reactive({
    showRegion: false,
    agreementChecked: false,
    communityOptions: [],
    buildingOptions: [],
    unitOptions: [],
    model: {
      areaId: undefined,
      areaName: '',
      communityName: '',
      buildingName: '',
      unitName: '',
      roomNo: '',
      contactName: '',
      contactMobile: '',
      referrer: '',
    },
  });

  const rules = {
    areaName: {
      rules: [{ required: true, errorMessage: '请选择城市' }],
    },
    communityName: {
      rules: [{ required: true, errorMessage: '请选择小区' }],
    },
    buildingName: {
      rules: [{ required: true, errorMessage: '请选择楼栋' }],
    },
    unitName: {
      rules: [{ required: true, errorMessage: '请选择单元' }],
    },
    roomNo: {
      rules: [{ required: true, errorMessage: '请选择房间号' }],
    },
    contactName: {
      rules: [{ required: true, errorMessage: '请输入联系人' }],
    },
    contactMobile: {
      rules: [{ required: true, errorMessage: '请输入联系方式' }],
    },
  };

  const onRegionConfirm = (e) => {
    state.model.areaName = `${e.province_name} ${e.city_name} ${e.district_name}`;
    state.model.areaId = e.district_id;
    state.showRegion = false;
    resetLocation();
    fetchCommunityList();
  };

  const resetLocation = () => {
    state.model.communityName = '';
    state.model.buildingName = '';
    state.model.unitName = '';
    state.model.roomNo = '';
    state.communityOptions = [];
    state.buildingOptions = [];
    state.unitOptions = [];
  };

  const fetchCommunityList = async () => {
    if (!state.model.areaId) return;
    const { code, data } = await WaterHouseApi.getCommunityList(state.model.areaId);
    if (code === 0) {
      state.communityOptions = data || [];
    }
  };

  const fetchBuildingList = async () => {
    if (!state.model.areaId || !state.model.communityName) return;
    const { code, data } = await WaterHouseApi.getBuildingList(
      state.model.areaId,
      state.model.communityName,
    );
    if (code === 0) {
      state.buildingOptions = data || [];
    }
  };

  const fetchUnitList = async () => {
    if (!state.model.areaId || !state.model.communityName || !state.model.buildingName) return;
    const { code, data } = await WaterHouseApi.getUnitList(
      state.model.areaId,
      state.model.communityName,
      state.model.buildingName,
    );
    if (code === 0) {
      state.unitOptions = data || [];
    }
  };

  const onSelectCommunity = () => {
    if (!state.model.areaId) {
      uni.showToast({ title: '请先选择城市', icon: 'none' });
      return;
    }
    if (!state.communityOptions.length) {
      uni.showToast({ title: '暂无小区数据', icon: 'none' });
      return;
    }
    uni.showActionSheet({
      itemList: state.communityOptions,
      success: (res) => {
        state.model.communityName = state.communityOptions[res.tapIndex];
        state.model.buildingName = '';
        state.model.unitName = '';
        state.model.roomNo = '';
        state.buildingOptions = [];
        state.unitOptions = [];
        fetchBuildingList();
      },
    });
  };

  const onSelectBuilding = () => {
    if (!state.model.communityName) {
      uni.showToast({ title: '请先选择小区', icon: 'none' });
      return;
    }
    if (!state.buildingOptions.length) {
      uni.showToast({ title: '暂无楼栋数据', icon: 'none' });
      return;
    }
    uni.showActionSheet({
      itemList: state.buildingOptions,
      success: (res) => {
        state.model.buildingName = state.buildingOptions[res.tapIndex];
        state.model.unitName = '';
        state.model.roomNo = '';
        state.unitOptions = [];
        fetchUnitList();
      },
    });
  };

  const onSelectUnit = () => {
    if (!state.model.buildingName) {
      uni.showToast({ title: '请先选择楼栋', icon: 'none' });
      return;
    }
    if (!state.unitOptions.length) {
      uni.showToast({ title: '暂无单元数据', icon: 'none' });
      return;
    }
    uni.showActionSheet({
      itemList: state.unitOptions,
      success: (res) => {
        state.model.unitName = state.unitOptions[res.tapIndex];
        state.model.roomNo = '';
      },
    });
  };

  const onSelectRoom = () => {
    if (!state.model.unitName) {
      uni.showToast({ title: '请先选择单元', icon: 'none' });
      return;
    }
    const query = `areaId=${state.model.areaId}&communityName=${encodeURIComponent(
      state.model.communityName,
    )}&buildingName=${encodeURIComponent(state.model.buildingName)}&unitName=${encodeURIComponent(
      state.model.unitName,
    )}`;
    uni.navigateTo({
      url: `/pages/user/water-room-select?${query}`,
      success: (res) => {
        res.eventChannel.on('select-room', (data) => {
          state.model.roomNo = data.roomNo;
        });
      },
    });
  };

  const onAgreementChange = (e) => {
    state.agreementChecked = e.detail.value.includes('agree');
  };

  const openAgreement = () => {
    agreementPopup.value?.open();
  };

  const onSubmit = async () => {
    const valid = await formRef.value?.validate().catch(() => false);
    if (!valid) return;
    if (!state.agreementChecked) {
      uni.showToast({ title: '请先阅读并同意开通协议', icon: 'none' });
      return;
    }
    uni.showToast({ title: '已进入下一步', icon: 'none' });
  };

  const onConfirmAgreement = () => {
    agreementPopup.value?.close();
    uni.showToast({ title: '已进入下一步', icon: 'none' });
  };

  const onMyContract = () => {
    uni.showToast({ title: '功能建设中', icon: 'none' });
  };

  onLoad(() => {
    if (!uni.getStorageSync('areaData')) {
      AreaApi.getAreaTree().then((res) => {
        if (res.code === 0) {
          uni.setStorageSync('areaData', res.data);
        }
      });
    }
  });
</script>

<style lang="scss" scoped>
  .water-apply {
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
      width: 140rpx;
      font-size: 28rpx;
      color: #333333;
    }

    .field-value {
      flex: 1;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .field-value.input {
      flex: 1;
    }

    .placeholder {
      color: #c0c4cc;
      font-size: 26rpx;
    }

    .value {
      color: #333333;
      font-size: 26rpx;
    }

    .field-row {
      display: flex;
      gap: 20rpx;
    }

    .half {
      flex: 1;
    }

    .agreement {
      margin: 30rpx 30rpx 0;
      display: flex;
      align-items: center;
      font-size: 24rpx;
      color: #666666;
    }

    .checkbox {
      display: flex;
      align-items: center;
      gap: 8rpx;
    }

    .agreement-link {
      margin-left: 8rpx;
      color: #3c7eff;
    }

    .footer {
      margin: 30rpx;
      display: flex;
      gap: 20rpx;
    }

    .ghost-btn {
      flex: 1;
      height: 84rpx;
      border-radius: 42rpx;
      border: 1rpx solid #d8d8d8;
      background: #fff;
      color: #666666;
      font-size: 28rpx;
    }

    .primary-btn {
      flex: 1;
      height: 84rpx;
      border-radius: 42rpx;
      background: linear-gradient(135deg, #4b8bff, #4fc0ff);
      color: #ffffff;
      font-size: 28rpx;
    }

    .agreement-popup {
      width: 620rpx;
      background: #ffffff;
      border-radius: 24rpx;
      padding: 24rpx;
    }

    .popup-title {
      text-align: center;
      font-size: 30rpx;
      font-weight: 600;
      margin-bottom: 16rpx;
      color: #333;
    }

    .popup-content {
      max-height: 420rpx;
      margin-bottom: 20rpx;
    }

    .popup-text {
      font-size: 24rpx;
      color: #666666;
      line-height: 1.6;
      white-space: pre-line;
    }
  }
</style>
