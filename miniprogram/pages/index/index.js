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
    console.log('navTo 被触发', e);
    const url = e.currentTarget.dataset.url;
    console.log('跳转 URL:', url);
    if (url) {
      // 检查是否是 TabBar 页面
      const tabBarPages = ['/pages/index/index', '/pages/mall/mall', '/pages/coupons/coupons', '/pages/profile/profile'];
      if (tabBarPages.includes(url)) {
        wx.switchTab({
          url,
          fail: (err) => {
            console.error('switchTab 跳转失败:', err);
            wx.showToast({
              title: '跳转失败',
              icon: 'none'
            });
          }
        });
      } else {
        wx.navigateTo({
          url,
          fail: (err) => {
            console.error('navigateTo 跳转失败:', err);
            wx.showToast({
              title: '跳转失败',
              icon: 'none'
            });
          }
        });
      }
    } else {
      console.error('URL 为空');
    }
  },

  goFlashSale() {
    wx.navigateTo({
      url: '/pages/flash/flash'
    });
  },

  goLuckyDraw() {
    wx.navigateTo({
      url: '/pages/lucky-draw/lucky-draw'
    });
  },

  buyNow(e) {
    const id = e.currentTarget.dataset.id || 1;
    wx.showLoading({ title: '抢购中...' });
    app.request({
      url: '/api/reservation',
      method: 'POST',
      data: { productId: id }
    }).then(res => {
      wx.hideLoading();
      wx.showToast({
        title: '抢购成功',
        icon: 'success',
        duration: 1500
      });
      setTimeout(() => {
        wx.redirectTo({
          url: '/pages/reservation-detail/reservation-detail?id=' + res.id
        });
      }, 1500);
    }).catch(err => {
      wx.hideLoading();
      wx.showToast({
        title: err.message || '抢购失败',
        icon: 'none'
      });
    });
  }
});
