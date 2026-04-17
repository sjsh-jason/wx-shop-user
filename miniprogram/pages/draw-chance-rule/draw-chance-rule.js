const app = getApp();

Page({
  data: {
    rules: [],
    saving: false
  },

  onLoad() {
    this.loadRules();
  },

  loadRules() {
    wx.showLoading({ title: '加载中...' });
    app.request({
      url: '/api/draw/chance-rules',
      method: 'GET'
    }).then((res) => {
      if (res && res.length > 0) {
        this.setData({ rules: res });
      }
      wx.hideLoading();
    }).catch(() => {
      wx.hideLoading();
    });
  },

  getRuleName(type) {
    const names = {
      'consume': '消费得机会',
      'check_in': '每日签到',
      'new_user': '新用户专享'
    };
    return names[type] || type;
  },

  onStatusChange(e) {
    const index = e.currentTarget.dataset.index;
    const rules = this.data.rules;
    rules[index].status = e.detail.value ? 1 : 0;
    this.setData({ rules });
  },

  onMinAmountInput(e) {
    const index = e.currentTarget.dataset.index;
    const rules = this.data.rules;
    rules[index].minAmount = parseFloat(e.detail.value) || 0;
    this.setData({ rules });
  },

  onGiveCountInput(e) {
    const index = e.currentTarget.dataset.index;
    const rules = this.data.rules;
    rules[index].giveCount = parseInt(e.detail.value) || 1;
    this.setData({ rules });
  },

  onDailyLimitInput(e) {
    const index = e.currentTarget.dataset.index;
    const rules = this.data.rules;
    rules[index].dailyLimit = parseInt(e.detail.value) || null;
    this.setData({ rules });
  },

  saveRules() {
    this.setData({ saving: true });
    app.request({
      url: '/api/draw/chance-rules',
      method: 'PUT',
      data: this.data.rules
    }).then(() => {
      wx.showToast({ title: '保存成功', icon: 'success' });
      this.setData({ saving: false });
    }).catch((err) => {
      wx.showToast({ title: err.message || '保存失败', icon: 'none' });
      this.setData({ saving: false });
    });
  }
});
