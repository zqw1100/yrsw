import request from '@/config/axios'

export interface WaterDevicePageParams {
  pageNo: number
  pageSize: number
  deviceNo?: string
  deviceUserName?: string
  deviceAddress?: string
  valveStatus?: number
  createTime?: string[]
}

export const getWaterDevicePage = (params: WaterDevicePageParams) => {
  return request.get({ url: '/member/water-device/page', params })
}

export const refreshWaterDevice = (id: number) => {
  return request.put({ url: '/member/water-device/refresh', params: { id } })
}
