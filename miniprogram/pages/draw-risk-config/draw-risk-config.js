const app = getApp();

Page({
  data: {
    configs: [],
    saving: false
  },

  onLoad() {
    this.loadConfigs();
  },

  loadConfigs() {
    wx.showLoading({ title: '加载中...' });
    app.request({
      url: '/api/draw/risk-config',
      method: 'GET'
    }).then((res) => {
      if (res && res.length > 0) {
        this.setData({ configs: res });
      }
      wx.hideLoading();
    }).catch(() => {
      wx.hideLoading();
    });
  },

  getOptions(config) {
    if (!config.options) return [];
    try {
      return JSON.parse(config.options);
    } catch (e) {
      return [];
    }
  },

  getSelectedLabel(config) {
    const options = this.getOptions(config);
    const selected = options.find(o => o.value === config.configValue);
    return selected ? selected.label : config.configValue;
  },

  onSwitchChange(e) {
    const index = e.currentTarget.dataset.index;
    const configs = this.data.configs;
    configs[index].configValue = e.detail.value ? 'true' : 'false';
    this.setData({ configs });
  },

  onSelectChange(e) {
    const index = e.currentTarget.dataset.index;
    const configs = this.data.configs;
    const options = this.getOptions(configs[index]);
    configs[index].configValue = options[e.detail.value].value;
    this.setData({ configs });
  },

  saveConfigs() {
    this.setData({ saving: true });
    app.request({
      url: '/api/draw/risk-config',
      method: 'PUT',
      data: this.data.configs
    }).then(() => {
      wx.showToast({ title: '保存成功', icon: 'success' });
      this.setData({ saving: false });
    }).catch((err) => {
      wx.showToast({ title: err.message || '保存失败', icon: 'none' });
      this.setData({ saving: false });
    });
  }
});
