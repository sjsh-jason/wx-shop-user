const app = getApp();

Page({
  data: {
    reservations: [],
    currentTab: 0,
    loading: false
  },

  onLoad() {
    this.loadReservations();
  },

  onShow() {
    this.loadReservations();
  },

  loadReservations() {
    this.setData({ loading: true });
    app.request({
      url: '/api/reservation/my',
      method: 'GET'
    }).then(res => {
      this.setData({
        reservations: res || [],
        loading: false
      });
    }).catch(err => {
      this.setData({ loading: false });
      console.error('加载预约记录失败:', err);
    });
  },

  get filteredList() {
    const list = this.data.reservations;
    const tab = this.data.currentTab;
    if (tab === 0) return list;
    if (tab === 1) return list.filter(item => item.status === 0);
    if (tab === 2) return list.filter(item => item.status === 1);
    return list;
  },

  switchTab(e) {
    const tab = parseInt(e.currentTarget.dataset.tab);
    this.setData({ currentTab: tab });
  },

  getStatusColor(status) {
    if (status === 0) return '#FF6B00';
    if (status === 1) return '#10B981';
    return '#999999';
  },

  getStatusText(status) {
    if (status === 0) return '待核销';
    if (status === 1) return '已核销';
    return '已取消';
  },

  formatTime(timeStr) {
    if (!timeStr) return '';
    const date = new Date(timeStr);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hour = String(date.getHours()).padStart(2, '0');
    const minute = String(date.getMinutes()).padStart(2, '0');
    return `${year}-${month}-${day} ${hour}:${minute}`;
  },

  goToDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: '/pages/reservation-detail/reservation-detail?id=' + id
    });
  }
});
