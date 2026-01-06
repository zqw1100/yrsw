<template>
  <s-layout title="工单详情" class="work-order-detail" :bgStyle="{ color: '#f4f6fb' }" navbar="inner">
    <view class="card info-card">
      <view class="info-row">
        <text class="label">类型：</text>
        <text class="value">{{ typeLabel(order.orderType) }}</text>
      </view>
      <view class="info-row">
        <text class="label">状态：</text>
        <text class="value">{{ statusLabel(order.status) }}</text>
      </view>
      <view class="info-row">
        <text class="label">地址：</text>
        <text class="value">{{ order.address || '-' }}</text>
      </view>
      <view class="info-row">
        <text class="label">联系人：</text>
        <text class="value">{{ order.contactName || '-' }}</text>
      </view>
      <view class="info-row">
        <text class="label">电话：</text>
        <text class="value">{{ order.contactMobile || '-' }}</text>
      </view>
      <view v-if="order.feedback" class="info-row">
        <text class="label">描述：</text>
        <text class="value">{{ order.feedback }}</text>
      </view>
    </view>

    <view class="card" v-if="mode !== 'start'">
      <view class="section-title">施工前</view>
      <view class="image-list" v-if="order.beforeImageUrls?.length">
        <image v-for="(url, index) in order.beforeImageUrls" :key="index" :src="url" class="image-item" mode="aspectFill" />
      </view>
      <view class="info-row">
        <text class="label">备注：</text>
        <text class="value">{{ order.beforeRemark || '-' }}</text>
      </view>
    </view>

    <view class="card" v-if="mode === 'start'">
      <view class="section-title">施工前信息</view>
      <view class="required-tip">* 施工前图片必填</view>
      <s-uploader
        v-model:url="form.beforeImageUrls"
        fileMediatype="image"
        limit="3"
        mode="grid"
        :imageStyles="{ width: '168rpx', height: '168rpx' }"
      />
      <uni-easyinput
        v-model="form.beforeRemark"
        type="textarea"
        autoHeight
        :inputBorder="false"
        placeholder="填写施工前说明"
        class="textarea"
      />
    </view>

    <view class="card" v-if="mode !== 'finish' && mode !== 'start'">
      <view class="section-title">施工后</view>
      <view class="image-list" v-if="order.afterImageUrls?.length">
        <image v-for="(url, index) in order.afterImageUrls" :key="index" :src="url" class="image-item" mode="aspectFill" />
      </view>
      <view class="info-row">
        <text class="label">备注：</text>
        <text class="value">{{ order.afterRemark || '-' }}</text>
      </view>
    </view>

    <view class="card" v-if="mode === 'finish'">
      <view class="section-title">施工完成信息</view>
      <view class="required-tip">* 施工后图片必填</view>
      <view v-if="order.orderType === 0" class="info-row">
        <text class="label">设备号：</text>
        <view class="value device-card">
          <uni-easyinput
            v-model="form.deviceNo"
            :inputBorder="false"
            placeholder="请输入设备号"
            class="input"
          />
          <view class="device-hint">完成报装工单时请绑定设备号</view>
        </view>
      </view>
      <s-uploader
        v-model:url="form.afterImageUrls"
        fileMediatype="image"
        limit="3"
        mode="grid"
        :imageStyles="{ width: '168rpx', height: '168rpx' }"
      />
      <uni-easyinput
        v-model="form.afterRemark"
        type="textarea"
        autoHeight
        :inputBorder="false"
        placeholder="填写施工完成说明"
        class="textarea"
      />
    </view>

    <view class="footer" v-if="mode === 'start'">
      <button class="primary-btn" @tap="onStartSubmit">提交施工前信息</button>
    </view>
    <view class="footer" v-if="mode === 'finish'">
      <button class="primary-btn" @tap="onFinishSubmit">提交完成</button>
    </view>
  </s-layout>
</template>

<script setup>
  import { reactive, ref } from 'vue';
  import { onLoad } from '@dcloudio/uni-app';
  import DictApi from '@/sheep/api/system/dict';
  import WorkOrderApi from '@/sheep/api/water/work-order';

  const orderId = ref(null);
  const mode = ref('view');
  const order = reactive({});
  const form = reactive({
    beforeImageUrls: [],
    beforeRemark: '',
    afterImageUrls: [],
    afterRemark: '',
    deviceNo: '',
  });

  const statusOptions = ref([]);
  const typeOptions = ref([]);

  const statusLabel = (value) => {
    return statusOptions.value.find((item) => Number(item.value) === Number(value))?.label || '-';
  };

  const typeLabel = (value) => {
    return typeOptions.value.find((item) => Number(item.value) === Number(value))?.label || '-';
  };

  const fetchDict = async () => {
    const [{ code: statusCode, data: statusData }, { code: typeCode, data: typeData }] = await Promise.all([
      DictApi.getDictDataListByType('member_water_work_order_status'),
      DictApi.getDictDataListByType('member_water_work_order_type'),
    ]);
    if (statusCode === 0) {
      statusOptions.value = statusData || [];
    }
    if (typeCode === 0) {
      typeOptions.value = typeData || [];
    }
  };

  const fetchOrder = async () => {
    const { code, data } = await WorkOrderApi.getWorkOrder(orderId.value);
    if (code !== 0) return;
    Object.assign(order, data || {});
  };

  const onStartSubmit = async () => {
    if (!form.beforeImageUrls || form.beforeImageUrls.length === 0) {
      uni.showToast({ title: '请上传施工前图片', icon: 'none' });
      return;
    }
    await WorkOrderApi.startWorkOrder({
      id: Number(orderId.value),
      beforeImageUrls: form.beforeImageUrls,
      beforeRemark: form.beforeRemark,
    });
    uni.showToast({ title: '提交成功', icon: 'success' });
    setTimeout(() => {
      uni.navigateBack();
    }, 600);
  };

  const onFinishSubmit = async () => {
    if (!form.afterImageUrls || form.afterImageUrls.length === 0) {
      uni.showToast({ title: '请上传施工后图片', icon: 'none' });
      return;
    }
    if (order.orderType === 0 && !form.deviceNo) {
      uni.showToast({ title: '请输入设备号', icon: 'none' });
      return;
    }
    await WorkOrderApi.finishWorkOrder({
      id: Number(orderId.value),
      afterImageUrls: form.afterImageUrls,
      afterRemark: form.afterRemark,
      deviceNo: form.deviceNo,
    });
    uni.showToast({ title: '提交成功', icon: 'success' });
    setTimeout(() => {
      uni.navigateBack();
    }, 600);
  };

  onLoad(async (query) => {
    orderId.value = query.id;
    mode.value = query.mode || 'view';
    await fetchDict();
    await fetchOrder();
    form.deviceNo = order.deviceNo || '';
  });
</script>

<style lang="scss" scoped>
  .work-order-detail {
    .card {
      margin: 20rpx 24rpx;
      padding: 24rpx;
      border-radius: 20rpx;
      background: #ffffff;
      box-shadow: 0 8rpx 20rpx rgba(0, 0, 0, 0.06);
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

    .section-title {
      font-size: 28rpx;
      font-weight: 600;
      color: #333;
      margin-bottom: 16rpx;
    }

    .image-list {
      display: flex;
      flex-wrap: wrap;
      gap: 12rpx;
      margin-bottom: 16rpx;
    }

    .image-item {
      width: 160rpx;
      height: 160rpx;
      border-radius: 12rpx;
    }

    .textarea {
      margin-top: 16rpx;
      background: #f7f9fc;
      border-radius: 12rpx;
      padding: 12rpx;
    }

    .input {
      background: #f7f9fc;
      border-radius: 12rpx;
      padding: 8rpx 12rpx;
      width: 100%;
    }

    .required-tip {
      font-size: 22rpx;
      color: #ff4d4f;
      margin-bottom: 12rpx;
    }

    .device-card {
      background: #f0f6ff;
      border-radius: 16rpx;
      padding: 12rpx;
      border: 1px dashed #3c7eff;
    }

    .device-hint {
      font-size: 22rpx;
      color: #3c7eff;
      margin-top: 8rpx;
    }

    .footer {
      padding: 12rpx 24rpx 40rpx;
    }

    .primary-btn {
      background: #3c7eff;
      color: #fff;
      border-radius: 999rpx;
      padding: 12rpx 26rpx;
      font-size: 26rpx;
    }
  }
</style>
