import { defineStore } from 'pinia';
import WaterApplyApi from '@/sheep/api/water/apply';
import PayWalletApi from '@/sheep/api/pay/wallet';

const defaultWallet = {
  balance: 0,
};

const waterDevice = defineStore({
  id: 'waterDevice',
  state: () => ({
    deviceList: [],
    activeDeviceNo: '',
    deviceWallet: { ...defaultWallet },
    lastWalletUpdate: 0,
  }),
  getters: {
    activeDevice(state) {
      return state.deviceList.find((item) => item.deviceNo === state.activeDeviceNo) || null;
    },
  },
  actions: {
    formatAddress(item) {
      if (!item) return '';
      return [item.areaName, item.communityName, item.buildingName, item.unitName, item.roomNo]
        .filter(Boolean)
        .join(' ');
    },
    setActiveDeviceNo(deviceNo) {
      this.activeDeviceNo = deviceNo || '';
    },
    ensureActiveDevice() {
      if (!this.deviceList.length) {
        this.activeDeviceNo = '';
        return;
      }
      if (!this.activeDeviceNo || !this.deviceList.some((item) => item.deviceNo === this.activeDeviceNo)) {
        this.activeDeviceNo = this.deviceList[0].deviceNo;
      }
    },
    async fetchDevices() {
      const { code, data } = await WaterApplyApi.getApplyPage({
        pageNo: 1,
        pageSize: 50,
      });
      if (code !== 0) {
        return;
      }
      const deviceMap = new Map();
      (data.list || []).forEach((item) => {
        if (!item.deviceNo || deviceMap.has(item.deviceNo)) {
          return;
        }
        deviceMap.set(item.deviceNo, {
          deviceNo: item.deviceNo,
          contactName: item.contactName,
          contactMobile: item.contactMobile,
          areaName: item.areaName,
          communityName: item.communityName,
          buildingName: item.buildingName,
          unitName: item.unitName,
          roomNo: item.roomNo,
          applyId: item.id,
          waterHouseId: item.waterHouseId,
        });
      });
      this.deviceList = Array.from(deviceMap.values());
      this.ensureActiveDevice();
    },
    async fetchWallet() {
      if (!this.activeDeviceNo) {
        this.deviceWallet = { ...defaultWallet };
        return;
      }
      const { code, data } = await PayWalletApi.getPayWallet({ deviceNo: this.activeDeviceNo });
      if (code !== 0) {
        return;
      }
      this.deviceWallet = data || { ...defaultWallet };
      this.lastWalletUpdate = Date.now();
    },
  },
  persist: {
    enabled: true,
    strategies: [
      {
        key: 'water-device-store',
      },
    ],
  },
});

export default waterDevice;
