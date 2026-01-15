import { EChartsOption } from 'echarts'

export const dailyLineOptions: EChartsOption = {
  title: {
    text: '近 7 日充值 / 用水 / 费用趋势',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  grid: {
    left: 40,
    right: 20,
    bottom: 20,
    top: 70
  },
  legend: {
    top: 35
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: []
  },
  yAxis: {
    type: 'value'
  },
  series: []
}

export const monthlyBarOptions: EChartsOption = {
  title: {
    text: '近 6 月汇总',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow'
    }
  },
  grid: {
    left: 40,
    right: 20,
    bottom: 20,
    top: 70
  },
  legend: {
    top: 35
  },
  xAxis: {
    type: 'category',
    data: []
  },
  yAxis: {
    type: 'value'
  },
  series: []
}

export const rechargePieOptions: EChartsOption = {
  title: {
    text: '充值渠道分布',
    left: 'center'
  },
  tooltip: {
    trigger: 'item',
    formatter: '{b} : {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 'left',
    data: []
  },
  series: []
}

export const valvePieOptions: EChartsOption = {
  title: {
    text: '阀门状态分布',
    left: 'center'
  },
  tooltip: {
    trigger: 'item',
    formatter: '{b} : {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 'left',
    data: []
  },
  series: []
}
