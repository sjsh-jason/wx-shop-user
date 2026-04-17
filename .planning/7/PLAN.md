---
name: Phase 7 - 抽奖功能优化与增强
goal: 根据新需求优化抽奖功能，添加抽奖机会管理、活动设置、风控配置
phase: 7
category: feature
---

# Phase 7: 抽奖功能优化与增强

## 目标
根据 `doc/抽奖需求优化.md` 需求文档，对现有抽奖功能进行全面优化和增强。

## 需求概述

### C端小程序功能
1. **抽奖首页（核心页）**
   - 顶部：活动标题 + 活动剩余时间
   - 中间：8格转盘（店铺LOGO）
   - 底部：剩余抽奖次数 + 开始抽奖按钮 + 活动规则入口 + 我的中奖记录入口

2. **活动规则详情页**
   - 展示富文本规则
   - 适配小程序屏幕，支持滚动

3. **中奖记录页**
   - 展示：中奖时间、奖品名称、奖品状态、使用截止时间

### B端商家功能
1. **奖品管理**
   - 8个奖品可自定义：奖项名称、中奖概率、奖品类型、使用期限
   - 默认奖品配置

2. **抽奖活动设置**
   - 消费得机会：满X元送X次，每日上限X次
   - 每日签到：赠送次数X次（默认1次）
   - 新用户专享：首次注册赠送X次（默认1次）

3. **抽奖风控配置**
   - 123等奖单人限中1次（布尔值，默认开启）
   - 异常用户拦截（布尔值，默认开启）
   - 恶意刷奖处罚（下拉选择：取消资格/限制参与/封禁账号）

## 任务清单

### 任务 1: 数据库设计与迁移
- [ ] 设计抽奖机会表 `draw_chance`（用户ID、剩余次数、获得来源、更新时间）
- [ ] 设计抽奖活动配置表 `draw_activity_config`（活动标题、开始时间、结束时间、规则富文本）
- [ ] 设计抽奖机会规则表 `draw_chance_rule`（机会类型、触发条件、配置项、状态）
- [ ] 设计风控配置表 `draw_risk_config`（配置项、类型、默认值、说明）
- [ ] 设计用户中奖限制表 `draw_prize_limit`（用户ID、奖品ID、限中次数）
- [ ] 更新 `prize` 表，添加使用期限字段
- [ ] 更新 `lucky_draw` 表，添加使用截止时间、核销状态
- [ ] 编写数据库迁移脚本

### 任务 2: 后端实体与Mapper
- [ ] 创建 `DrawChance` 实体类
- [ ] 创建 `DrawActivityConfig` 实体类
- [ ] 创建 `DrawChanceRule` 实体类
- [ ] 创建 `DrawRiskConfig` 实体类
- [ ] 创建 `DrawPrizeLimit` 实体类
- [ ] 更新 `Prize` 实体，添加 `validDays`（使用期限天数）
- [ ] 更新 `LuckyDraw` 实体，添加 `expireTime`、`verifyStatus`、`verifyTime`
- [ ] 创建对应的 Mapper 接口

### 任务 3: 后端抽奖机会服务
- [ ] 创建 `DrawChanceService`
- [ ] 实现查询用户剩余抽奖次数
- [ ] 实现消费订单完成后赠送抽奖机会
- [ ] 实现签到后赠送抽奖机会
- [ ] 实现新用户注册赠送抽奖机会
- [ ] 实现抽奖时消耗次数
- [ ] 实现每日上限控制

### 任务 4: 后端抽奖逻辑优化
- [ ] 重构 `LuckyDrawService`
- [ ] 实现抽奖前校验（剩余次数、用户身份）
- [ ] 实现123等奖单人限中逻辑
- [ ] 实现异常用户拦截（高频切换账号、机器操作）
- [ ] 优化概率抽奖算法
- [ ] 实现转盘8格奖项配置读取
- [ ] 实现中奖后自动发放奖品（积分/卡券）
- [ ] 实现中奖记录状态管理

### 任务 5: 后端管理服务
- [ ] 创建 `DrawActivityConfigService` - 活动配置管理
- [ ] 创建 `DrawChanceRuleService` - 机会规则管理
- [ ] 创建 `DrawRiskConfigService` - 风控配置管理
- [ ] 扩展 `PrizeService` - 支持8个奖品配置管理
- [ ] 实现默认奖品初始化

### 任务 6: 后端API控制器
- [ ] 创建 `DrawActivityConfigController`
  - GET `/api/draw/activity/config` - 获取活动配置
  - PUT `/api/draw/activity/config` - 更新活动配置
- [ ] 创建 `DrawChanceController`
  - GET `/api/draw/chance/status` - 获取用户抽奖机会状态
- [ ] 创建 `DrawChanceRuleController`
  - GET `/api/draw/chance-rules` - 获取规则列表
  - PUT `/api/draw/chance-rules` - 更新规则
- [ ] 创建 `DrawRiskConfigController`
  - GET `/api/draw/risk-config` - 获取风控配置
  - PUT `/api/draw/risk-config` - 更新风控配置
- [ ] 扩展 `LuckyDrawController`
  - GET `/api/lucky-draw/records` - 分页获取中奖记录（支持状态筛选）
- [ ] 扩展 `PrizeController`
  - GET `/api/prizes/all` - 获取所有奖品（包括禁用）
  - PUT `/api/prizes/batch` - 批量更新奖品

### 任务 7: 前端抽奖首页优化
- [ ] 修改 `pages/lucky-draw/lucky-draw.wxml`
  - 添加活动标题和剩余倒计时
  - 优化8格转盘UI，中心显示店铺LOGO
  - 添加剩余抽奖次数展示
  - 添加"活动规则"入口按钮
  - 添加"我的中奖记录"入口按钮
- [ ] 修改 `pages/lucky-draw/lucky-draw.js`
  - 加载活动配置和剩余时间
  - 加载用户剩余抽奖次数
  - 优化抽奖动画（2-3秒，不可手动停止）
  - 次数为0时弹窗提示并提供跳转入口
  - 中奖后弹窗展示结果和使用说明

### 任务 8: 前端活动规则页
- [ ] 创建 `pages/draw-rule/draw-rule.wxml`
  - 富文本规则展示
  - 支持上下滚动
  - 字体清晰（最小14px）
- [ ] 创建 `pages/draw-rule/draw-rule.wxss`
- [ ] 创建 `pages/draw-rule/draw-rule.js`
  - 加载规则富文本
- [ ] 创建 `pages/draw-rule/draw-rule.json`
- [ ] 在 `app.json` 中注册新页面

### 任务 9: 前端中奖记录页
- [ ] 创建 `pages/draw-records/draw-records.wxml`
  - 列表展示中奖记录
  - 展示：中奖时间、奖品名称、奖品状态、使用截止时间
- [ ] 创建 `pages/draw-records/draw-records.wxss`
- [ ] 创建 `pages/draw-records/draw-records.js`
  - 加载中奖记录列表
  - 下拉刷新，上拉加载更多
- [ ] 创建 `pages/draw-records/draw-records.json`
- [ ] 在 `app.json` 中注册新页面

### 任务 10: 前端商家奖品管理优化
- [ ] 修改 `pages/prize-manage/prize-manage.wxml`
  - 支持8个奖品的编辑
  - 每个奖品可编辑：名称、概率、类型、使用期限
  - 添加"重置为默认"按钮
- [ ] 修改 `pages/prize-manage/prize-manage.js`
  - 批量更新奖品
  - 重置默认奖品配置

### 任务 11: 前端抽奖活动设置页
- [ ] 创建 `pages/draw-activity-setting/draw-activity-setting.wxml`
  - 活动标题编辑
  - 活动时间设置
  - 规则富文本编辑
- [ ] 创建 `pages/draw-activity-setting/draw-activity-setting.wxss`
- [ ] 创建 `pages/draw-activity-setting/draw-activity-setting.js`
  - 加载/保存活动配置
- [ ] 创建 `pages/draw-activity-setting/draw-activity-setting.json`
- [ ] 在 `app.json` 中注册新页面
- [ ] 在商家管理入口添加此页面

### 任务 12: 前端抽奖机会规则设置页
- [ ] 创建 `pages/draw-chance-rule/draw-chance-rule.wxml`
  - 消费得机会：满X元送X次，每日上限X次
  - 每日签到：赠送次数X次
  - 新用户专享：赠送次数X次
- [ ] 创建 `pages/draw-chance-rule/draw-chance-rule.wxss`
- [ ] 创建 `pages/draw-chance-rule/draw-chance-rule.js`
  - 加载/保存规则配置
- [ ] 创建 `pages/draw-chance-rule/draw-chance-rule.json`
- [ ] 在 `app.json` 中注册新页面
- [ ] 在商家管理入口添加此页面

### 任务 13: 前端风控配置页
- [ ] 创建 `pages/draw-risk-config/draw-risk-config.wxml`
  - 123等奖单人限中1次：开关
  - 异常用户拦截：开关
  - 恶意刷奖处罚：下拉选择（取消资格/限制参与/封禁账号）
- [ ] 创建 `pages/draw-risk-config/draw-risk-config.wxss`
- [ ] 创建 `pages/draw-risk-config/draw-risk-config.js`
  - 加载/保存风控配置
- [ ] 创建 `pages/draw-risk-config/draw-risk-config.json`
- [ ] 在 `app.json` 中注册新页面
- [ ] 在商家管理入口添加此页面

### 任务 14: 集成与测试
- [ ] 更新个人中心商家管理入口，添加新页面链接
- [ ] 后端编译验证通过
- [ ] 端到端功能测试
- [ ] 边界条件测试

## 验收标准
- [ ] 数据库表结构设计合理，迁移脚本完整
- [ ] 后端所有服务和控制器正常编译
- [ ] C端抽奖首页展示活动标题、剩余时间、剩余次数
- [ ] 8格转盘正常旋转，中奖结果正确
- [ ] 活动规则页正常展示富文本
- [ ] 中奖记录页展示完整信息
- [ ] 商家端可以管理8个奖品配置
- [ ] 商家端可以设置抽奖活动、机会规则、风控配置
- [ ] 抽奖机会按规则正确发放和消耗
- [ ] 风控逻辑正常生效
- [ ] 奖品自动发放（积分/卡券）

## 风险与注意事项
- 需要兼容现有抽奖数据
- 注意事务处理，确保抽奖和积分/卡券发放的一致性
- 概率算法需要公平且可配置
- 风控拦截需要谨慎，避免误判正常用户
