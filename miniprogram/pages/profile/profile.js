const app = getApp();

Page({
  data: {
    userInfo: null,
    isMerchant: false
  },

  onLoad() {
    this.loadUserInfo();
    this.checkMerchant();
  },

  onShow() {
    this.loadUserInfo();
    this.checkMerchant();
  },

  loadUserInfo() {
    const userInfo = app.globalData.userInfo;
    if (userInfo) {
      this.setData({ userInfo });
    }
  },

  checkMerchant() {
    app.request({
      url: '/api/user/isMerchant',
      method: 'GET'
    }).then((res) => {
      this.setData({
        isMerchant: res.isMerchant || false
      });
    }).catch(() => {
      this.setData({
        isMerchant: false
      });
    });
  },

  goToScan() {
    wx.navigateTo({
      url: '/pages/scan/scan'
    });
  },

  goToCheckIn() {
    wx.navigateTo({
      url: '/pages/check-in/check-in'
    });
  },

  goToLuckyDraw() {
    wx.navigateTo({
      url: '/pages/lucky-draw/lucky-draw'
    });
  },

  goToExchanged() {
    wx.navigateTo({
      url: '/pages/exchanged/exchanged'
    });
  },

  goToPointsLog() {
    wx.navigateTo({
      url: '/pages/points-log/points-log'
    });
  },

  goToPrizeManage() {
    wx.navigateTo({
      url: '/pages/prize-manage/prize-manage'
    });
  },

  goToCustomerList() {
    wx.navigateTo({
      url: '/pages/customer-list/customer-list'
    });
  }
});
