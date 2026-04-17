const app = getApp();

Page({
  data: {
    currentTab: 0,
    tabs: [
      { name: '全部', status: null },
      { name: '待取美食', status: 0 },
      { name: '已领取', status: 1 }
    ],
    list: [],
    loading: true
  },

  onLoad() {
    this.loadExchanges();
  },

  onShow() {
    this.loadExchanges();
  },

  switchTab(e) {
    const index = e.currentTarget.dataset.index;
    this.setData({
      currentTab: index,
      loading: true
    });
    this.loadExchanges();
  },

  loadExchanges() {
    const status = this.data.tabs[this.data.currentTab].status;
    let url = '/api/exchange/my';
    if (status !== null) {
      url += `?status=${status}`;
    }

    app.request({
      url,
      method: 'GET'
    }).then((res) => {
      const list = (res || []).map(item => {
        return {
          ...item,
          statusText: this.getStatusText(item.status),
          timeText: this.formatTime(item.createTime)
        };
      });
      this.setData({
        list,
        loading: false
      });
    }).catch(() => {
      this.setData({
        list: [],
        loading: false
      });
    });
  },

  getStatusText(status) {
    const map = {
      0: '待取美食',
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
    return `${y}-${m}-${d}`;
  },

  goToDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/exchanged-detail/exchanged-detail?id=${id}`
    });
  }
});
