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

export interface WaterDeviceValveReq {
  deviceNo: string
  valveStatus: number
}

export const operateValve = (data: WaterDeviceValveReq) => {
  return request.post({ url: '/member/water-device/valve', data })
}

export interface WaterDeviceChangeReq {
  originalDeviceCode: string
  newDeviceCode: string
  originalTotalData: number
}

export const changeWaterDevice = (data: WaterDeviceChangeReq) => {
  return request.post({ url: '/member/water-device/change-device', data })
}

export interface WaterDeviceUploadModeReq {
  deviceCode: string
  uploadType: number
  value: number
}

export const setWaterDeviceUploadMode = (data: WaterDeviceUploadModeReq) => {
  return request.post({ url: '/member/water-device/upload-mode', data })
}
