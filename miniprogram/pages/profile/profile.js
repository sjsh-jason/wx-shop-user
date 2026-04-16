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
  },

  goToProductManage() {
    wx.navigateTo({
      url: '/pages/product-manage/product-manage'
    });
  },

  goToPromotionManage() {
    wx.navigateTo({
      url: '/pages/promotion-manage/promotion-manage'
    });
  },

  goToPointsProductManage() {
    wx.navigateTo({
      url: '/pages/points-product-manage/points-product-manage'
    });
  },

  logout() {
    wx.showModal({
      title: '提示',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          app.setToken(null);
          app.setUserInfo(null);
          wx.reLaunch({
            url: '/pages/login/login'
          });
        }
      }
    });
  }
});
