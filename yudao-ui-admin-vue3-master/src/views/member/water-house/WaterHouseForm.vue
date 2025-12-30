<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="地区" prop="areaId">
        <el-tree-select
          v-model="formData.areaId"
          :data="areaList"
          :props="defaultProps"
          :render-after-expand="true"
          @change="onAreaChange"
        />
      </el-form-item>
      <el-form-item label="小区" prop="communityName">
        <el-input v-model="formData.communityName" placeholder="请输入小区名称" />
      </el-form-item>
      <el-form-item label="楼栋" prop="buildingName">
        <el-input v-model="formData.buildingName" placeholder="请输入楼栋名称" />
      </el-form-item>
      <el-form-item label="单元" prop="unitName">
        <el-input v-model="formData.unitName" placeholder="请输入单元名称" />
      </el-form-item>
      <el-form-item label="房间号" prop="roomNo">
        <el-input v-model="formData.roomNo" placeholder="请输入房间号" />
      </el-form-item>
      <el-form-item label="报装状态" prop="installStatus">
        <el-radio-group v-model="formData.installStatus">
          <el-radio :value="0">未报装</el-radio>
          <el-radio :value="1">已报装</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input-number v-model="formData.sort" :min="0" class="!w-120px" />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input type="textarea" v-model="formData.remark" placeholder="请输入备注" />
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input type="textarea" v-model="formData.description" placeholder="请输入描述" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import * as WaterHouseApi from '@/api/member/water-house'
import * as AreaApi from '@/api/system/area'
import { defaultProps } from '@/utils/tree'

const { t } = useI18n()
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const formRef = ref()
const areaList = ref([])
const formData = ref({
  id: undefined,
  areaId: undefined,
  areaName: '',
  communityName: '',
  buildingName: '',
  unitName: '',
  roomNo: '',
  installStatus: 0,
  sort: 0,
  remark: ''
  description: ''
})

const formRules = reactive({
  areaId: [{ required: true, message: '地区不能为空', trigger: 'change' }],
  communityName: [{ required: true, message: '小区不能为空', trigger: 'blur' }],
  buildingName: [{ required: true, message: '楼栋不能为空', trigger: 'blur' }],
  unitName: [{ required: true, message: '单元不能为空', trigger: 'blur' }],
  roomNo: [{ required: true, message: '房间号不能为空', trigger: 'blur' }],
  installStatus: [{ required: true, message: '请选择报装状态', trigger: 'change' }]
})

const findAreaName = (nodes: any[], id: number): string => {
  for (const node of nodes) {
    if (node.id === id) return node.name
    if (node.children?.length) {
      const result = findAreaName(node.children, id)
      if (result) return result
    }
  }
  return ''
}

const onAreaChange = (value: number) => {
  formData.value.areaName = findAreaName(areaList.value, value)
}

const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  areaList.value = await AreaApi.getAreaTree()
  if (id) {
    formLoading.value = true
    try {
      formData.value = await WaterHouseApi.getWaterHouse(id)
    } finally {
      formLoading.value = false
    }
  }
}

defineExpose({ open })

const emit = defineEmits(['success'])
const submitForm = async () => {
  if (!formRef.value) return
  const valid = await formRef.value.validate()
  if (!valid) return
  formLoading.value = true
  try {
    const data = formData.value as WaterHouseApi.WaterHouseVO
    if (formType.value === 'create') {
      await WaterHouseApi.createWaterHouse(data)
      message.success('新增成功')
    } else {
      await WaterHouseApi.updateWaterHouse(data)
      message.success('更新成功')
    }
    dialogVisible.value = false
    emit('success')
  } finally {
    formLoading.value = false
  }
}

const resetForm = () => {
  formData.value = {
    id: undefined,
    areaId: undefined,
    areaName: '',
    communityName: '',
    buildingName: '',
    unitName: '',
    roomNo: '',
    installStatus: 0,
    sort: 0,
  remark: ''
  description: ''
  }
  formRef.value?.resetFields()
}
</script>
