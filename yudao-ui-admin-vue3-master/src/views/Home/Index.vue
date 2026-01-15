<template>
  <div class="water-dashboard">
    <el-card shadow="never" class="mb-12px">
      <div class="flex flex-wrap items-center justify-between gap-12px">
        <div>
          <div class="text-18px font-600">用户用水数据总览</div>
          <div class="mt-4px text-12px text-gray-500">更新时间：{{ refreshTime }}</div>
        </div>
        <el-button type="primary" :loading="loading" @click="fetchDashboard">刷新数据</el-button>
      </div>
    </el-card>

    <el-row :gutter="12" class="mb-12px">
      <el-col :xl="6" :lg="6" :md="12" :sm="12" :xs="24">
        <el-card shadow="never">
          <div class="text-12px text-gray-500">累计充值金额（元）</div>
          <div class="mt-8px text-24px font-600">{{ formatAmount(summary.totalRechargeAmount) }}</div>
          <div class="mt-6px text-12px text-gray-400">
            赠送金额：{{ formatAmount(summary.totalRechargeBonusAmount) }} 元
          </div>
        </el-card>
      </el-col>
      <el-col :xl="6" :lg="6" :md="12" :sm="12" :xs="24">
        <el-card shadow="never">
          <div class="text-12px text-gray-500">累计用水量（吨）</div>
          <div class="mt-8px text-24px font-600">{{ formatUsage(summary.totalUsage) }}</div>
          <div class="mt-6px text-12px text-gray-400">共计 {{ summary.totalUsage }} 升</div>
        </el-card>
      </el-col>
      <el-col :xl="6" :lg="6" :md="12" :sm="12" :xs="24">
        <el-card shadow="never">
          <div class="text-12px text-gray-500">累计水费（元）</div>
          <div class="mt-8px text-24px font-600">{{ formatAmount(summary.totalFeeAmount) }}</div>
          <div class="mt-6px text-12px text-gray-400">充值订单 {{ summary.rechargeCount }} 笔</div>
        </el-card>
      </el-col>
      <el-col :xl="6" :lg="6" :md="12" :sm="12" :xs="24">
        <el-card shadow="never">
          <div class="text-12px text-gray-500">在用设备数</div>
          <div class="mt-8px text-24px font-600">{{ summary.deviceCount }}</div>
          <div class="mt-6px text-12px text-gray-400">
            低余额设备 {{ summary.lowBalanceDeviceCount }} 台
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="12" class="mb-12px">
      <el-col :xl="6" :lg="6" :md="12" :sm="12" :xs="24">
        <el-card shadow="never">
          <div class="text-12px text-gray-500">待处理报装</div>
          <div class="mt-8px text-24px font-600">{{ summary.pendingApplyCount }}</div>
          <div class="mt-6px text-12px text-gray-400">需要分派施工工单</div>
        </el-card>
      </el-col>
      <el-col :xl="6" :lg="6" :md="12" :sm="12" :xs="24">
        <el-card shadow="never">
          <div class="text-12px text-gray-500">待处理故障</div>
          <div class="mt-8px text-24px font-600">{{ summary.pendingFaultCount }}</div>
          <div class="mt-6px text-12px text-gray-400">重点关注高优先级工单</div>
        </el-card>
      </el-col>
      <el-col :xl="6" :lg="6" :md="12" :sm="12" :xs="24">
        <el-card shadow="never">
          <div class="text-12px text-gray-500">近 7 日平均用水（吨）</div>
          <div class="mt-8px text-24px font-600">{{ formatUsage(averageDailyUsage) }}</div>
          <div class="mt-6px text-12px text-gray-400">基于近 7 日统计</div>
        </el-card>
      </el-col>
      <el-col :xl="6" :lg="6" :md="12" :sm="12" :xs="24">
        <el-card shadow="never">
          <div class="text-12px text-gray-500">近 7 日充值总额（元）</div>
          <div class="mt-8px text-24px font-600">{{ formatAmount(recentRechargeAmount) }}</div>
          <div class="mt-6px text-12px text-gray-400">含赠送金额</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="12" class="mb-12px">
      <el-col :xl="24" :lg="24" :md="24" :sm="24" :xs="24">
        <el-card shadow="never">
          <el-skeleton :loading="loading" animated>
            <Echart :options="dailyLineOptionsData" :height="320" />
          </el-skeleton>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="12">
      <el-col :xl="16" :lg="16" :md="24" :sm="24" :xs="24">
        <el-card shadow="never">
          <el-skeleton :loading="loading" animated>
            <Echart :options="monthlyBarOptionsData" :height="320" />
          </el-skeleton>
        </el-card>
      </el-col>
      <el-col :xl="8" :lg="8" :md="24" :sm="24" :xs="24">
        <el-card shadow="never">
          <el-skeleton :loading="loading" animated>
            <Echart :options="valvePieOptionsData" :height="320" />
          </el-skeleton>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup>
import { set } from 'lodash-es'
import { EChartsOption } from 'echarts'
import dayjs from 'dayjs'
import { getWaterDashboard, type WaterDashboardResp } from '@/api/member/water-dashboard'
import { dailyLineOptions, monthlyBarOptions, valvePieOptions } from './echarts-data'

defineOptions({ name: 'Index' })

const loading = ref(true)
const refreshTime = ref('')

const summary = ref<WaterDashboardResp['summary']>({
  totalRechargeAmount: 0,
  totalRechargeBonusAmount: 0,
  totalUsage: 0,
  totalFeeAmount: 0,
  rechargeCount: 0,
  deviceCount: 0,
  lowBalanceDeviceCount: 0,
  pendingApplyCount: 0,
  pendingFaultCount: 0
})

const dailyLineOptionsData = reactive<EChartsOption>(dailyLineOptions) as EChartsOption
const monthlyBarOptionsData = reactive<EChartsOption>(monthlyBarOptions) as EChartsOption
const valvePieOptionsData = reactive<EChartsOption>(valvePieOptions) as EChartsOption

const dashboardData = ref<WaterDashboardResp | null>(null)

const averageDailyUsage = computed(() => {
  if (!dashboardData.value?.dailyTrend?.length) {
    return 0
  }
  const total = dashboardData.value.dailyTrend.reduce((sum, item) => sum + (item.usage || 0), 0)
  return Math.round(total / dashboardData.value.dailyTrend.length)
})

const recentRechargeAmount = computed(() => {
  if (!dashboardData.value?.dailyTrend?.length) {
    return 0
  }
  return dashboardData.value.dailyTrend.reduce((sum, item) => sum + (item.rechargeAmount || 0), 0)
})

const formatAmount = (value?: number) => {
  const amount = value ?? 0
  return amount.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const formatUsage = (value?: number) => {
  const usage = value ?? 0
  const tons = usage / 1000
  return tons.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const fillDailyLine = (data: WaterDashboardResp['dailyTrend']) => {
  set(dailyLineOptionsData, 'xAxis.data', data.map((item) => item.time))
  set(dailyLineOptionsData, 'series', [
    {
      name: '充值金额(元)',
      type: 'line',
      smooth: true,
      data: data.map((item) => item.rechargeAmount)
    },
    {
      name: '用水量(升)',
      type: 'line',
      smooth: true,
      data: data.map((item) => item.usage)
    },
    {
      name: '费用金额(元)',
      type: 'line',
      smooth: true,
      data: data.map((item) => item.feeAmount)
    }
  ])
}

const fillMonthlyBar = (data: WaterDashboardResp['monthlyTrend']) => {
  set(monthlyBarOptionsData, 'xAxis.data', data.map((item) => item.time))
  set(monthlyBarOptionsData, 'series', [
    {
      name: '充值金额(元)',
      type: 'bar',
      data: data.map((item) => item.rechargeAmount)
    },
    {
      name: '用水量(升)',
      type: 'bar',
      data: data.map((item) => item.usage)
    },
    {
      name: '费用金额(元)',
      type: 'bar',
      data: data.map((item) => item.feeAmount)
    }
  ])
}

const fillPie = (option: EChartsOption, data: WaterDashboardResp['valveStatusStats']) => {
  set(option, 'legend.data', data.map((item) => item.name))
  set(option, 'series', [
    {
      type: 'pie',
      radius: '55%',
      center: ['50%', '60%'],
      data: data.map((item) => ({
        name: item.name,
        value: item.value
      }))
    }
  ])
}

const fetchDashboard = async () => {
  loading.value = true
  try {
    const data = await getWaterDashboard()
    dashboardData.value = data
    summary.value = data.summary
    fillDailyLine(data.dailyTrend)
    fillMonthlyBar(data.monthlyTrend)
    fillPie(valvePieOptionsData, data.valveStatusStats)
    refreshTime.value = dayjs().format('YYYY-MM-DD HH:mm')
  } finally {
    loading.value = false
  }
}

fetchDashboard()
</script>

<style scoped>
.water-dashboard .el-card {
  border-radius: 8px;
}
</style>
