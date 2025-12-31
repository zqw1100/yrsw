<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="110px"
      v-loading="formLoading"
    >
      <el-form-item label="套餐名称" prop="name">
        <el-input v-model="formData.name" placeholder="请输入套餐名称" />
      </el-form-item>
      <el-form-item label="套餐售价" prop="price">
        <el-input-number v-model="formData.price" :min="0" :precision="2" class="!w-200px" />
      </el-form-item>
      <el-form-item label="基础水量(升)" prop="waterVolume">
        <el-input-number v-model="formData.waterVolume" :min="0" class="!w-200px" />
      </el-form-item>
      <el-form-item label="赠送水量(升)" prop="giftWaterVolume">
        <el-input-number v-model="formData.giftWaterVolume" :min="0" class="!w-200px" />
      </el-form-item>
      <el-form-item label="优惠金额(元)" prop="discountAmount">
        <el-input-number v-model="formData.discountAmount" :min="0" :precision="2" class="!w-200px" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio :value="1">启用</el-radio>
          <el-radio :value="0">停用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input-number v-model="formData.sort" :min="0" class="!w-200px" />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input type="textarea" v-model="formData.remark" placeholder="请输入备注" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import * as WaterRechargePackageApi from '@/api/member/water-recharge-package'

const { t } = useI18n()
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const formRef = ref()
const formData = ref({
  id: undefined,
  name: '',
  price: 0,
  waterVolume: 0,
  giftWaterVolume: 0,
  discountAmount: 0,
  status: 1,
  sort: 0,
  remark: ''
})

const formRules = reactive({
  name: [{ required: true, message: '套餐名称不能为空', trigger: 'blur' }],
  price: [{ required: true, message: '套餐售价不能为空', trigger: 'blur' }],
  waterVolume: [{ required: true, message: '基础水量不能为空', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
})

const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  if (id) {
    formLoading.value = true
    try {
      formData.value = await WaterRechargePackageApi.getWaterRechargePackage(id)
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
    const data = formData.value as WaterRechargePackageApi.WaterRechargePackageVO
    if (formType.value === 'create') {
      await WaterRechargePackageApi.createWaterRechargePackage(data)
      message.success('新增成功')
    } else {
      await WaterRechargePackageApi.updateWaterRechargePackage(data)
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
    name: '',
    price: 0,
    waterVolume: 0,
    giftWaterVolume: 0,
    discountAmount: 0,
    status: 1,
    sort: 0,
    remark: ''
  }
  formRef.value?.resetFields()
}
</script>
