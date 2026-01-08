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
          class="!w-220px"
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
          class="!w-180px"
        />
      </el-form-item>
      <el-form-item label="联系人" prop="contactName">
        <el-input
          v-model="queryParams.contactName"
          placeholder="请输入联系人"
          clearable
          @keyup.enter="handleQuery"
          class="!w-180px"
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
      <el-form-item label="申请状态" prop="applyStatus">
        <el-select v-model="queryParams.applyStatus" placeholder="请选择" clearable class="!w-160px">
          <el-option
            v-for="item in applyStatusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
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
      <el-table-column label="联系人" align="center" prop="contactName" width="100px" />
      <el-table-column label="联系方式" align="center" prop="contactMobile" width="130px" />
      <el-table-column label="户主姓名" align="center" prop="ownerName" width="100px" />
      <el-table-column label="户主身份证" align="center" prop="ownerIdCard" width="170px" />
      <el-table-column label="合同图片" min-width="200px">
        <template #default="{ row }">
          <div class="flex flex-wrap gap-8px">
            <el-image
              v-for="(url, index) in row.contractImageUrls || []"
              :key="`${row.id}-${index}`"
              :src="url"
              class="h-40px w-40px cursor-pointer"
              fit="cover"
              @click="imagePreview(row.contractImageUrls, index)"
            />
          </div>
        </template>
      </el-table-column>
      <el-table-column label="施工前图片" min-width="200px">
        <template #default="{ row }">
          <div class="flex flex-wrap gap-8px">
            <el-image
              v-for="(url, index) in row.beforeImageUrls || []"
              :key="`before-${row.id}-${index}`"
              :src="url"
              class="h-40px w-40px cursor-pointer"
              fit="cover"
              @click="imagePreview(row.beforeImageUrls, index)"
            />
          </div>
        </template>
      </el-table-column>
      <el-table-column label="施工前备注" min-width="160px" prop="beforeRemark" />
      <el-table-column label="施工后图片" min-width="200px">
        <template #default="{ row }">
          <div class="flex flex-wrap gap-8px">
            <el-image
              v-for="(url, index) in row.afterImageUrls || []"
              :key="`after-${row.id}-${index}`"
              :src="url"
              class="h-40px w-40px cursor-pointer"
              fit="cover"
              @click="imagePreview(row.afterImageUrls, index)"
            />
          </div>
        </template>
      </el-table-column>
      <el-table-column label="施工后备注" min-width="160px" prop="afterRemark" />
      <el-table-column label="申请状态" align="center" width="110px">
        <template #default="{ row }">
          <el-tag :type="row.applyStatus === 1 ? 'success' : 'info'">
            {{ row.applyStatus === 1 ? '已提交' : '待补充资料' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="设备号" align="center" prop="deviceNo" width="140px" />
      <el-table-column label="处理状态" align="center" width="150px">
        <template #default="{ row }">
          <el-select
            v-model="row.processStatus"
            disabled
            size="small"
            class="!w-130px"
            @change="(value) => handleUpdateStatus(row, value)"
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

<script setup lang="ts" name="MemberWaterApply">
import { dateFormatter } from '@/utils/formatTime'
import { ElMessageBox } from 'element-plus'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { createImageViewer } from '@/components/ImageViewer'
import * as WaterApplyApi from '@/api/member/water-apply'

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
  contactName: undefined,
  contactMobile: undefined,
  applyStatus: undefined,
  processStatus: undefined
})

const applyStatusOptions = [
  { label: '待补充资料', value: 0 },
  { label: '已提交', value: 1 }
]
const processStatusOptions = getIntDictOptions(DICT_TYPE.MEMBER_WATER_APPLY_STATUS)

const getList = async () => {
  loading.value = true
  try {
    const data = await WaterApplyApi.getWaterApplyPage(queryParams)
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

const handleUpdateStatus = async (row: any, processStatus: number) => {
  try {
    let deviceNo: string | undefined
    if (processStatus === 3) {
      const { value } = await ElMessageBox.prompt('请输入设备号', '施工完成', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /\S+/,
        inputErrorMessage: '设备号不能为空'
      })
      deviceNo = value
      if (deviceNo && deviceNo !== row.deviceNo) {
        const used = await WaterApplyApi.checkDeviceNo(deviceNo, row.id)
        if (used) {
          message.error('设备号已被使用，无法重复绑定')
          await getList()
          return
        }
      }
    }
    await WaterApplyApi.updateWaterApplyStatus({ id: row.id, processStatus, deviceNo })
    message.success('状态更新成功')
  } catch {
    await getList()
  }
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
