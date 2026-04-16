const app = getApp();

Page({
  data: {
    userInfo: null,
    currentCategory: 'coupon',
    categories: [
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
    let url = '/api/points/products?category=' + category;

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
        products: [
          { id: 1, name: '全场通用10元券', points: 500, stock: 100, category: 'coupon', image: '', type: 'coupon' },
          { id: 2, name: '满100减30券', points: 1200, stock: 50, category: 'coupon', image: '', type: 'coupon' },
          { id: 3, name: '招牌冻柠茶', points: 800, stock: 200, category: 'dish', image: '', type: 'dish' },
          { id: 4, name: '脆皮炸鸡翅', points: 1500, stock: 80, category: 'dish', image: '', type: 'dish' }
        ],
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
    console.log('exchangeProduct 被触发', e);
    const product = e.currentTarget.dataset.product;
    console.log('product:', product);
    const userInfo = this.data.userInfo;
    console.log('userInfo:', userInfo);

    if (!userInfo) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      return;
    }

    if (userInfo.points < product.points) {
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
        title: err.message || '兑换失败，请重试',
        icon: 'none'
      });
    });
  }
});
