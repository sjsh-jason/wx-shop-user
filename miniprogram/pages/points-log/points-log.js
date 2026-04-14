const app = getApp();

Page({
  data: {
    logs: [],
    loading: true
  },

  onLoad() {
    this.loadPointsLogs();
  },

  onShow() {
    this.loadPointsLogs();
  },

  loadPointsLogs() {
    app.request({
      url: '/api/points/logs',
      method: 'GET'
    }).then((res) => {
      const logs = (res || []).map(item => {
        return {
          ...item,
          typeText: this.getTypeText(item.type),
          typeClass: item.points > 0 ? 'income' : 'expense',
          timeText: this.formatTime(item.createTime)
        };
      });
      this.setData({
        logs,
        loading: false
      });
    }).catch(() => {
      this.setData({
        logs: [],
        loading: false
      });
    });
  },

  getTypeText(type) {
    const map = {
      1: '每日签到',
      2: '消费获得',
      3: '兑换获得',
      4: '抽奖获得',
      5: '兑换消耗',
      6: '抽奖消耗'
    };
    return map[type] || '积分变动';
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
