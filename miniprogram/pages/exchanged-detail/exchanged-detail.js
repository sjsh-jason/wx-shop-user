const app = getApp();

Page({
  data: {
    id: null,
    detail: null,
    loading: true,
    statusText: '',
    createTimeText: '',
    verifyTimeText: ''
  },

  onLoad(options) {
    if (options.id) {
      this.setData({ id: options.id });
      this.loadDetail();
    } else {
      wx.showToast({
        title: '参数错误',
        icon: 'none'
      });
      setTimeout(() => {
        wx.navigateBack();
      }, 1500);
    }
  },

  loadDetail() {
    app.request({
      url: `/api/exchange/${this.data.id}`,
      method: 'GET'
    }).then((res) => {
      this.setData({
        detail: res,
        loading: false,
        statusText: this.getStatusText(res.status),
        createTimeText: this.formatTime(res.createTime),
        verifyTimeText: res.verifyTime ? this.formatTime(res.verifyTime) : ''
      });
    }).catch(() => {
      this.setData({
        loading: false
      });
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      });
    });
  },

  getStatusText(status) {
    const map = {
      0: '待领取',
      1: '已领取',
      2: '已取消'
    };
    return map[status] || '未知';
  },

  formatTime(timeStr) {
    if (!timeStr) return '';
    const date = new Date(timeStr.replace(/-/g, '/'));
    const y = date.getFullYear();
    const m = (date.getMonth() + 1).toString().padStart(2, '0');
    const d = date.getDate().toString().padStart(2, '0');
    const h = date.getHours().toString().padStart(2, '0');
    const min = date.getMinutes().toString().padStart(2, '0');
    return `${y}-${m}-${d} ${h}:${min}`;
  }
});
