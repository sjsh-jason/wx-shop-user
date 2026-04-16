const app = getApp();

Page({
  data: {
    prizes: [],
    myDraws: [],
    currentIndex: -1,
    isRunning: false,
    hasDrawn: false,
    targetIndex: -1
  },

  onLoad() {
    this.loadPrizes();
    this.loadDrawStatus();
    this.loadMyDraws();
  },

  onShow() {
    this.loadDrawStatus();
  },

  loadPrizes() {
    this.setData({
      prizes: [
        { id: 1, name: '免费饮品', iconText: '饮' },
        { id: 2, name: '200积分', iconText: '200' },
        { id: 3, name: '¥10代金券', iconText: '¥10' },
        { id: 4, name: '50积分', iconText: '50' },
        { id: 5, name: '招牌菜', iconText: '菜' },
        { id: 6, name: '¥50大额券', iconText: '¥50' },
        { id: 7, name: '500积分', iconText: '500' },
        { id: 8, name: '谢谢参与', iconText: '谢' }
      ]
    });

    app.request({
      url: '/api/prizes',
      method: 'GET'
    }).then((res) => {
      if (res && res.length > 0) {
        const prizes = res.map((item, index) => ({
          ...item,
          iconText: this.getIconText(item, index)
        }));
        this.setData({ prizes });
      }
    }).catch(() => {
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

  loadDrawStatus() {
    app.request({
      url: '/api/lucky-draw/status',
      method: 'GET'
    }).then((res) => {
      this.setData({
        hasDrawn: res.hasDrawn || false
      });
    }).catch(() => {
    });
  },

  loadMyDraws() {
    app.request({
      url: '/api/lucky-draw/my',
      method: 'GET'
    }).then((res) => {
      this.setData({
        myDraws: res || []
      });
    }).catch(() => {
    });
  },

  startDraw() {
    if (this.data.isRunning || this.data.hasDrawn) {
      return;
    }

    this.setData({
      isRunning: true,
      currentIndex: -1
    });

    app.request({
      url: '/api/lucky-draw',
      method: 'POST'
    }).then((res) => {
      const prizeId = res.prizeId;
      const targetIndex = this.data.prizes.findIndex(p => p.id === prizeId);
      this.setData({
        targetIndex: targetIndex >= 0 ? targetIndex : 0
      });

      this.runAnimation(() => {
        this.setData({
          isRunning: false,
          hasDrawn: true
        });

        this.loadMyDraws();

        // 判断奖品类型
        if (res.prizeType === 'points' || res.points > 0) {
          // 积分奖励：自动到账
          wx.showModal({
            title: '恭喜中奖',
            content: `获得：${res.prizeName}${res.points ? ' (' + res.points + '积分)' : ''}`,
            showCancel: false,
            success: () => {
              if (res.points > 0) {
                app.request({
                  url: '/api/user/info',
                  method: 'GET'
                }).then((userRes) => {
                  app.setUserInfo(userRes);
                });
              }
            }
          });
        } else if (res.prizeType === 'product' || res.prizeType === 'coupon') {
          // 实物商品或代金券：跳转到领取详情页
          wx.showModal({
            title: '恭喜中奖',
            content: `获得：${res.prizeName}，请前往领取`,
            showCancel: false,
            confirmText: '去领取',
            success: () => {
              if (res.exchangeRecordId) {
                wx.redirectTo({
                  url: '/pages/exchanged-detail/exchanged-detail?id=' + res.exchangeRecordId
                });
              }
            }
          });
        } else {
          // 谢谢参与
          wx.showModal({
            title: '抽奖结果',
            content: res.prizeName || '谢谢参与',
            showCancel: false
          });
        }
      });
    }).catch((err) => {
      this.setData({
        isRunning: false
      });
      wx.showToast({
        title: err.message || '抽奖失败',
        icon: 'none'
      });
    });
  },

  runAnimation(callback) {
    const totalSteps = 24 + this.data.targetIndex;
    let currentStep = 0;
    const interval = setInterval(() => {
      const nextIndex = currentStep % 8;
      this.setData({
        currentIndex: nextIndex
      });

      currentStep++;
      if (currentStep >= totalSteps) {
        clearInterval(interval);
        if (callback) callback();
      }
    }, currentStep < 16 ? 80 : (currentStep < 20 ? 120 : 200));
  },

  formatTime(timeStr) {
    if (!timeStr) return '';
    const date = new Date(timeStr.replace(/-/g, '/'));
    const m = (date.getMonth() + 1).toString().padStart(2, '0');
    const d = date.getDate().toString().padStart(2, '0');
    const h = date.getHours().toString().padStart(2, '0');
    const min = date.getMinutes().toString().padStart(2, '0');
    return `${m}-${d} ${h}:${min}`;
  }
});
