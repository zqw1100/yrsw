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
        label="同步时间"
        align="center"
        prop="lastSyncTime"
        :formatter="dateFormatter"
        width="170px"
      />
      <el-table-column label="操作" align="center" fixed="right" width="220px">
        <template #default="{ row }">
          <el-button
            link
            type="primary"
            @click="handleRefresh(row.id)"
            v-hasPermi="['member:water-device:update']"
          >
            刷新
          </el-button>
          <el-button
            link
            type="primary"
            @click="openValveDialog(row)"
            v-hasPermi="['member:water-device:update']"
          >
            阀门
          </el-button>
          <el-button
            link
            type="primary"
            @click="openChangeDialog(row)"
            v-hasPermi="['member:water-device:update']"
          >
            换表
          </el-button>
          <el-button
            link
            type="primary"
            @click="openUploadDialog(row)"
            v-hasPermi="['member:water-device:update']"
          >
            上传周期
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

  <el-dialog v-model="valveDialogVisible" title="阀门操作" width="420px">
    <el-form ref="valveFormRef" :model="valveForm" :rules="valveRules" label-width="100px">
      <el-form-item label="设备号" prop="deviceNo">
        <el-input v-model="valveForm.deviceNo" placeholder="请输入设备号" />
      </el-form-item>
      <el-form-item label="阀门状态" prop="valveStatus">
        <el-select v-model="valveForm.valveStatus" placeholder="请选择阀门状态" class="!w-200px">
          <el-option label="开阀" :value="1" />
          <el-option label="关阀" :value="2" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="valveDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitValve">确认</el-button>
    </template>
  </el-dialog>

  <el-dialog v-model="changeDialogVisible" title="换表" width="460px">
    <el-form ref="changeFormRef" :model="changeForm" :rules="changeRules" label-width="120px">
      <el-form-item label="旧表设备编码" prop="originalDeviceCode">
        <el-input v-model="changeForm.originalDeviceCode" placeholder="请输入旧表设备编码" />
      </el-form-item>
      <el-form-item label="新表设备编码" prop="newDeviceCode">
        <el-input v-model="changeForm.newDeviceCode" placeholder="请输入新表设备编码" />
      </el-form-item>
      <el-form-item label="旧表累计流量" prop="originalTotalData">
        <el-input-number v-model="changeForm.originalTotalData" :min="0" class="!w-200px" />
        <span class="ml-8px text-gray-400">单位：升</span>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="changeDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitChange">确认</el-button>
    </template>
  </el-dialog>

  <el-dialog v-model="uploadDialogVisible" title="设置上传周期" width="460px">
    <el-form ref="uploadFormRef" :model="uploadForm" :rules="uploadRules" label-width="120px">
      <el-form-item label="设备编码" prop="deviceCode">
        <el-input v-model="uploadForm.deviceCode" placeholder="请输入设备编码" />
      </el-form-item>
      <el-form-item label="周期类型" prop="uploadType">
        <el-select v-model="uploadForm.uploadType" placeholder="请选择周期类型" class="!w-200px">
          <el-option label="分钟" :value="1" />
          <el-option label="小时" :value="2" />
          <el-option label="天" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="上传周期" prop="value">
        <el-input-number v-model="uploadForm.value" :min="1" class="!w-200px" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="uploadDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitUpload">确认</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts" name="MemberWaterDevice">
import { dateFormatter } from '@/utils/formatTime'
import * as WaterDeviceApi from '@/api/member/water-device'

const message = useMessage()
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
  valveStatus: undefined
})
const valveDialogVisible = ref(false)
const changeDialogVisible = ref(false)
const uploadDialogVisible = ref(false)
const valveFormRef = ref()
const changeFormRef = ref()
const uploadFormRef = ref()
const valveForm = reactive({
  deviceNo: '',
  valveStatus: undefined as number | undefined
})
const changeForm = reactive({
  originalDeviceCode: '',
  newDeviceCode: '',
  originalTotalData: undefined as number | undefined
})
const uploadForm = reactive({
  deviceCode: '',
  uploadType: undefined as number | undefined,
  value: undefined as number | undefined
})
const valveRules = {
  deviceNo: [{ required: true, message: '设备号不能为空', trigger: 'blur' }],
  valveStatus: [{ required: true, message: '阀门状态不能为空', trigger: 'change' }]
}
const changeRules = {
  originalDeviceCode: [{ required: true, message: '旧表设备编码不能为空', trigger: 'blur' }],
  newDeviceCode: [{ required: true, message: '新表设备编码不能为空', trigger: 'blur' }],
  originalTotalData: [{ required: true, message: '旧表累计流量不能为空', trigger: 'blur' }]
}
const uploadRules = {
  deviceCode: [{ required: true, message: '设备编码不能为空', trigger: 'blur' }],
  uploadType: [{ required: true, message: '周期类型不能为空', trigger: 'change' }],
  value: [
    { required: true, message: '上传周期不能为空', trigger: 'blur' },
    {
      validator: (_: any, value: number, callback: (error?: Error) => void) => {
        if (!uploadForm.uploadType) {
          callback()
          return
        }
        if (uploadForm.uploadType === 1 && (value < 5 || value > 59)) {
          callback(new Error('分钟上传周期需在 5-59 之间'))
          return
        }
        if (uploadForm.uploadType === 2 && (value < 1 || value > 23)) {
          callback(new Error('小时上传周期需在 1-23 之间'))
          return
        }
        if (uploadForm.uploadType === 3 && (value < 1 || value > 7)) {
          callback(new Error('天上传周期需在 1-7 之间'))
          return
        }
        callback()
      },
      trigger: 'blur'
    }
  ]
}

const valveStatusOptions = [
  { label: '无阀控', value: 0 },
  { label: '开-合', value: 1 },
  { label: '关-断', value: 2 },
  { label: '异常', value: 3 },
  { label: '漏气', value: 4 },
  { label: '比例开阀', value: 5 }
]

const voltageStatusOptions = [
  { label: '正常', value: 0 },
  { label: '欠压', value: 1 },
  { label: '异常', value: 3 }
]

const feeStatusOptions = [
  { label: '正常', value: 0 },
  { label: '余额不足', value: 1 },
  { label: '欠费', value: 2 }
]

const getList = async () => {
  loading.value = true
  try {
    const { data } = await WaterDeviceApi.getWaterDevicePage(queryParams)
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

const handleRefresh = async (id: number) => {
  await WaterDeviceApi.refreshWaterDevice(id)
  message.success('同步成功')
  getList()
}

const openValveDialog = (row: any) => {
  valveForm.deviceNo = row.deviceNo
  valveForm.valveStatus = undefined
  valveDialogVisible.value = true
}

const openChangeDialog = (row: any) => {
  changeForm.originalDeviceCode = row.deviceNo
  changeForm.newDeviceCode = ''
  changeForm.originalTotalData = undefined
  changeDialogVisible.value = true
}

const openUploadDialog = (row: any) => {
  uploadForm.deviceCode = row.deviceNo
  uploadForm.uploadType = undefined
  uploadForm.value = undefined
  uploadDialogVisible.value = true
}

const submitValve = async () => {
  await valveFormRef.value?.validate()
  await WaterDeviceApi.operateValve({
    deviceNo: valveForm.deviceNo,
    valveStatus: valveForm.valveStatus as number
  })
  message.success('阀门操作已提交')
  valveDialogVisible.value = false
  getList()
}

const submitChange = async () => {
  await changeFormRef.value?.validate()
  await WaterDeviceApi.changeWaterDevice({
    originalDeviceCode: changeForm.originalDeviceCode,
    newDeviceCode: changeForm.newDeviceCode,
    originalTotalData: changeForm.originalTotalData as number
  })
  message.success('换表任务已提交')
  changeDialogVisible.value = false
  getList()
}

const submitUpload = async () => {
  await uploadFormRef.value?.validate()
  await WaterDeviceApi.setWaterDeviceUploadMode({
    deviceCode: uploadForm.deviceCode,
    uploadType: uploadForm.uploadType as number,
    value: uploadForm.value as number
  })
  message.success('上传周期已提交')
  uploadDialogVisible.value = false
  getList()
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
  return voltageStatusOptions.find((item) => item.value === value)?.label || '-'
}

const feeStatusLabel = (value?: number) => {
  return feeStatusOptions.find((item) => item.value === value)?.label || '-'
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

onMounted(() => {
  getList()
})
</script>
