const app = getApp();

// 九宫格位置映射：8个奖品围绕中心，索引4为中心按钮
const GRID_POSITIONS = [
  { isCenter: false, prizeIndex: 0 },
  { isCenter: false, prizeIndex: 1 },
  { isCenter: false, prizeIndex: 2 },
  { isCenter: false, prizeIndex: 3 },
  { isCenter: true },
  { isCenter: false, prizeIndex: 4 },
  { isCenter: false, prizeIndex: 5 },
  { isCenter: false, prizeIndex: 6 },
  { isCenter: false, prizeIndex: 7 }
];

Page({
  data: {
    activityTitle: '幸运大抽奖',
    prizes: [],
    gridPositions: GRID_POSITIONS,
    remainingCount: 0,
    remainingTime: 0,
    currentIndex: -1,
    isRunning: false,
    targetIndex: -1,
    ruleContent: ''
  },

  onLoad() {
    this.checkUserAuth();
  },

  onShow() {
    if (app.globalData.token) {
      this.loadActivityConfig();
      this.loadPrizes();
      this.loadChanceStatus();
    }
  },

  // 校验用户身份：未授权→引导登录；非会员→引导注册
  checkUserAuth() {
    const token = app.globalData.token;
    if (!token) {
      wx.showModal({
        title: '提示',
        content: '您尚未登录，请先授权登录',
        confirmText: '去登录',
        showCancel: false,
        success: () => {
          wx.reLaunch({
            url: '/pages/login/login'
          });
        }
      });
      return;
    }

    app.request({
      url: '/api/user/info',
      method: 'GET'
    }).then((res) => {
      app.setUserInfo(res);
      if (!res.level || res.level === 0) {
        wx.showModal({
          title: '提示',
          content: '您还不是会员，请先注册成为会员',
          confirmText: '去注册',
          showCancel: false,
          success: () => {
            wx.switchTab({
              url: '/pages/index/index'
            });
          }
        });
        return;
      }
      this.loadActivityConfig();
      this.loadPrizes();
      this.loadChanceStatus();
    }).catch(() => {
      wx.showModal({
        title: '提示',
        content: '登录状态已过期，请重新登录',
        confirmText: '去登录',
        showCancel: false,
        success: () => {
          wx.reLaunch({
            url: '/pages/login/login'
          });
        }
      });
    });
  },

  loadActivityConfig() {
    app.request({
      url: '/api/draw/activity/config',
      method: 'GET'
    }).then((res) => {
      if (res) {
        this.setData({
          ruleContent: res.ruleContent || ''
        });
        if (res.endTime) {
          this.startCountdown(new Date(res.endTime.replace(/-/g, '/')).getTime());
        }
      }
    }).catch(() => {
    });
  },

  startCountdown(endTime) {
    const updateCountdown = () => {
      const now = Date.now();
      const diff = endTime - now;
      if (diff <= 0) {
        this.setData({ remainingTime: 0 });
        return;
      }
      this.setData({ remainingTime: diff });
      setTimeout(updateCountdown, 1000);
    };
    updateCountdown();
  },

  formatCountdown(ms) {
    const seconds = Math.floor(ms / 1000);
    const h = Math.floor(seconds / 3600);
    const m = Math.floor((seconds % 3600) / 60);
    const s = seconds % 60;
    return `${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`;
  },

  loadPrizes() {
    this.setData({
      prizes: [
        { id: 1, name: '招牌三合一（特大份）', iconText: '特' },
        { id: 2, name: '招牌三合一（中份）', iconText: '中' },
        { id: 3, name: '香酥鸡柳（中份）', iconText: '鸡' },
        { id: 4, name: '全场通用5元券', iconText: '券' },
        { id: 5, name: '300积分', iconText: '300' },
        { id: 6, name: '250积分', iconText: '250' },
        { id: 7, name: '200积分', iconText: '200' },
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
    const icons = ['特', '中', '鸡', '券', '300', '250', '200', '谢'];
    return icons[index % 8];
  },

  loadChanceStatus() {
    app.request({
      url: '/api/draw/chance/status',
      method: 'GET'
    }).then((res) => {
      this.setData({
        remainingCount: res.remainingCount || 0
      });
    }).catch(() => {
    });
  },

  startDraw() {
    if (this.data.isRunning) {
      return;
    }

    if (this.data.remainingCount <= 0) {
      this.showNoChanceModal();
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
      const prizeIndex = this.data.prizes.findIndex(p => p.id === prizeId);
      // 将奖品索引映射到九宫格位置索引
      const gridIndex = this.prizeIndexToGridIndex(prizeIndex >= 0 ? prizeIndex : 0);
      this.setData({
        targetIndex: gridIndex
      });

      this.runAnimation(() => {
        this.setData({
          isRunning: false
        });

        this.loadChanceStatus();
        this.showResultModal(res);
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

  // 奖品索引转九宫格位置索引
  prizeIndexToGridIndex(prizeIndex) {
    const mapping = [0, 1, 2, 3, 5, 6, 7, 8];
    return mapping[prizeIndex % 8];
  },

  showNoChanceModal() {
    wx.showModal({
      title: '抽奖次数不足',
      content: '去消费 / 签到获取更多抽奖机会',
      confirmText: '去消费领取',
      cancelText: '去签到',
      success: (res) => {
        if (res.confirm) {
          wx.switchTab({
            url: '/pages/index/index'
          });
        } else if (res.cancel) {
          wx.navigateTo({
            url: '/pages/check-in/check-in'
          });
        }
      }
    });
  },

  showResultModal(draw) {
    const prize = this.data.prizes.find(p => p.id === draw.prizeId);
    let content = `获得：${draw.prizeName}`;
    if (prize && prize.description) {
      content += `\n${prize.description}`;
    }

    wx.showModal({
      title: draw.prizeType === 0 ? '抽奖结果' : '恭喜中奖',
      content: content,
      showCancel: false,
      success: () => {
        if (draw.prizeType === 1 && draw.points > 0) {
          app.request({
            url: '/api/user/info',
            method: 'GET'
          }).then((userRes) => {
            app.setUserInfo(userRes);
          });
        }
      }
    });
  },

  runAnimation(callback) {
    const totalSteps = 32 + this.data.targetIndex; // 约4圈 + 目标位置
    let currentStep = 0;

    const interval = setInterval(() => {
      const nextIndex = currentStep % 8;
      // 跳过中心位置4，将0-7序列映射到九宫格位置
      const gridPositions = [0, 1, 2, 3, 5, 6, 7, 8];
      this.setData({
        currentIndex: gridPositions[nextIndex]
      });

      currentStep++;
      if (currentStep >= totalSteps) {
        clearInterval(interval);
        if (callback) callback();
      }
    }, currentStep < 20 ? 60 : (currentStep < 28 ? 100 : 150));
  },

  goToRule() {
    wx.navigateTo({
      url: '/pages/draw-rule/draw-rule'
    });
  },

  goToRecords() {
    wx.navigateTo({
      url: '/pages/draw-records/draw-records'
    });
  }
});
