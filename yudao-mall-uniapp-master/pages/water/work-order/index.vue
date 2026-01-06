<template>
  <s-layout title="工单管理" class="work-order" :bgStyle="{ color: '#f4f6fb' }" navbar="inner">
    <view class="tabs">
      <view
        v-for="item in statusOptions"
        :key="item.value"
        class="tab-item"
        :class="{ active: state.query.status === Number(item.value) }"
        @tap="onStatusChange(Number(item.value))"
      >
        {{ item.label }}
      </view>
    </view>

    <view class="filters">
      <picker
        class="type-picker"
        :range="typeOptions"
        range-key="label"
        @change="onTypeChange"
      >
        <view class="picker-display">
          <text>{{ selectedTypeLabel }}</text>
          <uni-icons type="down" size="14" color="#999" />
        </view>
      </picker>
    </view>

    <s-empty v-if="state.pagination.total === 0" text="暂无工单" icon="/static/data-empty.png" />
    <view v-else class="order-list">
      <view v-for="item in state.pagination.list" :key="item.id" class="order-card">
        <view class="card-header ss-flex ss-row-between ss-col-center">
          <view class="title">{{ typeLabel(item.orderType) }}</view>
          <view class="status-pill" :class="statusClass(item.status)">{{ statusLabel(item.status) }}</view>
        </view>
        <view class="info-text">地址：{{ item.address || '-' }}</view>
        <view class="info-text">联系人：{{ item.contactName || '-' }}</view>
        <view class="info-text">联系方式：{{ item.contactMobile || '-' }}</view>
        <view v-if="item.feedback" class="info-text">问题描述：{{ item.feedback }}</view>
        <view v-if="item.workerName" class="info-text">施工人员：{{ item.workerName }}</view>

        <view class="actions">
          <button
            v-if="isManager && !item.workerId"
            class="primary-btn"
            @tap="onAssign(item)"
          >
            分配施工人员
          </button>
          <button
            v-if="isManager && item.workerId && item.status === 0"
            class="outline-btn"
            @tap="onRevoke(item)"
          >
            撤回指派
          </button>
          <button
            v-if="isWorker && item.status === 0"
            class="primary-btn"
            @tap="onAccept(item)"
          >
            接收
          </button>
          <button
            v-if="isWorker && item.status === 1"
            class="primary-btn"
            @tap="onStart(item)"
          >
            施工前
          </button>
          <button
            v-if="isWorker && item.status === 2"
            class="primary-btn"
            @tap="onFinish(item)"
          >
            施工完成
          </button>
          <button v-if="item.status === 3" class="outline-btn" @tap="onDetail(item)">查看</button>
        </view>
      </view>
    </view>

    <uni-load-more
      v-if="state.pagination.total > 0"
      :status="state.loadStatus"
      :content-text="{ contentdown: '上拉加载更多' }"
    />
  </s-layout>
</template>

<script setup>
  import { computed, reactive } from 'vue';
  import { onLoad, onReachBottom, onShow } from '@dcloudio/uni-app';
  import { concat } from 'lodash-es';
  import sheep from '@/sheep';
  import DictApi from '@/sheep/api/system/dict';
  import WorkOrderApi from '@/sheep/api/water/work-order';

  const userInfo = computed(() => sheep.$store('user').userInfo);
  const isManager = computed(() => userInfo.value?.groupId === 2);
  const isWorker = computed(() => userInfo.value?.groupId === 1);

  const state = reactive({
    pagination: {
      list: [],
      total: 0,
      pageNo: 1,
      pageSize: 10,
    },
    loadStatus: '',
    query: {
      status: 0,
      orderType: undefined,
    },
  });

  const statusOptions = reactive([]);
  const typeOptions = reactive([{ label: '全部类型', value: '' }]);

  const statusLabel = (value) => {
    return statusOptions.find((item) => Number(item.value) === Number(value))?.label || '-';
  };

  const statusClass = (value) => `status-${value ?? 'default'}`;

  const typeLabel = (value) => {
    return typeOptions.find((item) => Number(item.value) === Number(value))?.label || '-';
  };

  const selectedTypeLabel = computed(() => {
    if (state.query.orderType === undefined || state.query.orderType === '') {
      return '全部类型';
    }
    return typeLabel(state.query.orderType);
  });

  const fetchDict = async () => {
    const [{ code: statusCode, data: statusData }, { code: typeCode, data: typeData }] = await Promise.all([
      DictApi.getDictDataListByType('member_water_work_order_status'),
      DictApi.getDictDataListByType('member_water_work_order_type'),
    ]);
    if (statusCode === 0) {
      statusOptions.splice(0, statusOptions.length, ...(statusData || []));
    }
    if (typeCode === 0) {
      typeOptions.splice(1, typeOptions.length, ...(typeData || []));
    }
  };

  const fetchList = async () => {
    state.loadStatus = 'loading';
    const { code, data } = await WorkOrderApi.getWorkOrderPage({
      pageNo: state.pagination.pageNo,
      pageSize: state.pagination.pageSize,
      status: state.query.status,
      orderType: state.query.orderType || undefined,
    });
    if (code !== 0) {
      state.loadStatus = '';
      return;
    }
    state.pagination.list = concat(state.pagination.list, data.list || []);
    state.pagination.total = data.total || 0;
    state.loadStatus = state.pagination.list.length < state.pagination.total ? 'more' : 'noMore';
  };

  const resetList = async () => {
    state.pagination.list = [];
    state.pagination.pageNo = 1;
    await fetchList();
  };

  const onStatusChange = async (value) => {
    state.query.status = value;
    await resetList();
  };

  const onTypeChange = async (event) => {
    const index = Number(event.detail.value);
    const selected = typeOptions[index];
    state.query.orderType = selected?.value === '' ? undefined : Number(selected.value);
    await resetList();
  };

  const onAssign = (item) => {
    sheep.$router.go(`/pages/water/work-order/assign?id=${item.id}`);
  };

  const onRevoke = async (item) => {
    await WorkOrderApi.revokeWorkOrder({ id: item.id });
    await resetList();
  };

  const onAccept = async (item) => {
    await WorkOrderApi.acceptWorkOrder({ id: item.id });
    await resetList();
  };

  const onStart = (item) => {
    sheep.$router.go(`/pages/water/work-order/detail?id=${item.id}&mode=start`);
  };

  const onFinish = (item) => {
    sheep.$router.go(`/pages/water/work-order/detail?id=${item.id}&mode=finish`);
  };

  const onDetail = (item) => {
    sheep.$router.go(`/pages/water/work-order/detail?id=${item.id}&mode=view`);
  };

  onLoad(async () => {
    await fetchDict();
    await resetList();
  });

  onShow(async () => {
    if (statusOptions.length === 0) {
      await fetchDict();
    }
    await resetList();
  });

  onReachBottom(() => {
    if (state.loadStatus === 'noMore') {
      return;
    }
    state.pagination.pageNo++;
    fetchList();
  });
</script>

<style lang="scss" scoped>
  .work-order {
    .tabs {
      display: flex;
      padding: 16rpx 24rpx 0;
      gap: 16rpx;
      flex-wrap: wrap;
    }

    .tab-item {
      padding: 10rpx 24rpx;
      border-radius: 999rpx;
      background: #ffffff;
      font-size: 24rpx;
      color: #666;

      &.active {
        background: #3c7eff;
        color: #fff;
      }
    }

    .filters {
      padding: 16rpx 24rpx;
    }

    .type-picker {
      background: #ffffff;
      border-radius: 16rpx;
      padding: 16rpx 20rpx;
    }

    .picker-display {
      display: flex;
      align-items: center;
      justify-content: space-between;
      font-size: 24rpx;
      color: #333;
    }

    .order-list {
      padding: 0 24rpx 40rpx;
    }

    .order-card {
      background: #ffffff;
      border-radius: 20rpx;
      padding: 24rpx;
      margin-bottom: 20rpx;
      box-shadow: 0 8rpx 20rpx rgba(0, 0, 0, 0.06);
    }

    .card-header {
      margin-bottom: 16rpx;
    }

    .title {
      font-size: 28rpx;
      font-weight: 600;
      color: #333;
    }

    .status-pill {
      font-size: 22rpx;
      padding: 6rpx 16rpx;
      border-radius: 999rpx;
    }

    .status-0 {
      color: #fa8c16;
      background: #fff7e6;
    }

    .status-1 {
      color: #1677ff;
      background: #e6f4ff;
    }

    .status-2 {
      color: #2f54eb;
      background: #f0f5ff;
    }

    .status-3 {
      color: #52c41a;
      background: #f6ffed;
    }

    .info-text {
      font-size: 24rpx;
      color: #555;
      line-height: 1.6;
      margin-bottom: 8rpx;
    }

    .actions {
      display: flex;
      flex-wrap: wrap;
      gap: 16rpx;
      margin-top: 16rpx;
    }

    .primary-btn {
      background: #3c7eff;
      color: #fff;
      border-radius: 999rpx;
      padding: 10rpx 26rpx;
      font-size: 24rpx;
    }

    .outline-btn {
      border: 1px solid #3c7eff;
      color: #3c7eff;
      border-radius: 999rpx;
      padding: 10rpx 26rpx;
      font-size: 24rpx;
      background: #fff;
    }
  }
</style>
