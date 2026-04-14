const app = getApp();

Page({
  data: {
    userInfo: null,
    currentCategory: 'all',
    categories: [
      { key: 'all', name: '全部' },
      { key: 'coupon', name: '代金券' },
      { key: 'dish', name: '菜品兑换' },
      { key: 'gift', name: '周边礼品' }
    ],
    products: [],
    loading: true
  },

  onLoad() {
    this.setData({
      userInfo: app.globalData.userInfo
    });
    this.loadProducts();
  },

  onShow() {
    this.setData({
      userInfo: app.globalData.userInfo
    });
  },

  switchCategory(e) {
    const category = e.currentTarget.dataset.category;
    this.setData({
      currentCategory: category,
      loading: true
    });
    this.loadProducts();
  },

  loadProducts() {
    const category = this.data.currentCategory;
    let url = '/api/points/products';
    if (category !== 'all') {
      url += `?category=${category}`;
    }

    app.request({
      url,
      method: 'GET'
    }).then((res) => {
      this.setData({
        products: res || [],
        loading: false
      });
    }).catch(() => {
      this.setData({
        products: [],
        loading: false
      });
    });
  },

  goToPointsLog() {
    wx.navigateTo({
      url: '/pages/points-log/points-log'
    });
  },

  exchangeProduct(e) {
    const product = e.currentTarget.dataset.product;
    const userInfo = this.data.userInfo;

    if (!userInfo || userInfo.points < product.points) {
      wx.showToast({
        title: '积分不足',
        icon: 'none'
      });
      return;
    }

    if (product.stock <= 0) {
      wx.showToast({
        title: '库存不足',
        icon: 'none'
      });
      return;
    }

    wx.showModal({
      title: '确认兑换',
      content: `确定用 ${product.points} 积分兑换「${product.name}」吗？`,
      success: (res) => {
        if (res.confirm) {
          this.doExchange(product);
        }
      }
    });
  },

  doExchange(product) {
    wx.showLoading({ title: '兑换中...' });

    app.request({
      url: '/api/exchange',
      method: 'POST',
      data: { productId: product.id }
    }).then((res) => {
      wx.hideLoading();
      wx.showModal({
        title: '兑换成功',
        content: '您可以在「我的-待领取」中查看并出示二维码进行核销',
        showCancel: false,
        success: () => {
          this.loadProducts();
          app.request({
            url: '/api/user/info',
            method: 'GET'
          }).then((userRes) => {
            app.setUserInfo(userRes);
            this.setData({
              userInfo: userRes
            });
          });
        }
      });
    }).catch((err) => {
      wx.hideLoading();
      wx.showToast({
        title: err.message || '兑换失败',
        icon: 'none'
      });
    });
  }
});
