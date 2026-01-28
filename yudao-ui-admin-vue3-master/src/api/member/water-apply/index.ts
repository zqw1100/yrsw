import request from '@/config/axios'

export interface WaterApplyPageParams {
  pageNo: number
  pageSize: number
  areaId?: number
  communityName?: string
  communityId?: string
  buildingName?: string
  unitName?: string
  roomNo?: string
  contactName?: string
  contactMobile?: string
  applyStatus?: number
  processStatus?: number
  createTime?: string[]
}

export const getWaterApplyPage = (params: WaterApplyPageParams) => {
  return request.get({ url: '/member/water-apply/page', params })
}
export const checkDeviceNo = (deviceNo: string, excludeApplyId?: number) => {
  return request.get({
    url: '/member/water-apply/check-device',
    params: { deviceNo, excludeApplyId }
  })
}
export const updateWaterApplyStatus = (data: { id: number; processStatus: number; deviceNo?: string }) => {
  return request.put({ url: '/member/water-apply/update-status', data })
}
