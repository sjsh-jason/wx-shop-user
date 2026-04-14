# Phase 3: 积分商城功能

## 阶段概览

| 项目 | 内容 |
|------|------|
| **阶段名称** | 积分商城功能 |
| **阶段编号** | 3 |
| **预计周期** | 1周 |
| **目标** | 实现积分规则、商品兑换、待领取管理、每日签到、积分明细 |

---

## 交付物清单

- [ ] 积分商品实体、Mapper、Service、Controller
- [ ] 兑换记录实体、Mapper、Service、Controller
- [ ] 积分明细实体、Mapper、Service、Controller
- [ ] 签到记录实体、Mapper、Service、Controller
- [ ] 积分规则实体、Mapper、Service、Controller
- [ ] 小程序积分商城页面（商品展示 + 兑换功能）
- [ ] 小程序每日签到功能
- [ ] 小程序积分明细页面
- [ ] 小程序待领取列表页面
- [ ] 商家端领取核销支持

---

## 详细任务清单

### 3.1 后端 - 积分商品模块

| ID | 任务 | 描述 | 优先级 |
|----|------|------|--------|
| T-3.1.1 | 创建 PointsProduct 实体 | 积分商品实体类 | P0 |
| T-3.1.2 | 创建 PointsProductMapper | Mapper 接口 | P0 |
| T-3.1.3 | 创建 PointsProductService | 业务逻辑服务 | P0 |
| T-3.1.4 | 创建 PointsProductController | 接口控制器 | P0 |

### 3.2 后端 - 兑换记录模块

| ID | 任务 | 描述 | 优先级 |
|----|------|------|--------|
| T-3.2.1 | 创建 ExchangeRecord 实体 | 兑换记录实体类 | P0 |
| T-3.2.2 | 创建 ExchangeRecordMapper | Mapper 接口 | P0 |
| T-3.2.3 | 创建 ExchangeRecordService | 兑换、核销业务逻辑 | P0 |
| T-3.2.4 | 创建 ExchangeRecordController | 接口控制器 | P0 |
| T-3.2.5 | 实现兑换核销功能 | 商家扫码核销兑换 | P0 |

### 3.3 后端 - 积分明细与签到模块

| ID | 任务 | 描述 | 优先级 |
|----|------|------|--------|
| T-3.3.1 | 创建 PointsLog 实体 | 积分明细实体类 | P0 |
| T-3.3.2 | 创建 PointsLogMapper/Service/Controller | 积分明细查询 | P0 |
| T-3.3.3 | 创建 CheckIn 实体 | 签到记录实体类 | P0 |
| T-3.3.4 | 创建 CheckInMapper/Service/Controller | 签到业务逻辑 | P0 |
| T-3.3.5 | 创建 PointsRule 实体 | 积分规则实体类 | P0 |
| T-3.3.6 | 创建 PointsRuleMapper/Service/Controller | 规则查询 | P1 |

### 3.4 前端 - 积分商城页面

| ID | 任务 | 描述 | 优先级 |
|----|------|------|--------|
| T-3.4.1 | 完善 mall 页面 | 展示积分商品分类列表 | P0 |
| T-3.4.2 | 实现积分兑换功能 | 点击兑换调用后端 API | P0 |
| T-3.4.3 | 兑换成功提示 | 兑换成功展示二维码/记录 | P0 |

### 3.5 前端 - 签到与明细

| ID | 任务 | 描述 | 优先级 |
|----|------|------|--------|
| T-3.5.1 | 创建签到功能入口 | 在首页或个人中心添加签到 | P0 |
| T-3.5.2 | 创建积分明细页面 | points-log 页面展示积分变动 | P0 |
| T-3.5.3 | 创建待领取列表页面 | exchanged 页面展示待领取商品 | P0 |

---

## API 接口清单

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 积分商品列表 | GET | /api/points/products | 获取积分商品列表 |
| 积分商品详情 | GET | /api/points/products/:id | 获取商品详情 |
| 积分兑换 | POST | /api/exchange | 积分兑换商品 |
| 我的兑换 | GET | /api/exchange/my | 获取我的兑换记录 |
| 兑换详情 | GET | /api/exchange/:id | 获取兑换详情 |
| 核销兑换 | POST | /api/exchange/verifyByQrCode | 商家核销兑换 |
| 积分明细 | GET | /api/points/logs | 获取积分明细 |
| 今日签到状态 | GET | /api/checkIn/status | 查询今日是否已签到 |
| 执行签到 | POST | /api/checkIn | 执行每日签到 |
| 积分规则 | GET | /api/points/rules | 获取积分规则 |

---

## 验收标准

- [ ] 积分商品列表正常展示，支持分类切换
- [ ] 用户可用积分兑换商品，扣减积分并生成核销码
- [ ] 兑换记录可在待领取列表查看
- [ ] 商家可扫码核销兑换记录
- [ ] 每日签到可获得积分，连续签到有额外奖励
- [ ] 积分明细清晰展示所有积分变动记录
