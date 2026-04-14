const app = getApp();

Page({
  data: {
    verifying: false
  },

  onLoad() {
    // 页面加载时检查登录状态
    app.checkLogin();
  },

  // 点击扫码按钮
  handleScan() {
    wx.scanCode({
      onlyFromCamera: true,
      scanType: ['qrCode'],
      success: (res) => {
        const qrCode = res.result;
        if (!qrCode) {
          wx.showToast({
            title: '无效的二维码',
            icon: 'none'
          });
          return;
        }
        this.verifyReservation(qrCode);
      },
      fail: (err) => {
        if (err.errMsg && err.errMsg.includes('cancel')) {
          // 用户取消扫码，不提示错误
          return;
        }
        wx.showToast({
          title: '扫码失败',
          icon: 'none'
        });
      }
    });
  },

  // 手动输入核销码
  handleManualInput() {
    wx.showModal({
      title: '输入核销码',
      editable: true,
      placeholderText: '请输入顾客的核销码',
      success: (res) => {
        if (res.confirm && res.content) {
          this.verifyReservation(res.content.trim());
        }
      }
    });
  },

  // 调用核销接口（先尝试预约核销，失败后尝试兑换核销）
  verifyReservation(qrCode) {
    if (this.data.verifying) {
      return;
    }
    this.setData({ verifying: true });

    app.request({
      url: '/api/reservation/verifyByQrCode',
      method: 'POST',
      data: { qrCode }
    }).then(() => {
      wx.showToast({
        title: '预约核销成功',
        icon: 'success'
      });
    }).catch(() => {
      // 预约核销失败，尝试兑换核销
      app.request({
        url: '/api/exchange/verifyByQrCode',
        method: 'POST',
        data: { qrCode }
      }).then(() => {
        wx.showToast({
          title: '兑换核销成功',
          icon: 'success'
        });
      }).catch((err) => {
        wx.showModal({
          title: '核销失败',
          content: err.message || '未找到有效的预约或兑换记录，请检查核销码',
          showCancel: false
        });
      });
    }).finally(() => {
      this.setData({ verifying: false });
    });
  }
});
