const app = getApp();

Page({
  data: {
    hasCheckedIn: false,
    checkInInfo: null,
    loading: true
  },

  onLoad() {
    this.loadCheckInStatus();
  },

  onShow() {
    this.loadCheckInStatus();
  },

  loadCheckInStatus() {
    app.request({
      url: '/api/checkIn/status',
      method: 'GET'
    }).then((res) => {
      this.setData({
        hasCheckedIn: res.hasCheckedIn || false,
        loading: false
      });
    }).catch(() => {
      this.setData({
        hasCheckedIn: false,
        loading: false
      });
    });
  },

  doCheckIn() {
    if (this.data.hasCheckedIn) {
      wx.showToast({
        title: '今日已签到',
        icon: 'none'
      });
      return;
    }

    wx.showLoading({ title: '签到中...' });

    app.request({
      url: '/api/checkIn',
      method: 'POST'
    }).then((res) => {
      wx.hideLoading();
      this.setData({
        hasCheckedIn: true,
        checkInInfo: res
      });
      wx.showModal({
        title: '签到成功',
        content: `获得 ${res.points} 积分${res.continuousDays > 1 ? '，连续签到 ' + res.continuousDays + ' 天' : ''}`,
        showCancel: false,
        success: () => {
          // 刷新用户信息以更新积分
          app.request({
            url: '/api/user/info',
            method: 'GET'
          }).then((userRes) => {
            app.setUserInfo(userRes);
          });
        }
      });
    }).catch((err) => {
      wx.hideLoading();
      wx.showToast({
        title: err.message || '签到失败',
        icon: 'none'
      });
    });
  },

  goToPointsLog() {
    wx.navigateTo({
      url: '/pages/points-log/points-log'
    });
  }
});
