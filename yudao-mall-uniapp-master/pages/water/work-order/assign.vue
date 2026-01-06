<template>
  <s-layout title="选择人员" class="work-order-assign" :bgStyle="{ color: '#f4f6fb' }" navbar="inner">
    <s-empty v-if="workers.length === 0" text="暂无施工人员" icon="/static/data-empty.png" />
    <view v-else class="worker-list">
      <view v-for="worker in workers" :key="worker.id" class="worker-card">
        <view class="worker-info">
          <view class="name">{{ worker.nickname || '未命名' }}</view>
          <view class="mobile">{{ worker.mobile || '-' }}</view>
        </view>
        <button class="primary-btn" @tap="onSelect(worker)">选择</button>
      </view>
    </view>
  </s-layout>
</template>

<script setup>
  import { ref } from 'vue';
  import { onLoad } from '@dcloudio/uni-app';
  import sheep from '@/sheep';
  import UserApi from '@/sheep/api/member/user';
  import WorkOrderApi from '@/sheep/api/water/work-order';

  const orderId = ref(null);
  const workers = ref([]);

  const fetchWorkers = async () => {
    const { code, data } = await UserApi.getUserListByGroupId(1);
    if (code !== 0) {
      return;
    }
    workers.value = data || [];
  };

  const onSelect = async (worker) => {
    await WorkOrderApi.assignWorkOrder({ id: Number(orderId.value), workerId: worker.id });
    uni.showToast({ title: '指派成功', icon: 'success' });
    setTimeout(() => sheep.$router.back(), 600);
  };

  onLoad((query) => {
    orderId.value = query.id;
    fetchWorkers();
  });
</script>

<style lang="scss" scoped>
  .work-order-assign {
    .worker-list {
      padding: 20rpx 24rpx 40rpx;
    }

    .worker-card {
      background: #ffffff;
      border-radius: 20rpx;
      padding: 24rpx;
      margin-bottom: 20rpx;
      display: flex;
      align-items: center;
      justify-content: space-between;
      box-shadow: 0 8rpx 20rpx rgba(0, 0, 0, 0.06);
    }

    .worker-info {
      display: flex;
      flex-direction: column;
      gap: 8rpx;
    }

    .name {
      font-size: 28rpx;
      font-weight: 600;
      color: #333;
    }

    .mobile {
      font-size: 24rpx;
      color: #666;
    }

    .primary-btn {
      background: #3c7eff;
      color: #fff;
      border-radius: 999rpx;
      padding: 8rpx 24rpx;
      font-size: 24rpx;
    }
  }
</style>
