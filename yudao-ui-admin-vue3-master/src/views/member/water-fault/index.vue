<template>
  <ContentWrap>
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="90px"
    >
      <el-form-item label="小区名称" prop="communityName">
        <el-input
          v-model="queryParams.communityName"
          placeholder="请输入小区名称"
          clearable
          @keyup.enter="handleQuery"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="楼栋名称" prop="buildingName">
        <el-input
          v-model="queryParams.buildingName"
          placeholder="请输入楼栋名称"
          clearable
          @keyup.enter="handleQuery"
          class="!w-200px"
        />
      </el-form-item>
      <el-form-item label="单元名称" prop="unitName">
        <el-input
          v-model="queryParams.unitName"
          placeholder="请输入单元名称"
          clearable
          @keyup.enter="handleQuery"
          class="!w-180px"
        />
      </el-form-item>
      <el-form-item label="房间号" prop="roomNo">
        <el-input
          v-model="queryParams.roomNo"
          placeholder="请输入房间号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-160px"
        />
      </el-form-item>
      <el-form-item label="户主姓名" prop="ownerName">
        <el-input
          v-model="queryParams.ownerName"
          placeholder="请输入户主姓名"
          clearable
          @keyup.enter="handleQuery"
          class="!w-160px"
        />
      </el-form-item>
      <el-form-item label="联系方式" prop="contactMobile">
        <el-input
          v-model="queryParams.contactMobile"
          placeholder="请输入联系方式"
          clearable
          @keyup.enter="handleQuery"
          class="!w-200px"
        />
      </el-form-item>
      <el-form-item label="设备号" prop="deviceNo">
        <el-input
          v-model="queryParams.deviceNo"
          placeholder="请输入设备号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-200px"
        />
      </el-form-item>
      <el-form-item label="故障类型" prop="faultCode">
        <el-select v-model="queryParams.faultCode" placeholder="请选择" clearable class="!w-160px">
          <el-option
            v-for="item in faultTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="优先级" prop="priority">
        <el-select v-model="queryParams.priority" placeholder="请选择" clearable class="!w-140px">
          <el-option v-for="item in priorityOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="处理状态" prop="processStatus">
        <el-select v-model="queryParams.processStatus" placeholder="请选择" clearable class="!w-160px">
          <el-option
            v-for="item in processStatusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="编号" align="center" prop="id" width="90px" />
      <el-table-column label="用户编号" align="center" prop="userId" width="110px" />
      <el-table-column label="地址" min-width="220px">
        <template #default="{ row }">
          <div>{{ row.areaName }}</div>
          <div>{{ row.communityName }} {{ row.buildingName }} {{ row.unitName }} {{ row.roomNo }}</div>
        </template>
      </el-table-column>
      <el-table-column label="户主姓名" align="center" prop="ownerName" width="100px" />
      <el-table-column label="联系方式" align="center" prop="contactMobile" width="130px" />
      <el-table-column label="设备号" align="center" prop="deviceNo" width="140px" />
      <el-table-column label="故障类型" align="center" width="140px">
        <template #default="{ row }">
          {{ getDictLabel(faultTypeOptions, row.faultCode) }}
        </template>
      </el-table-column>
      <el-table-column label="优先级" align="center" width="100px">
        <template #default="{ row }">
          {{ getDictLabel(priorityOptions, row.priority) }}
        </template>
      </el-table-column>
      <el-table-column label="故障图片" min-width="200px">
        <template #default="{ row }">
          <div class="flex flex-wrap gap-8px">
            <el-image
              v-for="(url, index) in row.imageUrls || []"
              :key="`${row.id}-${index}`"
              :src="url"
              class="h-40px w-40px cursor-pointer"
              fit="cover"
              @click="imagePreview(row.imageUrls, index)"
            />
          </div>
        </template>
      </el-table-column>
      <el-table-column label="处理状态" align="center" width="150px">
        <template #default="{ row }">
          <el-select
            v-model="row.processStatus"
            size="small"
            class="!w-130px"
            @change="(value) => handleUpdateStatus(row.id, value)"
          >
            <el-option
              v-for="item in processStatusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="备注" min-width="180px">
        <template #default="{ row }">
          <el-input
            v-model="row.remark"
            size="small"
            placeholder="填写备注"
            @blur="() => handleUpdateRemark(row.id, row.remark)"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180"
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

<script setup lang="ts" name="MemberWaterFault">
import { dateFormatter } from '@/utils/formatTime'
import { DICT_TYPE, getDictLabel, getIntDictOptions, getStrDictOptions } from '@/utils/dict'
import { createImageViewer } from '@/components/ImageViewer'
import * as WaterFaultApi from '@/api/member/water-fault'

const message = useMessage()
const loading = ref(true)
const total = ref(0)
const list = ref<any[]>([])
const queryFormRef = ref()
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  communityName: undefined,
  buildingName: undefined,
  unitName: undefined,
  roomNo: undefined,
  ownerName: undefined,
  contactMobile: undefined,
  deviceNo: undefined,
  faultCode: undefined,
  priority: undefined,
  processStatus: undefined
})

const priorityOptions = [
  { label: '低', value: 1 },
  { label: '中', value: 2 },
  { label: '高', value: 3 }
]
const processStatusOptions = getIntDictOptions(DICT_TYPE.MEMBER_WATER_FAULT_STATUS)
const faultTypeOptions = getStrDictOptions(DICT_TYPE.DEVICE_FAULT_CODE)

const getList = async () => {
  loading.value = true
  try {
    const data = await WaterFaultApi.getWaterFaultPage(queryParams)
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
  queryFormRef.value.resetFields()
  handleQuery()
}

const handleUpdateStatus = async (id: number, processStatus: number) => {
  try {
    await WaterFaultApi.updateWaterFaultStatus({ id, processStatus })
    message.success('状态更新成功')
  } catch {
    await getList()
  }
}

const handleUpdateRemark = async (id: number, remark: string) => {
  if (!remark) {
    return
  }
  await WaterFaultApi.updateWaterFaultStatus({ id, remark })
}

const imagePreview = (urls: string[], index: number) => {
  if (!urls || urls.length === 0) {
    return
  }
  createImageViewer({
    urlList: urls,
    initialIndex: index
  })
}

onMounted(() => {
  getList()
})
</script>
