# 迭代需求：业务流程完整实现

**版本**: v1.0  
**创建日期**: 2026-04-16  
**基于**: API_TEST_REPORT.md 和 REQUIREMENTS.md  

---

## 一、迭代背景

根据测试报告结果，当前项目核心C端功能已完整实现，但B端管理功能存在缺失，导致需求文档中描述的三大业务流程无法完整闭环。本次迭代旨在补充缺失的B端管理功能，实现业务流程的完整贯通。

---

## 二、当前实现情况回顾

### 2.1 已完整实现的功能
- ✅ C端全部10个功能（扫码登录、促销浏览、抢购预约、积分商城、积分兑换、待领取列表、生成二维码、幸运抽奖、每日签到、积分明细）
- ✅ B端基础功能（扫码核销预约、扫码核销兑换、客户列表）

### 2.2 缺失/不完整的功能
- ❌ 促销商品管理（上架/编辑/下架）
- ❌ 积分规则配置（签到积分、消费积分比例、兑换积分比例）
- ⚠️ 抽奖管理（仅奖品列表展示，缺少配置功能）

---

## 三、业务流程完整性需求

### 3.1 促销抢购流程闭环

**当前状态**: C端可浏览和预约，但B端无法管理促销商品

**需要补充的功能**:
1. **促销商品列表页**（商家端）
   - 展示所有促销商品（含已下架）
   - 显示商品状态（上架/下架）、库存、销售数据
   
2. **促销商品新增/编辑页**（商家端）
   - 商品图片上传
   - 商品名称、描述输入
   - 原价、促销价设置
   - 库存数量设置
   - 活动时间设置（开始时间、结束时间）
   
3. **促销商品上下架操作**（商家端）
   - 上架商品
   - 下架商品
   - 删除商品（逻辑删除）

**涉及接口补充**:
```
后端已有（前端未调用）:
- GET /api/promotion/products/{id} - 获取促销商品详情

需要新增:
- POST /api/promotion/products - 创建促销商品
- PUT /api/promotion/products/{id} - 更新促销商品
- DELETE /api/promotion/products/{id} - 删除促销商品
- POST /api/promotion/products/{id}/publish - 上架促销商品
- POST /api/promotion/products/{id}/unpublish - 下架促销商品
```

---

### 3.2 积分兑换流程闭环

**当前状态**: C端可浏览和兑换，但B端无法配置积分规则

**需要补充的功能**:
1. **积分规则配置页**（商家端）
   - 每日签到积分设置
   - 抢购预约积分规则（消费金额 → 积分比例）
   - 积分兑换积分规则（商品价值 → 积分奖励比例）
   - 规则启用/禁用状态
   
2. **积分规则列表**（商家端）
   - 展示所有积分规则
   - 显示规则当前值和状态
   - 快速编辑入口

**涉及接口补充**:
```
后端已有（前端未调用）:
- GET /api/points/rules - 获取所有积分规则

需要新增:
- PUT /api/points/rules/{id} - 更新积分规则
- POST /api/points/rules/{id}/enable - 启用规则
- POST /api/points/rules/{id}/disable - 禁用规则
```

---

### 3.3 抽奖活动流程闭环

**当前状态**: C端可参与抽奖，但B端无法管理奖品和配置活动

**需要补充的功能**:
1. **奖品管理页**（商家端）
   - 奖品列表展示（已有页面，需扩展）
   - 新增奖品功能
     - 奖品名称、描述
     - 奖品类型（谢谢参与/积分奖励/实物商品/代金券）
     - 奖品图片
     - 中奖概率设置
     - 库存数量（实物奖品）
     - 积分数量（积分奖励）
     - 代金券金额（代金券）
   - 编辑奖品功能
   - 删除奖品功能
   - 奖品排序
   
2. **抽奖活动配置页**（商家端）
   - 活动开启/关闭
   - 参与所需积分设置
   - 每日参与次数限制
   - 中奖记录查看

**涉及接口补充**:
```
后端已有（前端未调用）:
- GET /api/prizes/{id} - 获取奖品详情

需要新增:
- POST /api/prizes - 创建奖品
- PUT /api/prizes/{id} - 更新奖品
- DELETE /api/prizes/{id} - 删除奖品
- POST /api/prizes/{id}/enable - 启用奖品
- POST /api/prizes/{id}/disable - 禁用奖品
- GET /api/lucky-draw/records - 中奖记录列表（商家端）
- GET /api/lucky-draw/config - 获取抽奖配置
- PUT /api/lucky-draw/config - 更新抽奖配置
```

---

## 四、迭代需求清单

### 4.1 前端页面需求

| 序号 | 页面名称 | 路径 | 功能描述 | 优先级 |
|-----|---------|------|---------|--------|
| 1 | 促销商品管理列表 | pages/promotion-manage/promotion-manage | 展示促销商品列表，支持上下架、删除 | P0 |
| 2 | 促销商品编辑 | pages/promotion-edit/promotion-edit | 新增/编辑促销商品信息 | P0 |
| 3 | 积分规则配置 | pages/points-rule/points-rule | 配置各类积分规则 | P0 |
| 4 | 奖品编辑 | pages/prize-edit/prize-edit | 新增/编辑奖品信息 | P0 |
| 5 | 抽奖活动配置 | pages/lottery-config/lottery-config | 配置抽奖活动参数、查看中奖记录 | P1 |

### 4.2 后端接口需求

| 序号 | 接口地址 | 请求方法 | 功能描述 | 优先级 |
|-----|---------|---------|---------|--------|
| 1 | `/api/promotion/products` | POST | 创建促销商品 | P0 |
| 2 | `/api/promotion/products/{id}` | PUT | 更新促销商品 | P0 |
| 3 | `/api/promotion/products/{id}` | DELETE | 删除促销商品 | P0 |
| 4 | `/api/promotion/products/{id}/publish` | POST | 上架促销商品 | P0 |
| 5 | `/api/promotion/products/{id}/unpublish` | POST | 下架促销商品 | P0 |
| 6 | `/api/points/rules/{id}` | PUT | 更新积分规则 | P0 |
| 7 | `/api/points/rules/{id}/enable` | POST | 启用积分规则 | P0 |
| 8 | `/api/points/rules/{id}/disable` | POST | 禁用积分规则 | P0 |
| 9 | `/api/prizes` | POST | 创建奖品 | P0 |
| 10 | `/api/prizes/{id}` | PUT | 更新奖品 | P0 |
| 11 | `/api/prizes/{id}` | DELETE | 删除奖品 | P0 |
| 12 | `/api/prizes/{id}/enable` | POST | 启用奖品 | P0 |
| 13 | `/api/prizes/{id}/disable` | POST | 禁用奖品 | P0 |
| 14 | `/api/lucky-draw/records` | GET | 获取中奖记录列表（商家端） | P1 |
| 15 | `/api/lucky-draw/config` | GET | 获取抽奖配置 | P1 |
| 16 | `/api/lucky-draw/config` | PUT | 更新抽奖配置 | P1 |

---

## 五、验收标准

### 5.1 促销抢购流程验收
- [ ] 商家可成功新增促销商品并上架
- [ ] 商家可编辑已有促销商品信息
- [ ] 商家可下架/删除促销商品
- [ ] C端仅能看到已上架且在活动时间内的促销商品
- [ ] 促销商品库存扣减正确

### 5.2 积分兑换流程验收
- [ ] 商家可配置每日签到积分数量
- [ ] 商家可配置消费金额与积分比例
- [ ] 商家可配置兑换商品积分奖励比例
- [ ] 积分规则变更后实时生效
- [ ] 用户积分变动符合配置的规则

### 5.3 抽奖活动流程验收
- [ ] 商家可新增/编辑/删除奖品
- [ ] 商家可设置各奖品中奖概率
- [ ] 商家可开启/关闭抽奖活动
- [ ] 商家可设置参与所需积分
- [ ] 抽奖结果符合设定的概率分布
- [ ] 商家可查看中奖记录

---

## 六、优先级说明

- **P0**: 必须实现，业务流程闭环的核心功能
- **P1**: 重要功能，提升管理体验的增强功能

---

## 七、参考文件

- API测试报告: `API_TEST_REPORT.md`
- 需求文档: `.planning/REQUIREMENTS.md`
- 详细需求: `doc/requiremenDoc.md`
