import request from '@/config/axios'

export interface WaterFaultPageParams {
  pageNo: number
  pageSize: number
  communityName?: string
  buildingName?: string
  unitName?: string
  roomNo?: string
  contactMobile?: string
  ownerName?: string
  deviceNo?: string
  faultCode?: string
  priority?: number
  processStatus?: number
}

export interface WaterFaultStatusUpdateReqVO {
  id: number
  processStatus?: number
  remark?: string
}

export const getWaterFaultPage = (params: WaterFaultPageParams) => {
  return request.get({ url: '/member/water-fault/page', params })
}

export const updateWaterFaultStatus = (data: WaterFaultStatusUpdateReqVO) => {
  return request.put({ url: '/member/water-fault/update-status', data })
}
