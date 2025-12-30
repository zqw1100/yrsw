<template>
  <s-layout title="选择门牌号" class="room-select" :bgStyle="{ color: '#f4f6fb' }" navbar="inner">
    <view class="header">
      <uni-search-bar
        v-model="state.keyword"
        placeholder="搜索门牌号"
        @confirm="filterRooms"
        @clear="filterRooms"
      />
      <view class="tips">共{{ availableCount }}个可选房间号</view>
    </view>

    <view class="room-grid">
      <view
        v-for="room in state.filteredRooms"
        :key="room.roomNo"
        class="room-card"
        :class="{
          selected: state.selectedRoom?.roomNo === room.roomNo,
          disabled: room.installStatus === 1,
        }"
        @tap="selectRoom(room)"
      >
        <view class="room-no">{{ room.roomNo }}</view>
        <view class="status" :class="room.installStatus === 1 ? 'status-used' : 'status-free'">
          {{ room.installStatus === 1 ? '已报装' : '可报装' }}
        </view>
      </view>
    </view>

    <view class="footer">
      <button class="ghost-btn" @tap="onCancel">取消</button>
      <button class="primary-btn" @tap="onConfirm">确认选择</button>
    </view>
  </s-layout>
</template>

<script setup>
  import { computed, reactive } from 'vue';
  import { onLoad } from '@dcloudio/uni-app';
  import WaterHouseApi from '@/sheep/api/water/house';

  const state = reactive({
    keyword: '',
    rooms: [],
    filteredRooms: [],
    selectedRoom: null,
    query: {
      areaId: undefined,
      communityName: '',
      buildingName: '',
      unitName: '',
    },
  });

  const availableCount = computed(() =>
    state.filteredRooms.filter((room) => room.installStatus !== 1).length,
  );

  const filterRooms = () => {
    if (!state.keyword) {
      state.filteredRooms = state.rooms;
      return;
    }
    state.filteredRooms = state.rooms.filter((room) => room.roomNo.includes(state.keyword));
  };

  const selectRoom = (room) => {
    if (room.installStatus === 1) {
      return;
    }
    state.selectedRoom = room;
  };

  const onCancel = () => {
    uni.navigateBack();
  };

  const onConfirm = () => {
    if (!state.selectedRoom) {
      uni.showToast({ title: '请选择房间号', icon: 'none' });
      return;
    }
    const eventChannel = getCurrentPages()[getCurrentPages().length - 1]?.getOpenerEventChannel();
    eventChannel?.emit('select-room', state.selectedRoom);
    uni.navigateBack();
  };

  const fetchRooms = async () => {
    const { code, data } = await WaterHouseApi.getRoomList(state.query);
    if (code === 0) {
      state.rooms = data || [];
      state.filteredRooms = state.rooms;
    }
  };

  onLoad((options) => {
    state.query = {
      areaId: Number(options.areaId),
      communityName: decodeURIComponent(options.communityName || ''),
      buildingName: decodeURIComponent(options.buildingName || ''),
      unitName: decodeURIComponent(options.unitName || ''),
    };
    fetchRooms();
  });
</script>

<style lang="scss" scoped>
  .room-select {
    padding-bottom: 40rpx;

    .header {
      margin: 20rpx 30rpx 0;
      background: #ffffff;
      border-radius: 24rpx;
      padding: 20rpx 20rpx 10rpx;
      box-shadow: 0 10rpx 24rpx rgba(0, 0, 0, 0.06);
    }

    .tips {
      font-size: 24rpx;
      color: #8c8c8c;
      padding: 0 10rpx 10rpx;
    }

    .room-grid {
      margin: 20rpx 30rpx 0;
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 16rpx;
    }

    .room-card {
      padding: 20rpx 0;
      border-radius: 18rpx;
      background: #ffffff;
      text-align: center;
      box-shadow: 0 8rpx 18rpx rgba(0, 0, 0, 0.05);
      border: 2rpx solid transparent;
    }

    .room-card.selected {
      border-color: #5a8bff;
      background: linear-gradient(180deg, #e9f1ff, #ffffff);
    }

    .room-card.disabled {
      opacity: 0.5;
    }

    .room-no {
      font-size: 28rpx;
      font-weight: 600;
      color: #333333;
      margin-bottom: 8rpx;
    }

    .status {
      display: inline-block;
      padding: 4rpx 12rpx;
      border-radius: 999rpx;
      font-size: 22rpx;
    }

    .status-free {
      background: #e9f7ef;
      color: #3db778;
    }

    .status-used {
      background: #f4f4f5;
      color: #999999;
    }

    .footer {
      margin: 30rpx;
      display: flex;
      gap: 20rpx;
    }

    .ghost-btn {
      flex: 1;
      height: 80rpx;
      border-radius: 40rpx;
      border: 1rpx solid #d8d8d8;
      background: #fff;
      color: #666666;
      font-size: 28rpx;
    }

    .primary-btn {
      flex: 1;
      height: 80rpx;
      border-radius: 40rpx;
      background: linear-gradient(135deg, #4b8bff, #4fc0ff);
      color: #ffffff;
      font-size: 28rpx;
    }
  }
</style>
