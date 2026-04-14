const app = getApp();

Page({
  data: {
    userInfo: null
  },

  onLoad() {
    this.loadUserInfo();
  },

  onShow() {
    this.loadUserInfo();
  },

  loadUserInfo() {
    const userInfo = app.globalData.userInfo;
    if (userInfo) {
      this.setData({ userInfo });
    } else {
      this.fetchUserInfo();
    }
  },

  fetchUserInfo() {
    app.request({
      url: '/api/user/info',
      method: 'GET'
    }).then(res => {
      app.setUserInfo(res);
      this.setData({ userInfo: res });
    }).catch(err => {
      console.error('获取用户信息失败:', err);
    });
  },

  navTo(e) {
    const url = e.currentTarget.dataset.url;
    wx.navigateTo({ url });
  },

  goCheckIn() {
    wx.navigateTo({
      url: '/pages/check-in/check-in'
    });
  },

  goLuckyDraw() {
    wx.navigateTo({
      url: '/pages/lucky-draw/lucky-draw'
    });
  }
});
