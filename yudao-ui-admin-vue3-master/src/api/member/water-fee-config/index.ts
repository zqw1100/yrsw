import request from '@/config/axios'

export interface WaterFeeConfigVO {
  id?: number
  pricePerLiter: number
  status: number
  remark?: string
  createTime?: Date
}

export const getWaterFeeConfigPage = async (params: any) => {
  return await request.get({ url: '/member/water-fee-config/page', params })
}

export const getWaterFeeConfig = async (id: number) => {
  return await request.get({ url: '/member/water-fee-config/get?id=' + id })
}

export const createWaterFeeConfig = async (data: WaterFeeConfigVO) => {
  return await request.post({ url: '/member/water-fee-config/create', data })
}

export const updateWaterFeeConfig = async (data: WaterFeeConfigVO) => {
  return await request.put({ url: '/member/water-fee-config/update', data })
}

export const deleteWaterFeeConfig = async (id: number) => {
  return await request.delete({ url: '/member/water-fee-config/delete?id=' + id })
}
