<template>
  <s-layout title="消息通知" class="water-notice" :bgStyle="{ color: '#f4f6fb' }" navbar="inner">
    <s-empty v-if="state.pagination.total === 0 && !state.loading" text="暂无消息通知" icon="/static/data-empty.png" />
    <view v-else class="notice-list">
      <view v-for="item in state.pagination.list" :key="item.id" class="notice-card" @tap="onOpenNotice(item)">
        <view class="card-header ss-flex ss-col-center ss-row-between">
          <view class="title ss-line-2">{{ item.title }}</view>
          <view class="time">{{ formatTime(item.createTime) }}</view>
        </view>
        <view v-if="item.introduction" class="intro ss-line-2">
          {{ item.introduction }}
        </view>
        <view v-if="item.picUrl" class="cover">
          <image class="cover-image" :src="coverUrl(item.picUrl)" mode="aspectFill" />
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
  import { reactive } from 'vue';
  import { onLoad, onReachBottom } from '@dcloudio/uni-app';
  import { concat } from 'lodash-es';
  import sheep from '@/sheep';
  import ArticleApi from '@/sheep/api/promotion/article';

  const NOTICE_CATEGORY_ID = 4;

  const state = reactive({
    pagination: {
      list: [],
      total: 0,
      pageNo: 1,
      pageSize: 10,
    },
    loadStatus: '',
    loading: true,
  });

  const formatTime = (time) => {
    return time ? sheep.$helper.timeFormat(time, 'yyyy-mm-dd hh:MM') : '';
  };

  const coverUrl = (url) => sheep.$url.cdn(url);

  const onOpenNotice = (item) => {
    sheep.$router.go('/pages/public/richtext', {
      id: item.id,
      title: item.title,
    });
  };

  async function getNoticeList() {
    state.loadStatus = 'loading';
    const { code, data } = await ArticleApi.getArticlePage({
      pageNo: state.pagination.pageNo,
      pageSize: state.pagination.pageSize,
      categoryId: NOTICE_CATEGORY_ID,
    });
    if (code !== 0) {
      state.loadStatus = '';
      state.loading = false;
      return;
    }
    state.pagination.list = concat(state.pagination.list, data.list || []);
    state.pagination.total = data.total || 0;
    state.loadStatus = state.pagination.list.length < state.pagination.total ? 'more' : 'noMore';
    state.loading = false;
  }

  onLoad(() => {
    getNoticeList();
  });

  onReachBottom(() => {
    if (state.loadStatus === 'noMore') {
      return;
    }
    state.pagination.pageNo++;
    getNoticeList();
  });
</script>

<style lang="scss" scoped>
  .water-notice {
    .notice-list {
      padding: 20rpx 24rpx 40rpx;
    }

    .notice-card {
      background: #ffffff;
      border-radius: 20rpx;
      padding: 24rpx;
      margin-bottom: 20rpx;
      box-shadow: 0 8rpx 20rpx rgba(0, 0, 0, 0.06);
    }

    .card-header {
      margin-bottom: 12rpx;
    }

    .title {
      flex: 1;
      font-size: 28rpx;
      font-weight: 600;
      color: #333333;
      margin-right: 16rpx;
      line-height: 1.4;
    }

    .time {
      font-size: 22rpx;
      color: #8c8c8c;
      white-space: nowrap;
    }

    .intro {
      font-size: 24rpx;
      color: #666666;
      line-height: 1.6;
    }

    .cover {
      margin-top: 16rpx;
    }

    .cover-image {
      width: 100%;
      height: 260rpx;
      border-radius: 16rpx;
    }
  }
</style>
