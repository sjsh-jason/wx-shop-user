const app = getApp();

Page({
  data: {
    reservation: null,
    reservationId: null
  },

  onLoad(options) {
    if (options.id) {
      this.setData({ reservationId: options.id });
      this.loadDetail(options.id);
    }
  },

  loadDetail(id) {
    wx.showLoading({ title: '加载中...' });
    app.request({
      url: '/api/reservation/' + id,
      method: 'GET'
    }).then(res => {
      wx.hideLoading();
      this.setData({ reservation: res });
      if (res.status === 0 && res.qrCode) {
        this.generateQRCode(res.qrCode);
      }
    }).catch(err => {
      wx.hideLoading();
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      });
    });
  },

  generateQRCode(text) {
    const ctx = wx.createCanvasContext('qrCanvas');
    const size = 360;
    ctx.setFillStyle('#ffffff');
    ctx.fillRect(0, 0, size, size);

    ctx.setFillStyle('#333333');
    ctx.setFontSize(24);
    ctx.setTextAlign('center');
    ctx.fillText('核销码', size / 2, size / 2 - 20);
    ctx.setFontSize(16);
    ctx.setFillStyle('#666666');
    ctx.fillText(text.substring(0, 16) + '...', size / 2, size / 2 + 20);

    ctx.draw();
  },

  getStatusBg(status) {
    if (status === 0) return '#FFF0E6';
    if (status === 1) return '#D1FAE5';
    return '#F5F5F5';
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

  goToScan() {
    wx.switchTab({
      url: '/pages/profile/profile'
    });
  }
});
