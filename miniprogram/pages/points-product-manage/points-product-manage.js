const app = getApp();

Page({
  data: {
    products: [],
    sourceProducts: [],
    loading: true,
    showModal: false,
    showProductPicker: false,
    editingId: null,
    selectedProduct: null,
    formData: {
      productId: null,
      name: '',
      image: '',
      category: '',
      points: '',
      stock: ''
    }
  },

  onLoad() {
    this.loadPointsProducts();
    this.loadSourceProducts();
  },

  onShow() {
    this.loadPointsProducts();
  },

  loadPointsProducts() {
    this.setData({ loading: true });

    app.request({
      url: '/api/points/products/all',
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

  loadSourceProducts() {
    app.request({
      url: '/api/products/active',
      method: 'GET'
    }).then((res) => {
      this.setData({
        sourceProducts: res || []
      });
    }).catch(() => {
      this.setData({
        sourceProducts: []
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

  getFullImageUrl(url) {
    if (!url) return '';
    if (url.startsWith('http://') || url.startsWith('https://')) {
      return url;
    }
    return app.globalData.baseUrl + url;
  },

  showAddModal() {
    this.setData({
      showModal: true,
      editingId: null,
      selectedProduct: null,
      formData: {
        productId: null,
        name: '',
        image: '',
        category: '',
        points: '',
        stock: ''
      }
    });
  },

  editProduct(e) {
    const id = e.currentTarget.dataset.id;
    const product = this.data.products.find(p => p.id === id);
    if (product) {
      const selectedProduct = product.productId
        ? this.data.sourceProducts.find(p => p.id === product.productId) || null
        : null;
      this.setData({
        showModal: true,
        editingId: id,
        selectedProduct,
        formData: {
          productId: product.productId || null,
          name: product.name || '',
          image: this.getFullImageUrl(product.image),
          category: product.category || '',
          points: product.points || '',
          stock: product.stock || ''
        }
      });
    }
  },

  noop() {
    // 阻止事件冒泡，避免点击弹窗内容时关闭弹窗
  },

  hideModal() {
    this.setData({ showModal: false });
  },

  showProductPicker() {
    this.setData({ showProductPicker: true });
  },

  hideProductPicker() {
    this.setData({ showProductPicker: false });
  },

  selectProduct(e) {
    const product = e.currentTarget.dataset.product;
    this.setData({
      selectedProduct: product,
      'formData.productId': product.id,
      'formData.name': product.name,
      'formData.image': this.getFullImageUrl(product.image),
      'formData.category': product.type || '',
      showProductPicker: false
    });
  },

  onInputChange(e) {
    const field = e.currentTarget.dataset.field;
    const value = e.detail.value;
    this.setData({
      [`formData.${field}`]: value
    });
  },

  chooseImage() {
    wx.chooseImage({
      count: 1,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const tempFilePath = res.tempFilePaths[0];
        this.uploadImage(tempFilePath);
      }
    });
  },

  uploadImage(filePath) {
    wx.showLoading({ title: '上传中...' });

    wx.uploadFile({
      url: app.globalData.baseUrl + '/api/upload',
      filePath: filePath,
      name: 'file',
      header: {
        'Authorization': 'Bearer ' + (wx.getStorageSync('token') || '')
      },
      success: (res) => {
        wx.hideLoading();
        const data = JSON.parse(res.data);
        if (data.code === 200) {
          this.setData({
            'formData.image': data.data.url
          });
          wx.showToast({
            title: '上传成功',
            icon: 'success'
          });
        } else {
          wx.showToast({
            title: data.message || '上传失败',
            icon: 'none'
          });
        }
      },
      fail: () => {
        wx.hideLoading();
        wx.showToast({
          title: '上传失败',
          icon: 'none'
        });
      }
    });
  },

  previewImage() {
    const image = this.getFullImageUrl(this.data.formData.image);
    if (image) {
      wx.previewImage({
        urls: [image],
        current: image
      });
    }
  },

  deleteImage(e) {
    e.stopPropagation();
    this.setData({
      'formData.image': ''
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

    if (!formData.points) {
      wx.showToast({
        title: '请输入所需积分',
        icon: 'none'
      });
      return;
    }

    wx.showLoading({ title: '保存中...' });

    const data = {
      productId: formData.productId,
      name: formData.name.trim(),
      image: formData.image,
      category: formData.category,
      points: parseInt(formData.points),
      stock: formData.stock ? parseInt(formData.stock) : 0
    };

    const request = editingId
      ? app.request({
          url: `/api/points/products/${editingId}`,
          method: 'PUT',
          data
        })
      : app.request({
          url: '/api/points/products',
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
      this.loadPointsProducts();
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
      url: `/api/points/products/${id}/status`,
      method: 'PUT',
      data: { status }
    }).then(() => {
      wx.showToast({
        title: checked ? '已上架' : '已下架',
        icon: 'success'
      });
      this.loadPointsProducts();
    }).catch((err) => {
      wx.showToast({
        title: err.message || '操作失败',
        icon: 'none'
      });
      this.loadPointsProducts();
    });
  }
});
