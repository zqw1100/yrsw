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
      <el-form-item label="统计日期" prop="statDate">
        <el-date-picker
          v-model="queryParams.statDate"
          type="daterange"
          value-format="YYYY-MM-DD"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          class="!w-240px"
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
      <el-table-column label="统计日期" align="center" prop="statDate" width="120px" />
      <el-table-column label="总用水量(升)" align="center" prop="totalUsage" width="130px" />
      <el-table-column label="上次用水量(升)" align="center" prop="lastTotalUsage" width="140px" />
      <el-table-column label="用量差额(升)" align="center" prop="usageDiff" width="130px" />
      <el-table-column label="费用(元)" align="center" width="120px">
        <template #default="scope">
          {{ formatMoney(scope.row.fee) }}
        </template>
      </el-table-column>
      <el-table-column label="余额(元)" align="center" width="120px">
        <template #default="scope">
          {{ formatMoney(scope.row.balance) }}
        </template>
      </el-table-column>
      <el-table-column label="钱包编号" align="center" prop="walletId" width="120px" />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
    </el-table>
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>
</template>

<script setup lang="ts" name="MemberWaterFeeBill">
import { dateFormatter } from '@/utils/formatTime'
import * as WaterFeeBillApi from '@/api/member/water-fee-bill'

const loading = ref(true)
const total = ref(0)
const list = ref<any[]>([])
const queryFormRef = ref()

const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  deviceNo: undefined,
  statDate: undefined
})

const getList = async () => {
  loading.value = true
  try {
    const data = await WaterFeeBillApi.getWaterFeeBillPage(queryParams)
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

const formatMoney = (value?: number) => {
  if (value === null || value === undefined) {
    return '-'
  }
  return (value / 100).toFixed(2)
}

onMounted(() => {
  getList()
})
</script>
