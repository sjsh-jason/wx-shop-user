const app = getApp();

Page({
  data: {
    customers: [],
    searchKeyword: '',
    loading: true,
    totalCustomers: 0,
    todayNew: 0,
    activeCustomers: 0
  },

  onLoad() {
    this.loadCustomers();
  },

  onShow() {
    this.loadCustomers();
  },

  loadCustomers() {
    this.setData({ loading: true });

    const mockCustomers = [
      { id: 1, nickname: '美食达人', avatar: '', phone: '138****8888', points: 2580, level: 3, createTime: '2026-03-15' },
      { id: 2, nickname: '小王', avatar: '', phone: '139****9999', points: 1200, level: 2, createTime: '2026-03-20' },
      { id: 3, nickname: '吃货一号', avatar: '', phone: '137****7777', points: 860, level: 2, createTime: '2026-04-01' },
      { id: 4, nickname: '美食探索者', avatar: '', phone: '136****6666', points: 420, level: 1, createTime: '2026-04-10' },
      { id: 5, nickname: '新用户', avatar: '', phone: '', points: 50, level: 1, createTime: '2026-04-14' }
    ];

    this.setData({
      customers: mockCustomers,
      totalCustomers: 128,
      todayNew: 5,
      activeCustomers: 86,
      loading: false
    });
  },

  onSearchInput(e) {
    const keyword = e.detail.value;
    this.setData({ searchKeyword: keyword });
  },

  formatDate(timeStr) {
    if (!timeStr) return '';
    const date = new Date(timeStr.replace(/-/g, '/'));
    const m = (date.getMonth() + 1).toString().padStart(2, '0');
    const d = date.getDate().toString().padStart(2, '0');
    return `${m}-${d}`;
  }
});
