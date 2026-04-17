const app = getApp();

Page({
  data: {
    ruleContent: ''
  },

  onLoad() {
    this.loadRule();
  },

  loadRule() {
    app.request({
      url: '/api/draw/activity/config',
      method: 'GET'
    }).then((res) => {
      if (res && res.ruleContent) {
        this.setData({
          ruleContent: res.ruleContent
        });
      }
    }).catch(() => {
    });
  }
});
