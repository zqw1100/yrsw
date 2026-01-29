import request from '@/config/axios'

export interface WaterMeterFinanceDailyReqVO {
  year?: number
  city?: string
  county?: string
  community?: string
  dateRange?: string[]
}

export interface WaterMeterFinanceMonthlyRespVO {
  city: string
  county: string
  community: string
  year: number
  month: string
  billingDays: number
  orderCount: number
  orderAmount: number
  deliveryAmount: number
  paidAmount: number
  wechatCount: number
  wechatAmount: number
  wechatFee: number
  refundAmount: number
}

export interface WaterMeterFinanceDailyRespVO {
  city: string
  county: string
  community: string
  year: number
  month: string
  date: string
  orderCount: number
  orderAmount: number
  deliveryAmount: number
  paidAmount: number
  wechatCount: number
  wechatAmount: number
  wechatFee: number
  refundAmount: number
}

export const getFinanceMonthlyList = async (params: Record<string, any>) => {
  return await request.get<WaterMeterFinanceMonthlyRespVO[]>({
    url: '/statistics/water-meter/finance/month-list',
    params
  })
}

export const getFinanceDailyList = async (params: WaterMeterFinanceDailyReqVO) => {
  return await request.get<WaterMeterFinanceDailyRespVO[]>({
    url: '/statistics/water-meter/finance/day-list',
    params
  })
}

export const exportFinanceMonthlyExcel = async (params: Record<string, any>) => {
  return await request.download({
    url: '/statistics/water-meter/finance/export-month-excel',
    params
  })
}

export const exportFinanceDailyExcel = async (params: WaterMeterFinanceDailyReqVO) => {
  return await request.download({
    url: '/statistics/water-meter/finance/export-day-excel',
    params
  })
}
