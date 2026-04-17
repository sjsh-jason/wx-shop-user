const app = getApp();

Page({
  data: {
    config: {
      title: '幸运大抽奖',
      ruleContent: ''
    },
    startDate: '',
    endDate: '',
    saving: false
  },

  onLoad() {
    this.loadConfig();
  },

  loadConfig() {
    wx.showLoading({ title: '加载中...' });
    app.request({
      url: '/api/draw/activity/config',
      method: 'GET'
    }).then((res) => {
      if (res) {
        this.setData({
          config: res,
          startDate: res.startTime ? res.startTime.substring(0, 10) : '',
          endDate: res.endTime ? res.endTime.substring(0, 10) : ''
        });
      }
      wx.hideLoading();
    }).catch(() => {
      wx.hideLoading();
    });
  },

  onTitleInput(e) {
    this.setData({
      'config.title': e.detail.value
    });
  },

  onRuleInput(e) {
    this.setData({
      'config.ruleContent': e.detail.value
    });
  },

  onStartDateChange(e) {
    this.setData({
      startDate: e.detail.value,
      'config.startTime': e.detail.value + ' 00:00:00'
    });
  },

  onEndDateChange(e) {
    this.setData({
      endDate: e.detail.value,
      'config.endTime': e.detail.value + ' 23:59:59'
    });
  },

  saveConfig() {
    if (!this.data.config.title) {
      wx.showToast({ title: '请输入活动标题', icon: 'none' });
      return;
    }

    this.setData({ saving: true });
    app.request({
      url: '/api/draw/activity/config',
      method: 'PUT',
      data: this.data.config
    }).then(() => {
      wx.showToast({ title: '保存成功', icon: 'success' });
      this.setData({ saving: false });
    }).catch((err) => {
      wx.showToast({ title: err.message || '保存失败', icon: 'none' });
      this.setData({ saving: false });
    });
  }
});
