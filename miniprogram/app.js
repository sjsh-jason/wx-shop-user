App({
  globalData: {
    userInfo: null,
    token: null,
    baseUrl: 'http://192.168.184.1:8080'
  },

  onLaunch() {
    const token = wx.getStorageSync('token');
    const userInfo = wx.getStorageSync('userInfo');
    if (token) {
      this.globalData.token = token;
    }
    if (userInfo) {
      this.globalData.userInfo = userInfo;
    }
    this.checkLogin();
  },

  checkLogin() {
    if (!this.globalData.token) {
      wx.reLaunch({
        url: '/pages/login/login'
      });
    }
  },

  setUserInfo(userInfo) {
    this.globalData.userInfo = userInfo;
    wx.setStorageSync('userInfo', userInfo);
  },

  setToken(token) {
    this.globalData.token = token;
    wx.setStorageSync('token', token);
  },

  request(options) {
    const { url, method = 'GET', data } = options;
    return new Promise((resolve, reject) => {
      wx.request({
        url: this.globalData.baseUrl + url,
        method,
        data,
        header: {
          'Content-Type': 'application/json',
          'Authorization': this.globalData.token ? 'Bearer ' + this.globalData.token : ''
        },
        success: (res) => {
          if (res.statusCode === 200) {
            if (res.data.code === 200) {
              resolve(res.data.data);
            } else {
              wx.showToast({
                title: res.data.message || '请求失败',
                icon: 'none'
              });
              reject(res.data);
            }
          } else if (res.statusCode === 401) {
            this.setToken(null);
            this.setUserInfo(null);
            wx.reLaunch({
              url: '/pages/login/login'
            });
            reject(res);
          } else {
            wx.showToast({
              title: '网络错误',
              icon: 'none'
            });
            reject(res);
          }
        },
        fail: (err) => {
          wx.showToast({
            title: '网络错误',
            icon: 'none'
          });
          reject(err);
        }
      });
    });
  }
});
