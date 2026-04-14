const app = getApp();

Page({
  data: {
    prizes: [],
    loading: true
  },

  onLoad() {
    this.loadPrizes();
  },

  onShow() {
    this.loadPrizes();
  },

  loadPrizes() {
    this.setData({ loading: true });

    app.request({
      url: '/api/prizes',
      method: 'GET'
    }).then((res) => {
      const prizes = (res || []).map((item, index) => ({
        ...item,
        iconText: this.getIconText(item, index)
      }));
      this.setData({
        prizes,
        loading: false
      });
    }).catch(() => {
      this.setData({
        prizes: [],
        loading: false
      });
    });
  },

  getIconText(prize, index) {
    if (prize.type === 1 && prize.points) {
      return prize.points.toString();
    }
    if (prize.type === 3 && prize.couponValue) {
      return '¥' + prize.couponValue;
    }
    const icons = ['饮', '积', '券', '积', '菜', '券', '积', '谢'];
    return icons[index % 8];
  },

  getTypeText(type) {
    const map = {
      0: '谢谢参与',
      1: '积分',
      2: '商品',
      3: '代金券'
    };
    return map[type] || '未知';
  },

  toggleStatus(e) {
    const id = e.currentTarget.dataset.id;
    const checked = e.detail.value;
    wx.showToast({
      title: checked ? '已启用' : '已禁用',
      icon: 'success'
    });
  },

  showAddModal() {
    wx.showToast({
      title: '添加功能开发中',
      icon: 'none'
    });
  }
});
