<template>
  <ContentWrap>
    <el-card shadow="never">
      <template #header>
        <div class="flex items-center justify-between">
          <div class="flex items-center gap-8px">
            <Icon icon="ep:trophy" />
            <span class="text-base font-medium">财务统计报表</span>
          </div>
          <el-radio-group v-model="activeReport" size="small">
            <el-radio-button label="month">月报表</el-radio-button>
            <el-radio-button label="day">日报表</el-radio-button>
          </el-radio-group>
        </div>
      </template>

      <div v-if="activeReport === 'month'">
        <div class="mb-4 flex items-center justify-between">
          <span class="text-xs text-gray-500">
            微信手续费按千分之 6 计算（费率：{{ (wechatFeeRate * 100).toFixed(2) }}%）
          </span>
          <el-button type="primary" :loading="exportLoading.month" @click="exportMonthly">
            <Icon icon="ep:download" class="mr-1" />导出月报表
          </el-button>
        </div>
        <el-table :data="monthlyRows" border height="520" v-loading="loading.month">
          <el-table-column type="index" label="序号" width="70" align="center" />
          <el-table-column label="地区" align="center">
            <el-table-column prop="city" label="城市" width="120" align="center" />
            <el-table-column prop="county" label="县区" min-width="200" />
            <el-table-column prop="community" label="小区" min-width="140" />
          </el-table-column>
          <el-table-column prop="year" label="年份" width="90" align="center" />
          <el-table-column prop="month" label="月份" width="90" align="center" />
          <el-table-column prop="billingDays" label="计费天数" width="100" align="center" />
          <el-table-column label="平台订单数量" align="center">
            <el-table-column prop="orderCount" label="订单总量" width="100" align="center" />
            <el-table-column
              prop="orderAmount"
              label="订单金额汇总(元)"
              width="140"
              align="center"
            >
              <template #default="{ row }">{{ formatMoney(row.orderAmount) }}</template>
            </el-table-column>
            <el-table-column
              prop="deliveryAmount"
              label="订单配送金额(元)"
              width="140"
              align="center"
            >
              <template #default="{ row }">{{ formatMoney(row.deliveryAmount) }}</template>
            </el-table-column>
            <el-table-column
              prop="paidAmount"
              label="入账金额汇总(元)"
              width="140"
              align="center"
            >
              <template #default="{ row }">{{ formatMoney(row.paidAmount) }}</template>
            </el-table-column>
          </el-table-column>
          <el-table-column label="微信支付数据" align="center">
            <el-table-column prop="wechatCount" label="交易单数" width="100" align="center" />
            <el-table-column
              prop="wechatAmount"
              label="交易总额(元)"
              width="120"
              align="center"
            >
              <template #default="{ row }">{{ formatMoney(row.wechatAmount) }}</template>
            </el-table-column>
            <el-table-column
              prop="wechatFee"
              label="手续费总额(元)"
              width="130"
              align="center"
            >
              <template #default="{ row }">{{ formatMoney(row.wechatFee) }}</template>
            </el-table-column>
            <el-table-column
              prop="refundAmount"
              label="退款总金额(元)"
              width="130"
              align="center"
            >
              <template #default="{ row }">{{ formatMoney(row.refundAmount) }}</template>
            </el-table-column>
          </el-table-column>
        </el-table>
      </div>

      <div v-else>
        <el-form class="-mb-15px" :inline="true" label-width="70px">
          <el-form-item label="统计年份">
            <el-date-picker
              v-model="filters.year"
              type="year"
              value-format="YYYY"
              placeholder="选择年份"
            />
          </el-form-item>
          <el-form-item label="城市选择">
            <el-input v-model="filters.city" placeholder="请输入城市" class="!w-160px" />
          </el-form-item>
          <el-form-item label="区县选择">
            <el-input v-model="filters.county" placeholder="请输入区县" class="!w-160px" />
          </el-form-item>
          <el-form-item label="小区选择">
            <el-input v-model="filters.community" placeholder="请输入小区" class="!w-160px" />
          </el-form-item>
          <el-form-item label="报表日期">
            <el-date-picker
              v-model="filters.dateRange"
              type="daterange"
              value-format="YYYY-MM-DD"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              class="!w-260px"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">
              <Icon icon="ep:search" class="mr-1" /> 查询
            </el-button>
            <el-button @click="handleReset">
              <Icon icon="ep:refresh" class="mr-1" /> 重置
            </el-button>
          </el-form-item>
        </el-form>
        <div class="mb-4 mt-4 flex items-center justify-between">
          <span class="text-xs text-gray-500">
            微信手续费按千分之 6 计算（费率：{{ (wechatFeeRate * 100).toFixed(2) }}%）
          </span>
          <el-button type="primary" :loading="exportLoading.day" @click="exportDaily">
            <Icon icon="ep:download" class="mr-1" />导出日报表
          </el-button>
        </div>
        <el-table :data="dailyRows" border height="520" v-loading="loading.day">
          <el-table-column type="index" label="序号" width="70" align="center" />
          <el-table-column label="地区" align="center">
            <el-table-column prop="city" label="城市" width="120" align="center" />
            <el-table-column prop="county" label="县区" min-width="200" />
            <el-table-column prop="community" label="小区" min-width="140" />
          </el-table-column>
          <el-table-column prop="year" label="年份" width="90" align="center" />
          <el-table-column prop="month" label="月份" width="90" align="center" />
          <el-table-column prop="date" label="日期" width="120" align="center" />
          <el-table-column label="平台订单数量" align="center">
            <el-table-column prop="orderCount" label="订单总量" width="100" align="center" />
            <el-table-column
              prop="orderAmount"
              label="订单金额汇总(元)"
              width="140"
              align="center"
            >
              <template #default="{ row }">{{ formatMoney(row.orderAmount) }}</template>
            </el-table-column>
            <el-table-column
              prop="deliveryAmount"
              label="订单配送金额(元)"
              width="140"
              align="center"
            >
              <template #default="{ row }">{{ formatMoney(row.deliveryAmount) }}</template>
            </el-table-column>
            <el-table-column
              prop="paidAmount"
              label="入账金额汇总(元)"
              width="140"
              align="center"
            >
              <template #default="{ row }">{{ formatMoney(row.paidAmount) }}</template>
            </el-table-column>
          </el-table-column>
          <el-table-column label="微信支付数据" align="center">
            <el-table-column prop="wechatCount" label="交易单数" width="100" align="center" />
            <el-table-column
              prop="wechatAmount"
              label="交易总额(元)"
              width="120"
              align="center"
            >
              <template #default="{ row }">{{ formatMoney(row.wechatAmount) }}</template>
            </el-table-column>
            <el-table-column
              prop="wechatFee"
              label="手续费总额(元)"
              width="130"
              align="center"
            >
              <template #default="{ row }">{{ formatMoney(row.wechatFee) }}</template>
            </el-table-column>
            <el-table-column
              prop="refundAmount"
              label="退款总金额(元)"
              width="130"
              align="center"
            >
              <template #default="{ row }">{{ formatMoney(row.refundAmount) }}</template>
            </el-table-column>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </ContentWrap>
</template>

<script setup lang="ts">
import download from '@/utils/download'
import {
  exportFinanceDailyExcel,
  exportFinanceMonthlyExcel,
  getFinanceDailyList,
  getFinanceMonthlyList,
  type WaterMeterFinanceDailyRespVO,
  type WaterMeterFinanceMonthlyRespVO
} from '@/api/waterMeterReport/finance'

type DailyFilters = {
  year: string
  city: string
  county: string
  community: string
  dateRange: string[]
}

const activeReport = ref<'month' | 'day'>('month')
const wechatFeeRate = 0.006

const formatMoney = (value: number) => value.toFixed(2)

const monthlyRows = ref<WaterMeterFinanceMonthlyRespVO[]>([])
const dailyRows = ref<WaterMeterFinanceDailyRespVO[]>([])

const filters = reactive<DailyFilters>({
  year: '',
  city: '',
  county: '',
  community: '',
  dateRange: []
})

const exportLoading = reactive({
  month: false,
  day: false
})

const loading = reactive({
  month: false,
  day: false
})

const buildDailyParams = () => ({
  year: filters.year ? Number(filters.year) : undefined,
  city: filters.city || undefined,
  county: filters.county || undefined,
  community: filters.community || undefined,
  dateRange: filters.dateRange.length ? filters.dateRange : undefined
})

const getMonthlyData = async () => {
  loading.month = true
  try {
    monthlyRows.value = await getFinanceMonthlyList({})
  } finally {
    loading.month = false
  }
}

const getDailyData = async () => {
  loading.day = true
  try {
    dailyRows.value = await getFinanceDailyList(buildDailyParams())
  } finally {
    loading.day = false
  }
}

const handleSearch = () => {
  getDailyData()
}

const handleReset = () => {
  filters.year = ''
  filters.city = ''
  filters.county = ''
  filters.community = ''
  filters.dateRange = []
}

const exportMonthly = async () => {
  exportLoading.month = true
  try {
    const data = await exportFinanceMonthlyExcel({})
    download.excel(data, `财务统计月报表-${new Date().toISOString().slice(0, 10)}.xls`)
  } finally {
    exportLoading.month = false
  }
}

const exportDaily = async () => {
  exportLoading.day = true
  try {
    const data = await exportFinanceDailyExcel(buildDailyParams())
    download.excel(data, `财务统计日报表-${new Date().toISOString().slice(0, 10)}.xls`)
  } finally {
    exportLoading.day = false
  }
}

onMounted(() => {
  getMonthlyData()
  getDailyData()
})
</script>
