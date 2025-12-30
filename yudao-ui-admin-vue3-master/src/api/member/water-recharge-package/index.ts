import request from '@/config/axios'

export interface WaterRechargePackageVO {
  id?: number
  name: string
  price: number
  waterVolume: number
  giftWaterVolume?: number
  discountAmount?: number
  status: number
  sort?: number
  remark?: string
  createTime?: Date
}

export const getWaterRechargePackagePage = async (params: any) => {
  return await request.get({ url: '/member/water-recharge-package/page', params })
}

export const getWaterRechargePackage = async (id: number) => {
  return await request.get({ url: '/member/water-recharge-package/get?id=' + id })
}

export const createWaterRechargePackage = async (data: WaterRechargePackageVO) => {
  return await request.post({ url: '/member/water-recharge-package/create', data })
}

export const updateWaterRechargePackage = async (data: WaterRechargePackageVO) => {
  return await request.put({ url: '/member/water-recharge-package/update', data })
}

export const deleteWaterRechargePackage = async (id: number) => {
  return await request.delete({ url: '/member/water-recharge-package/delete?id=' + id })
}
