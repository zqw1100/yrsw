import request from '@/config/axios'

export interface WaterFeeBillVO {
  id?: number
  deviceNo: string
  statDate: string
  totalUsage?: number
  lastTotalUsage?: number
  usageDiff?: number
  fee?: number
  balance?: number
  walletId?: number
  createTime?: Date
}

export const getWaterFeeBillPage = async (params: any) => {
  return await request.get({ url: '/member/water-fee-bill/page', params })
}

export const getWaterFeeBill = async (id: number) => {
  return await request.get({ url: '/member/water-fee-bill/get?id=' + id })
}
