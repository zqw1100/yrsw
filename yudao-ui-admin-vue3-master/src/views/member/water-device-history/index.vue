<template>
  <ContentWrap>
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="90px"
    >
      <el-form-item label="设备号" prop="deviceNo">
        <el-input
          v-model="queryParams.deviceNo"
          placeholder="请输入设备号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="设备用户" prop="deviceUserName">
        <el-input
          v-model="queryParams.deviceUserName"
          placeholder="请输入设备用户"
          clearable
          @keyup.enter="handleQuery"
          class="!w-200px"
        />
      </el-form-item>
      <el-form-item label="设备地址" prop="deviceAddress">
        <el-input
          v-model="queryParams.deviceAddress"
          placeholder="请输入设备地址"
          clearable
          @keyup.enter="handleQuery"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="阀门状态" prop="valveStatus">
        <el-select v-model="queryParams.valveStatus" placeholder="请选择" clearable class="!w-180px">
          <el-option
            v-for="item in valveStatusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="上报原因" prop="reportReason">
        <el-select v-model="queryParams.reportReason" placeholder="请选择" clearable class="!w-200px">
          <el-option
            v-for="item in reportReasonOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="抄收时间" prop="deviceUpdateTime">
        <el-date-picker
          v-model="queryParams.deviceUpdateTime"
          type="datetimerange"
          value-format="YYYY-MM-DD HH:mm:ss"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          class="!w-340px"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="设备号" align="center" prop="deviceNo" width="150px" />
      <el-table-column label="设备用户" align="center" prop="deviceUserName" width="120px" />
      <el-table-column label="设备地址" min-width="200px" prop="deviceAddress" />
      <el-table-column label="余额(元)" align="center" width="120px">
        <template #default="{ row }">
          {{ formatBalance(row.deviceBalance) }}
        </template>
      </el-table-column>
      <el-table-column label="累计流量(升)" align="center" prop="deviceTotalData" width="140px" />
      <el-table-column label="瞬时流量(升)" align="center" prop="deviceCurrentData" width="140px" />
      <el-table-column label="结算日流量(升)" align="center" prop="deviceSettleDayData" width="160px" />
      <el-table-column label="上月流量(升)" align="center" prop="deviceLastData" width="140px" />
      <el-table-column label="阀门状态" align="center" width="120px">
        <template #default="{ row }">
          <el-tag :type="statusTagType(row.valveStatus)">
            {{ valveStatusLabel(row.valveStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="电池状态" align="center" width="120px">
        <template #default="{ row }">
          <el-tag :type="row.voltageStatus === 0 ? 'success' : 'warning'">
            {{ voltageStatusLabel(row.voltageStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="费用状态" align="center" width="120px">
        <template #default="{ row }">
          <el-tag :type="row.feeStatus === 0 ? 'success' : 'danger'">
            {{ feeStatusLabel(row.feeStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="上报原因" align="center" width="140px">
        <template #default="{ row }">
          {{ reportReasonLabel(row.reportReason) }}
        </template>
      </el-table-column>
      <el-table-column label="电池电压" align="center" prop="deviceVoltage" width="120px" />
      <el-table-column label="信号强度" align="center" prop="deviceRssi" width="100px" />
      <el-table-column
        label="设备时钟"
        align="center"
        prop="deviceClock"
        :formatter="dateFormatter"
        width="170px"
      />
      <el-table-column
        label="抄收时间"
        align="center"
        prop="deviceUpdateTime"
        :formatter="dateFormatter"
        width="170px"
      />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="170px"
      />
      <el-table-column label="周期类型" align="center" prop="cycleReportType" width="110px" />
      <el-table-column label="周期数据" min-width="240px" prop="cycleReportContent" />
    </el-table>
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>
</template>

<script setup lang="ts" name="MemberWaterDeviceHistory">
import { dateFormatter } from '@/utils/formatTime'
import * as WaterDeviceHistoryApi from '@/api/member/water-device-history'

const loading = ref(true)
const total = ref(0)
const list = ref<any[]>([])
const queryFormRef = ref()

const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  deviceNo: undefined,
  deviceUserName: undefined,
  deviceAddress: undefined,
  valveStatus: undefined,
  reportReason: undefined,
  deviceUpdateTime: undefined
})

const valveStatusOptions = [
  { label: '无阀控', value: 0 },
  { label: '开阀', value: 1 },
  { label: '关阀', value: 2 },
  { label: '异常', value: 3 }
]

const reportReasonOptions = [
  { label: '定时上报', value: 0 },
  { label: '触发上报', value: 1 },
  { label: '首次上电', value: 2 },
  { label: '阀门动作', value: 3 },
  { label: '磁干扰', value: 4 },
  { label: '泄漏报警', value: 5 },
  { label: '备份上传', value: 6 },
  { label: '欠压报警', value: 7 },
  { label: '剩余告警', value: 8 },
  { label: '欠费报警', value: 9 },
  { label: '透支用完', value: 10 },
  { label: '调节完成', value: 11 },
  { label: '震动报警', value: 12 },
  { label: '密集采样', value: 13 },
  { label: '数据重发', value: 14 },
  { label: '数据补发', value: 22 }
]

const getList = async () => {
  loading.value = true
  try {
    const data = await WaterDeviceHistoryApi.getWaterDeviceHistoryPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

const resetQuery = () => {
  queryFormRef.value?.resetFields()
  handleQuery()
}

const formatBalance = (balance?: number) => {
  if (balance === null || balance === undefined) {
    return '-'
  }
  return (balance / 100).toFixed(2)
}

const valveStatusLabel = (value?: number) => {
  return valveStatusOptions.find((item) => item.value === value)?.label || '-'
}

const voltageStatusLabel = (value?: number) => {
  if (value === 0) {
    return '正常'
  }
  if (value === 1) {
    return '欠压'
  }
  if (value === 3) {
    return '异常'
  }
  return '-'
}

const feeStatusLabel = (value?: number) => {
  if (value === 0) {
    return '正常'
  }
  if (value === 1) {
    return '余额不足'
  }
  if (value === 2) {
    return '欠费'
  }
  return '-'
}

const statusTagType = (status?: number) => {
  if (status === 1) {
    return 'success'
  }
  if (status === 2) {
    return 'danger'
  }
  if (status === 3 || status === 4) {
    return 'warning'
  }
  return 'info'
}

const reportReasonLabel = (value?: number) => {
  return reportReasonOptions.find((item) => item.value === value)?.label || '-'
}

onMounted(() => {
  getList()
})
</script>
