import request from '@/config/axios'

export interface WaterDeviceHistoryPageParams {
  pageNo: number
  pageSize: number
  deviceNo?: string
  deviceUserName?: string
  deviceAddress?: string
  valveStatus?: number
  reportReason?: number
  deviceUpdateTime?: string[]
}

export const getWaterDeviceHistoryPage = (params: WaterDeviceHistoryPageParams) => {
  return request.get({ url: '/member/water-device-history/page', params })
}
