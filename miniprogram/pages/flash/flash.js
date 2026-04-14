const app = getApp();

Page({
  data: {
    products: [],
    loading: false,
    hours: '02',
    minutes: '45',
    seconds: '12',
    timer: null
  },

  onLoad() {
    this.loadProducts();
    this.startCountdown();
  },

  onUnload() {
    if (this.data.timer) {
      clearInterval(this.data.timer);
    }
  },

  startCountdown() {
    let totalSeconds = 2 * 3600 + 45 * 60 + 12;
    const timer = setInterval(() => {
      totalSeconds--;
      if (totalSeconds <= 0) {
        totalSeconds = 24 * 3600;
      }
      const hours = Math.floor(totalSeconds / 3600);
      const minutes = Math.floor((totalSeconds % 3600) / 60);
      const seconds = totalSeconds % 60;
      this.setData({
        hours: String(hours).padStart(2, '0'),
        minutes: String(minutes).padStart(2, '0'),
        seconds: String(seconds).padStart(2, '0')
      });
    }, 1000);
    this.setData({ timer });
  },

  loadProducts() {
    this.setData({ loading: true });
    app.request({
      url: '/api/promotion/products',
      method: 'GET'
    }).then(res => {
      this.setData({
        products: res || [],
        loading: false
      });
    }).catch(err => {
      this.setData({ loading: false });
      console.error('加载商品失败:', err);
    });
  },

  getProgress(item) {
    if (!item.soldCount && item.soldCount !== 0) return 50;
    const total = item.stock + item.soldCount;
    if (total === 0) return 0;
    return Math.floor((item.soldCount / total) * 100);
  },

  goToDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: '/pages/reservation-detail/reservation-detail?id=' + id
    });
  },

  buyNow(e) {
    const id = e.currentTarget.dataset.id;
    wx.showLoading({ title: '抢购中...' });
    app.request({
      url: '/api/reservation',
      method: 'POST',
      data: { productId: id }
    }).then(res => {
      wx.hideLoading();
      wx.showToast({
        title: '抢购成功',
        icon: 'success',
        duration: 1500
      });
      setTimeout(() => {
        wx.redirectTo({
          url: '/pages/reservation-detail/reservation-detail?id=' + res.id
        });
      }, 1500);
    }).catch(err => {
      wx.hideLoading();
      wx.showToast({
        title: err.message || '抢购失败',
        icon: 'none'
      });
    });
  },

  onShow() {
    this.loadProducts();
  }
});
