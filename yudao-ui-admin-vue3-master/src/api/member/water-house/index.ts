import request from '@/config/axios'

export interface WaterHouseVO {
  id?: number
  areaId: number
  areaName: string
  communityName: string
  buildingName: string
  unitName: string
  roomNo: string
  installStatus: number
  sort?: number
  remark?: string
}

export const getWaterHousePage = async (params: any) => {
  return await request.get({ url: '/member/water-house/page', params })
}

export const getWaterHouse = async (id: number) => {
  return await request.get({ url: '/member/water-house/get?id=' + id })
}

export const createWaterHouse = async (data: WaterHouseVO) => {
  return await request.post({ url: '/member/water-house/create', data })
}

export const updateWaterHouse = async (data: WaterHouseVO) => {
  return await request.put({ url: '/member/water-house/update', data })
}

export const deleteWaterHouse = async (id: number) => {
  return await request.delete({ url: '/member/water-house/delete?id=' + id })
}
