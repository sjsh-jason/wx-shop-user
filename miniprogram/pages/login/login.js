const app = getApp();

Page({
  data: {
    agreed: false
  },

  onLoad(options) {
    if (options.scene) {
      console.log('扫码场景:', options.scene);
    }
  },

  onPrivacyChange(e) {
    const agreed = e.detail.value.includes('agree');
    this.setData({ agreed });
  },

  onGetUserInfo() {
    if (!this.data.agreed) {
      wx.showToast({
        title: '请先同意用户协议',
        icon: 'none'
      });
      return;
    }

    wx.showLoading({ title: '登录中...' });

    wx.login({
      success: (res) => {
        if (res.code) {
          this.wechatLogin(res.code);
        } else {
          wx.hideLoading();
          wx.showToast({
            title: '获取登录信息失败',
            icon: 'none'
          });
        }
      },
      fail: () => {
        wx.hideLoading();
        wx.showToast({
          title: '登录失败',
          icon: 'none'
        });
      }
    });
  },

  wechatLogin(code) {
    app.request({
      url: '/api/auth/wechat/login',
      method: 'POST',
      data: { code }
    }).then(res => {
      wx.hideLoading();
      app.setToken(res.token);
      app.setUserInfo(res.userInfo);
      wx.showToast({
        title: '登录成功',
        icon: 'success',
        duration: 1500
      });
      setTimeout(() => {
        wx.switchTab({
          url: '/pages/index/index'
        });
      }, 1500);
    }).catch(err => {
      wx.hideLoading();
      console.error('登录失败:', err);
    });
  }
});
