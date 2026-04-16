const app = getApp();

Page({
  data: {
    tabs: [
      { key: 'all', name: '全部' },
      { key: 'coupon', name: '代金券' },
      { key: 'dish', name: '菜品' },
      { key: 'gift', name: '礼品' }
    ],
    typeOptions: [
      { key: 'coupon', name: '代金券' },
      { key: 'dish', name: '菜品' },
      { key: 'gift', name: '礼品' }
    ],
    currentTab: 'all',
    products: [],
    loading: true,
    showModal: false,
    editingId: null,
    formData: {
      name: '',
      type: 'coupon',
      price: '',
      stock: '',
      description: ''
    }
  },

  onLoad() {
    this.loadProducts();
  },

  onShow() {
    this.loadProducts();
  },

  switchTab(e) {
    const tab = e.currentTarget.dataset.tab;
    this.setData({ currentTab: tab });
    this.loadProducts();
  },

  loadProducts() {
    this.setData({ loading: true });
    const tab = this.data.currentTab;
    const url = tab === 'all' ? '/api/products' : `/api/products?type=${tab}`;

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

  getTypeText(type) {
    const map = {
      'coupon': '代金券',
      'dish': '菜品',
      'gift': '礼品'
    };
    return map[type] || type;
  },

  showAddModal() {
    this.setData({
      showModal: true,
      editingId: null,
      formData: {
        name: '',
        type: 'coupon',
        price: '',
        stock: '',
        description: ''
      }
    });
  },

  editProduct(e) {
    const id = e.currentTarget.dataset.id;
    const product = this.data.products.find(p => p.id === id);
    if (product) {
      this.setData({
        showModal: true,
        editingId: id,
        formData: {
          name: product.name || '',
          type: product.type || 'coupon',
          price: product.price || '',
          stock: product.stock || '',
          description: product.description || ''
        }
      });
    }
  },

  hideModal() {
    this.setData({ showModal: false });
  },

  preventBubble() {
    // 阻止事件冒泡，防止点击弹窗内容时关闭弹窗
  },

  onInputChange(e) {
    const field = e.currentTarget.dataset.field;
    const value = e.detail.value;
    this.setData({
      [`formData.${field}`]: value
    });
  },

  selectType(e) {
    const type = e.currentTarget.dataset.type;
    this.setData({
      'formData.type': type
    });
  },

  saveProduct() {
    const { formData, editingId } = this.data;

    if (!formData.name || !formData.name.trim()) {
      wx.showToast({
        title: '请输入商品名称',
        icon: 'none'
      });
      return;
    }

    wx.showLoading({ title: '保存中...' });

    const data = {
      name: formData.name.trim(),
      type: formData.type,
      price: formData.price ? parseFloat(formData.price) : null,
      stock: formData.stock ? parseInt(formData.stock) : 0,
      description: formData.description
    };

    const request = editingId
      ? app.request({
          url: `/api/products/${editingId}`,
          method: 'PUT',
          data
        })
      : app.request({
          url: '/api/products',
          method: 'POST',
          data
        });

    request.then(() => {
      wx.hideLoading();
      wx.showToast({
        title: '保存成功',
        icon: 'success'
      });
      this.hideModal();
      this.loadProducts();
    }).catch((err) => {
      wx.hideLoading();
      wx.showToast({
        title: err.message || '保存失败',
        icon: 'none'
      });
    });
  },

  toggleStatus(e) {
    const id = e.currentTarget.dataset.id;
    const checked = e.detail.value;
    const status = checked ? 1 : 0;

    app.request({
      url: `/api/products/${id}/status`,
      method: 'PUT',
      data: { status }
    }).then(() => {
      wx.showToast({
        title: checked ? '已上架' : '已下架',
        icon: 'success'
      });
      this.loadProducts();
    }).catch((err) => {
      wx.showToast({
        title: err.message || '操作失败',
        icon: 'none'
      });
      this.loadProducts();
    });
  }
});
