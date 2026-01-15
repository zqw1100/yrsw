import request from '@/config/axios'

export interface WaterDashboardSummary {
  totalRechargeAmount: number
  totalRechargeBonusAmount: number
  totalUsage: number
  totalFeeAmount: number
  rechargeCount: number
  deviceCount: number
  lowBalanceDeviceCount: number
  pendingApplyCount: number
  pendingFaultCount: number
}

export interface WaterDashboardTrend {
  time: string
  rechargeAmount: number
  usage: number
  feeAmount: number
}

export interface WaterDashboardPieItem {
  name: string
  value: number
}

export interface WaterDashboardResp {
  summary: WaterDashboardSummary
  dailyTrend: WaterDashboardTrend[]
  monthlyTrend: WaterDashboardTrend[]
  rechargeChannelStats: WaterDashboardPieItem[]
  valveStatusStats: WaterDashboardPieItem[]
}

export const getWaterDashboard = () => {
  return request.get<WaterDashboardResp>({ url: '/member/water-dashboard' })
}
