<template>
  <ContentWrap>
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="80px"
    >
      <el-form-item label="地区" prop="areaId">
        <el-tree-select
          v-model="queryParams.areaId"
          :data="areaList"
          :props="defaultProps"
          :render-after-expand="true"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="小区" prop="communityName">
        <el-input
          v-model="queryParams.communityName"
          placeholder="请输入小区名称"
          clearable
          @keyup.enter="handleQuery"
          class="!w-200px"
        />
      </el-form-item>
      <el-form-item label="楼栋" prop="buildingName">
        <el-input
          v-model="queryParams.buildingName"
          placeholder="请输入楼栋名称"
          clearable
          @keyup.enter="handleQuery"
          class="!w-200px"
        />
      </el-form-item>
      <el-form-item label="单元" prop="unitName">
        <el-input
          v-model="queryParams.unitName"
          placeholder="请输入单元名称"
          clearable
          @keyup.enter="handleQuery"
          class="!w-200px"
        />
      </el-form-item>
      <el-form-item label="房间号" prop="roomNo">
        <el-input
          v-model="queryParams.roomNo"
          placeholder="请输入房间号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-180px"
        />
      </el-form-item>
      <el-form-item label="报装状态" prop="installStatus">
        <el-select v-model="queryParams.installStatus" placeholder="请选择" clearable class="!w-160px">
          <el-option v-for="item in installStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
        <el-button type="primary" @click="openForm('create')" v-hasPermi="['member:water-house:create']">
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button type="primary" @click="openImport">
          <Icon icon="ep:upload" class="mr-5px" /> 导入
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="编号" align="center" prop="id" width="100px" />
      <el-table-column label="地区" align="center" prop="areaName" min-width="180px" />
      <el-table-column label="小区" align="center" prop="communityName" min-width="140px" />
      <el-table-column label="楼栋" align="center" prop="buildingName" width="120px" />
      <el-table-column label="单元" align="center" prop="unitName" width="120px" />
      <el-table-column label="房间号" align="center" prop="roomNo" width="120px" />
      <el-table-column label="报装状态" align="center" width="120px">
        <template #default="scope">
          <el-tag :type="scope.row.installStatus === 1 ? 'info' : 'success'">
            {{ scope.row.installStatus === 1 ? '已报装' : '未报装' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="描述" align="center" prop="description" min-width="160px" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180px">
        <template #default="scope">
          {{ formatCreateTime(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="150px">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['member:water-house:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['member:water-house:delete']"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

  <WaterHouseForm ref="formRef" @success="getList" />
  <WaterHouseImportForm ref="importFormRef" @success="getList" />
</template>

<script setup lang="ts" name="MemberWaterHouse">
import { formatDate } from '@/utils/formatTime'
import * as WaterHouseApi from '@/api/member/water-house'
import WaterHouseForm from './WaterHouseForm.vue'
import WaterHouseImportForm from './WaterHouseImportForm.vue'
import * as AreaApi from '@/api/system/area'
import { defaultProps } from '@/utils/tree'

const message = useMessage()
const loading = ref(true)
const total = ref(0)
const list = ref([])
const queryFormRef = ref()
const areaList = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  areaId: undefined,
  communityName: undefined,
  buildingName: undefined,
  unitName: undefined,
  roomNo: undefined,
  installStatus: undefined
})

const installStatusOptions = [
  { label: '未报装', value: 0 },
  { label: '已报装', value: 1 }
]

const formatCreateTime = (value?: number | string) => {
  if (!value) {
    return ''
  }
  const timestamp = typeof value === 'string' ? Number(value) : value
  const time = timestamp && timestamp < 1000000000000 ? timestamp * 1000 : timestamp
  return formatDate(new Date(time))
}

const getList = async () => {
  loading.value = true
  try {
    const data = await WaterHouseApi.getWaterHousePage(queryParams)
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

const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

const importFormRef = ref()
const openImport = () => {
  importFormRef.value.open()
}

const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await WaterHouseApi.deleteWaterHouse(id)
    message.success('删除成功')
    await getList()
  } catch {}
}

onMounted(async () => {
  areaList.value = await AreaApi.getAreaTree()
  getList()
})
</script>
