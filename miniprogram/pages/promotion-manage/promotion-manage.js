const app = getApp();

Page({
  data: {
    activities: [],
    products: [],
    loading: true,
    showModal: false,
    showProductPicker: false,
    showTimePickerModal: false,
    editingId: null,
    selectedProduct: null,
    timePickerField: null,
    timePickerValue: [0, 0, 0],
    hours: [],
    minutes: [],
    seconds: [],
    formData: {
      productId: null,
      name: '',
      originalPrice: '',
      promotionPrice: '',
      stock: '',
      startDate: '',
      startTime: '00:00:00',
      endDate: '',
      endTime: '23:59:59'
    }
  },

  onLoad() {
    this.initTimePickerData();
    this.loadActivities();
    this.loadProducts();
  },

  onShow() {
    this.loadActivities();
  },

  initTimePickerData() {
    const hours = [];
    const minutes = [];
    const seconds = [];
    for (let i = 0; i < 24; i++) {
      hours.push(i.toString().padStart(2, '0'));
    }
    for (let i = 0; i < 60; i++) {
      minutes.push(i.toString().padStart(2, '0'));
      seconds.push(i.toString().padStart(2, '0'));
    }
    this.setData({ hours, minutes, seconds });
  },

  loadActivities() {
    this.setData({ loading: true });

    app.request({
      url: '/api/promotion/products/all',
      method: 'GET'
    }).then((res) => {
      this.setData({
        activities: res || [],
        loading: false
      });
    }).catch(() => {
      this.setData({
        activities: [],
        loading: false
      });
    });
  },

  loadProducts() {
    app.request({
      url: '/api/products/active',
      method: 'GET'
    }).then((res) => {
      this.setData({
        products: res || []
      });
    }).catch(() => {
      this.setData({
        products: []
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
      selectedProduct: null,
      formData: {
        productId: null,
        name: '',
        originalPrice: '',
        promotionPrice: '',
        stock: '',
        startDate: '',
        startTime: '00:00:00',
        endDate: '',
        endTime: '23:59:59'
      }
    });
  },

  editActivity(e) {
    const id = e.currentTarget.dataset.id;
    const activity = this.data.activities.find(p => p.id === id);
    if (activity) {
      const selectedProduct = activity.productId
        ? this.data.products.find(p => p.id === activity.productId) || null
        : null;
      this.setData({
        showModal: true,
        editingId: id,
        selectedProduct,
        formData: {
          productId: activity.productId || null,
          name: activity.name || '',
          originalPrice: activity.originalPrice || '',
          promotionPrice: activity.promotionPrice || '',
          stock: activity.stock || '',
          startDate: activity.startTime ? activity.startTime.split(' ')[0] : '',
          startTime: activity.startTime ? activity.startTime.split(' ')[1] || '00:00:00' : '00:00:00',
          endDate: activity.endTime ? activity.endTime.split(' ')[0] : '',
          endTime: activity.endTime ? activity.endTime.split(' ')[1] || '23:59:59' : '23:59:59'
        }
      });
    }
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

  showTimePicker(e) {
    const field = e.currentTarget.dataset.field;
    const timeStr = this.data.formData[field] || '00:00:00';
    const parts = timeStr.split(':');
    const hour = parseInt(parts[0] || '0');
    const minute = parseInt(parts[1] || '0');
    const second = parseInt(parts[2] || '0');
    this.setData({
      showTimePickerModal: true,
      timePickerField: field,
      timePickerValue: [hour, minute, second]
    });
  },

  hideTimePicker() {
    this.setData({ showTimePickerModal: false });
  },

  onTimePickerChange(e) {
    this.setData({
      timePickerValue: e.detail.value
    });
  },

  confirmTimePicker() {
    const { timePickerField, timePickerValue, hours, minutes, seconds } = this.data;
    const timeStr = `${hours[timePickerValue[0]]}:${minutes[timePickerValue[1]]}:${seconds[timePickerValue[2]]}`;
    this.setData({
      [`formData.${timePickerField}`]: timeStr,
      showTimePickerModal: false
    });
  },

  preventBubble() {
    // 阻止事件冒泡，防止点击弹窗内容时关闭弹窗
  },

  selectProduct(e) {
    const product = e.currentTarget.dataset.product;
    this.setData({
      selectedProduct: product,
      'formData.productId': product.id,
      'formData.name': product.name,
      'formData.originalPrice': product.price,
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

  onDateChange(e) {
    const field = e.currentTarget.dataset.field;
    const value = e.detail.value;
    this.setData({
      [`formData.${field}`]: value
    });
  },

  saveActivity() {
    const { formData, editingId } = this.data;

    if (!formData.name || !formData.name.trim()) {
      wx.showToast({
        title: '请输入活动名称',
        icon: 'none'
      });
      return;
    }

    if (!formData.promotionPrice) {
      wx.showToast({
        title: '请输入促销价',
        icon: 'none'
      });
      return;
    }

    if (!formData.startDate || !formData.startTime || !formData.endDate || !formData.endTime) {
      wx.showToast({
        title: '请选择活动时间',
        icon: 'none'
      });
      return;
    }

    wx.showLoading({ title: '保存中...' });

    const data = {
      productId: formData.productId,
      name: formData.name.trim(),
      originalPrice: formData.originalPrice ? parseFloat(formData.originalPrice) : null,
      promotionPrice: parseFloat(formData.promotionPrice),
      stock: formData.stock ? parseInt(formData.stock) : 0,
      startTime: formData.startDate + ' ' + formData.startTime,
      endTime: formData.endDate + ' ' + formData.endTime
    };

    const request = editingId
      ? app.request({
          url: `/api/promotion/products/${editingId}`,
          method: 'PUT',
          data
        })
      : app.request({
          url: '/api/promotion/products',
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
      this.loadActivities();
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
      url: `/api/promotion/products/${id}/status`,
      method: 'PUT',
      data: { status }
    }).then(() => {
      wx.showToast({
        title: checked ? '已启用' : '已禁用',
        icon: 'success'
      });
      this.loadActivities();
    }).catch((err) => {
      wx.showToast({
        title: err.message || '操作失败',
        icon: 'none'
      });
      this.loadActivities();
    });
  }
});
