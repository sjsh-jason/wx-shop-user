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
      description: '',
      image: ''
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

  getFullImageUrl(url) {
    if (!url) return '';
    if (url.startsWith('http://') || url.startsWith('https://')) {
      return url;
    }
    return app.globalData.baseUrl + url;
  },

  loadProducts() {
    this.setData({ loading: true });
    const tab = this.data.currentTab;
    const url = tab === 'all' ? '/api/products' : `/api/products?type=${tab}`;

    app.request({
      url,
      method: 'GET'
    }).then((res) => {
      const products = (res || []).map(item => ({
        ...item,
        image: this.getFullImageUrl(item.image)
      }));
      this.setData({
        products,
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

  normalizeName(name) {
    return name.toLowerCase().replace(/[（）]/g, (c) => c === '（' ? '(' : ')');
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
        description: '',
        image: ''
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
          description: product.description || '',
          image: product.image || ''
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
        try {
          const data = JSON.parse(res.data);
          if (data.code === 200) {
            const imageUrl = this.getFullImageUrl(data.data.url);
            this.setData({
              'formData.image': imageUrl
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
        } catch (e) {
          wx.showToast({
            title: '上传失败',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        wx.hideLoading();
        wx.showToast({
          title: '上传失败',
          icon: 'none'
        });
      }
    });
  },

  previewImage() {
    const image = this.data.formData.image;
    if (image) {
      wx.previewImage({
        urls: [image],
        current: image
      });
    }
  },

  deleteImage() {
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

    const normalizedInput = this.normalizeName(formData.name.trim());
    const duplicate = this.data.products.find(p => {
      if (editingId && p.id === editingId) return false;
      return this.normalizeName(p.name) === normalizedInput;
    });
    if (duplicate) {
      wx.showToast({
        title: '商品名称已存在',
        icon: 'none'
      });
      return;
    }

    wx.showLoading({ title: '保存中...' });

    let imageUrl = formData.image;
    if (imageUrl && imageUrl.startsWith(app.globalData.baseUrl)) {
      imageUrl = imageUrl.substring(app.globalData.baseUrl.length);
    }

    const data = {
      name: formData.name.trim(),
      type: formData.type,
      price: formData.price ? parseFloat(formData.price) : null,
      stock: formData.stock ? parseInt(formData.stock) : 0,
      description: formData.description,
      image: imageUrl
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
