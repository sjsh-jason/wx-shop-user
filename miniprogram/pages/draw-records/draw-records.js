const app = getApp();

Page({
  data: {
    records: [],
    page: 1,
    pageSize: 20,
    loading: false,
    hasMore: true
  },

  onLoad() {
    this.loadRecords();
  },

  onPullDownRefresh() {
    this.setData({
      page: 1,
      records: [],
      hasMore: true
    });
    this.loadRecords(() => {
      wx.stopPullDownRefresh();
    });
  },

  onReachBottom() {
    if (this.data.hasMore && !this.data.loading) {
      this.loadRecords();
    }
  },

  loadRecords(callback) {
    if (this.data.loading || !this.data.hasMore) {
      if (callback) callback();
      return;
    }

    this.setData({ loading: true });

    app.request({
      url: '/api/lucky-draw/my',
      method: 'GET',
      data: {
        page: this.data.page,
        pageSize: this.data.pageSize
      }
    }).then((res) => {
      const newRecords = res.records || res || [];
      this.setData({
        records: this.data.page === 1 ? newRecords : [...this.data.records, ...newRecords],
        hasMore: newRecords.length >= this.data.pageSize,
        page: this.data.page + 1,
        loading: false
      });
      if (callback) callback();
    }).catch(() => {
      this.setData({ loading: false });
      if (callback) callback();
    });
  },

  formatTime(timeStr) {
    if (!timeStr) return '';
    const date = new Date(timeStr.replace(/-/g, '/'));
    const m = (date.getMonth() + 1).toString().padStart(2, '0');
    const d = date.getDate().toString().padStart(2, '0');
    const h = date.getHours().toString().padStart(2, '0');
    const min = date.getMinutes().toString().padStart(2, '0');
    return `${m}-${d} ${h}:${min}`;
  },

  formatDate(timeStr) {
    if (!timeStr) return '';
    const date = new Date(timeStr.replace(/-/g, '/'));
    const y = date.getFullYear();
    const m = (date.getMonth() + 1).toString().padStart(2, '0');
    const d = date.getDate().toString().padStart(2, '0');
    return `${y}-${m}-${d}`;
  }
});
