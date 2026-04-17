# 项目状态

## 当前状态
- **当前阶段**: Phase 7 进行中
- **当前里程碑**: 抽奖功能优化与增强
- **最后更新**: 2026-04-18

## 已完成
- ✅ 需求调研与确认
- ✅ 需求文档编写（doc/requiremenDoc.md）
- ✅ 项目规划文件创建（.planning/）
- ✅ Phase 1 项目初始化与基础框架
- ✅ Phase 1 后端 Spring Boot 项目结构、用户模块、登录认证
- ✅ Phase 1 小程序前端项目初始化、登录页、首页、个人中心、导航
- ✅ Phase 2 促销抢购功能完整交付
  - 促销商品模块（实体、Mapper、Service、Controller）
  - 预约记录模块（实体、Mapper、Service、Controller）
  - 小程序促销商品列表页（pages/flash）
  - 抢购预约功能与我的预约列表（pages/reservation）
  - 预约详情与二维码展示（pages/reservation-detail）
  - 商家白名单权限控制（merchant_whitelist）
  - 商家端扫码核销页面（pages/scan）
- ✅ Phase 3 积分商城功能完整交付
  - 积分商品模块（实体、Mapper、Service、Controller）
  - 兑换记录模块（实体、Mapper、Service、Controller）
  - 积分明细模块（实体、Mapper、Service、Controller）
  - 签到记录模块（实体、Mapper、Service、Controller）
  - 积分规则模块（实体、Mapper、Service、Controller）
  - 小程序积分商城页面（pages/mall）- 商品展示 + 兑换功能
  - 小程序每日签到功能（pages/check-in）
  - 小程序积分明细页面（pages/points-log）
  - 小程序待领取列表页面（pages/exchanged）
  - 小程序兑换详情页面（pages/exchanged-detail）- 二维码展示
  - 商家端领取核销支持（复用 scan 页面）
  - 个人中心页面更新（添加签到、兑换记录、积分明细入口）
- ✅ Phase 4 幸运抽奖 & 完善完整交付
  - 后端抽奖模块（Prize、LuckyDraw 实体、Mapper、Service、Controller）
  - 小程序幸运抽奖页面（pages/lucky-draw）- 九宫格抽奖动画
  - 首页添加幸运抽奖入口
  - 个人中心添加幸运抽奖入口
  - 商家端奖品管理页面（pages/prize-manage）
  - 商家端客户列表页面（pages/customer-list）
  - app.json 更新添加所有新页面路由
- ✅ Phase 5 商品中心完善与积分商品管理完整交付
  - 修复后端实体类 `PromotionProduct`、`PointsProduct` 的 `productId` 字段缺失
  - 修复促销活动管理页面编辑时的商品回显 Bug
  - 创建积分商品管理页面（pages/points-product-manage）
  - 更新商家管理入口（profile 页面添加积分商品管理入口）
  - 后端编译验证通过
- ✅ Phase 6 Bug 修复与功能完善完整交付
  - 验证积分商城页面功能正常
  - 验证首页"查看全部"跳转到限时秒杀页面
  - 验证"立即抢购"按钮功能完整
  - 修改抽奖页面，支持奖品类型判断和领取跳转
  - 确认首页快捷入口已为"限时秒杀"
  - 确认"每日签到"入口在个人中心保留
  - 确认拼团功能已记录到后续待实现需求
  - 后端编译验证通过
- ✅ Phase 7 抽奖功能优化与增强（部分完成）
  - 创建 Phase 7 规划文档（.planning/7/PLAN.md）
  - 后端数据库迁移脚本（migration_draw_v2.sql）
  - 后端新增实体类：DrawActivityConfig、DrawChance、DrawChanceRule、DrawChanceLog、DrawRiskConfig、DrawPrizeLimit
  - 后端新增 Mapper：DrawActivityConfigMapper、DrawChanceMapper、DrawChanceRuleMapper、DrawChanceLogMapper、DrawRiskConfigMapper、DrawPrizeLimitMapper
  - 后端新增服务：DrawChanceService、DrawActivityConfigService、DrawRiskConfigService
  - 后端重构优化：LuckyDrawService、PrizeService
  - 后端新增控制器：DrawActivityConfigController、DrawChanceController、DrawChanceRuleController、DrawRiskConfigController
  - 后端更新控制器：LuckyDrawController、PrizeController
  - 前端优化抽奖页面：lucky-draw（添加活动标题、倒计时、剩余次数、活动规则入口、我的奖品入口）
  - 前端新增活动规则页：draw-rule
  - 前端新增中奖记录页：draw-records
  - 前端新增抽奖活动设置页：draw-activity-setting
  - 前端新增抽奖机会规则页：draw-chance-rule
  - 前端新增风控配置页：draw-risk-config
  - 前端更新 app.json 注册新页面
  - 前端更新个人中心 profile 页面添加抽奖管理入口

## 进行中
- Phase 7 抽奖功能优化与增强

## 待开始
- Phase 7 剩余：后端编译验证、端到端功能测试

## Phase 1 已交付文件

### 后端
- `backend/pom.xml` - Maven 配置
- `backend/src/main/resources/application.yml` - 应用配置
- `backend/src/main/resources/db/schema.sql` - 数据库初始化脚本
- `backend/src/main/java/com/wxshop/member/MemberApplication.java` - 启动类
- `backend/src/main/java/com/wxshop/member/entity/User.java` - 用户实体
- `backend/src/main/java/com/wxshop/member/mapper/UserMapper.java` - 用户Mapper
- `backend/src/main/java/com/wxshop/member/service/UserService.java` - 用户服务
- `backend/src/main/java/com/wxshop/member/controller/AuthController.java` - 认证接口
- `backend/src/main/java/com/wxshop/member/controller/UserController.java` - 用户接口
- `backend/src/main/java/com/wxshop/member/config/*` - 配置类
- `backend/src/main/java/com/wxshop/member/util/*` - 工具类
- `backend/src/main/java/com/wxshop/member/common/*` - 公共类
- `backend/src/main/java/com/wxshop/member/dto/*` - 数据传输对象

### 前端
- `miniprogram/app.json` - 小程序配置
- `miniprogram/app.js` - 小程序入口
- `miniprogram/app.wxss` - 全局样式
- `miniprogram/sitemap.json` - 站点地图
- `miniprogram/pages/login/*` - 登录页面
- `miniprogram/pages/index/*` - 首页
- `miniprogram/pages/profile/*` - 个人中心
- `miniprogram/pages/mall/*` - 积分商城
- `miniprogram/pages/coupons/*` - 卡券中心

## Phase 3 已交付文件

### 后端新增
- `backend/src/main/java/com/wxshop/member/entity/PointsProduct.java` - 积分商品实体
- `backend/src/main/java/com/wxshop/member/entity/ExchangeRecord.java` - 兑换记录实体
- `backend/src/main/java/com/wxshop/member/entity/PointsLog.java` - 积分明细实体
- `backend/src/main/java/com/wxshop/member/entity/CheckIn.java` - 签到记录实体
- `backend/src/main/java/com/wxshop/member/entity/PointsRule.java` - 积分规则实体
- `backend/src/main/java/com/wxshop/member/mapper/PointsProductMapper.java`
- `backend/src/main/java/com/wxshop/member/mapper/ExchangeRecordMapper.java`
- `backend/src/main/java/com/wxshop/member/mapper/PointsLogMapper.java`
- `backend/src/main/java/com/wxshop/member/mapper/CheckInMapper.java`
- `backend/src/main/java/com/wxshop/member/mapper/PointsRuleMapper.java`
- `backend/src/main/java/com/wxshop/member/service/PointsProductService.java`
- `backend/src/main/java/com/wxshop/member/service/ExchangeRecordService.java`
- `backend/src/main/java/com/wxshop/member/service/PointsLogService.java`
- `backend/src/main/java/com/wxshop/member/service/CheckInService.java`
- `backend/src/main/java/com/wxshop/member/service/PointsRuleService.java`
- `backend/src/main/java/com/wxshop/member/controller/PointsProductController.java`
- `backend/src/main/java/com/wxshop/member/controller/ExchangeRecordController.java`
- `backend/src/main/java/com/wxshop/member/controller/PointsLogController.java`
- `backend/src/main/java/com/wxshop/member/controller/CheckInController.java`
- `backend/src/main/java/com/wxshop/member/controller/PointsRuleController.java`

### 前端新增
- `miniprogram/pages/mall/*` - 积分商城页面（已完善）
- `miniprogram/pages/check-in/*` - 每日签到页面
- `miniprogram/pages/points-log/*` - 积分明细页面
- `miniprogram/pages/exchanged/*` - 待领取列表页面
- `miniprogram/pages/exchanged-detail/*` - 兑换详情页面
- `miniprogram/pages/profile/*` - 个人中心页面（已更新）
- `miniprogram/app.json` - 小程序配置（已更新，添加新页面路由）

## Phase 4 已交付文件

### 后端新增
- `backend/src/main/java/com/wxshop/member/entity/Prize.java` - 抽奖奖品实体
- `backend/src/main/java/com/wxshop/member/entity/LuckyDraw.java` - 抽奖记录实体
- `backend/src/main/java/com/wxshop/member/mapper/PrizeMapper.java`
- `backend/src/main/java/com/wxshop/member/mapper/LuckyDrawMapper.java`
- `backend/src/main/java/com/wxshop/member/service/PrizeService.java`
- `backend/src/main/java/com/wxshop/member/service/LuckyDrawService.java` - 含概率抽奖算法
- `backend/src/main/java/com/wxshop/member/controller/PrizeController.java`
- `backend/src/main/java/com/wxshop/member/controller/LuckyDrawController.java`

### 前端新增
- `miniprogram/pages/lucky-draw/*` - 幸运抽奖页面（九宫格动画）
- `miniprogram/pages/prize-manage/*` - 商家端奖品管理页面
- `miniprogram/pages/customer-list/*` - 商家端客户列表页面
- `miniprogram/pages/index/*` - 首页（已更新，添加抽奖入口）
- `miniprogram/pages/profile/*` - 个人中心（已更新，添加抽奖入口）
- `miniprogram/app.json` - 小程序配置（已更新，添加所有新页面路由）

## Phase 5 已交付文件

### 后端修改
- `backend/src/main/java/com/wxshop/member/entity/PromotionProduct.java` - 新增 `productId` 字段
- `backend/src/main/java/com/wxshop/member/entity/PointsProduct.java` - 新增 `productId` 字段
- `backend/src/main/java/com/wxshop/member/controller/ProductController.java` - 商品中心 Controller
- `backend/src/main/java/com/wxshop/member/entity/Product.java` - 商品中心实体
- `backend/src/main/java/com/wxshop/member/mapper/ProductMapper.java` - 商品中心 Mapper
- `backend/src/main/java/com/wxshop/member/service/ProductService.java` - 商品中心 Service
- `backend/src/main/resources/db/migration_product.sql` - 商品表迁移脚本

### 前端新增/修改
- `miniprogram/pages/product-manage/*` - 商品管理页面
- `miniprogram/pages/promotion-manage/*` - 促销活动管理页面（修复编辑回显）
- `miniprogram/pages/points-product-manage/*` - 积分商品管理页面（新增）
- `miniprogram/pages/profile/*` - 个人中心（新增积分商品管理入口）
- `miniprogram/app.json` - 注册新页面路由

## Phase 7 已交付文件

### 后端新增
- `backend/src/main/resources/db/migration_draw_v2.sql` - 抽奖功能优化数据库迁移脚本
- `backend/src/main/java/com/wxshop/member/entity/DrawActivityConfig.java` - 抽奖活动配置实体
- `backend/src/main/java/com/wxshop/member/entity/DrawChance.java` - 抽奖机会实体
- `backend/src/main/java/com/wxshop/member/entity/DrawChanceRule.java` - 抽奖机会规则实体
- `backend/src/main/java/com/wxshop/member/entity/DrawChanceLog.java` - 抽奖机会明细实体
- `backend/src/main/java/com/wxshop/member/entity/DrawRiskConfig.java` - 风控配置实体
- `backend/src/main/java/com/wxshop/member/entity/DrawPrizeLimit.java` - 用户中奖限制实体
- `backend/src/main/java/com/wxshop/member/mapper/DrawActivityConfigMapper.java`
- `backend/src/main/java/com/wxshop/member/mapper/DrawChanceMapper.java`
- `backend/src/main/java/com/wxshop/member/mapper/DrawChanceRuleMapper.java`
- `backend/src/main/java/com/wxshop/member/mapper/DrawChanceLogMapper.java`
- `backend/src/main/java/com/wxshop/member/mapper/DrawRiskConfigMapper.java`
- `backend/src/main/java/com/wxshop/member/mapper/DrawPrizeLimitMapper.java`
- `backend/src/main/java/com/wxshop/member/service/DrawChanceService.java` - 抽奖机会服务
- `backend/src/main/java/com/wxshop/member/service/DrawActivityConfigService.java` - 活动配置服务
- `backend/src/main/java/com/wxshop/member/service/DrawRiskConfigService.java` - 风控配置服务
- `backend/src/main/java/com/wxshop/member/controller/DrawActivityConfigController.java` - 活动配置控制器
- `backend/src/main/java/com/wxshop/member/controller/DrawChanceController.java` - 抽奖机会控制器
- `backend/src/main/java/com/wxshop/member/controller/DrawChanceRuleController.java` - 机会规则控制器
- `backend/src/main/java/com/wxshop/member/controller/DrawRiskConfigController.java` - 风控配置控制器

### 后端修改
- `backend/src/main/java/com/wxshop/member/entity/Prize.java` - 新增 validDays、description 字段
- `backend/src/main/java/com/wxshop/member/entity/LuckyDraw.java` - 新增 expireTime、verifyStatus、verifyTime、qrCode 字段
- `backend/src/main/java/com/wxshop/member/service/LuckyDrawService.java` - 重构优化，支持多次抽奖、风控逻辑
- `backend/src/main/java/com/wxshop/member/service/PrizeService.java` - 新增 updatePrize 重载方法
- `backend/src/main/java/com/wxshop/member/controller/LuckyDrawController.java` - 新增分页查询、核销接口
- `backend/src/main/java/com/wxshop/member/controller/PrizeController.java` - 新增批量更新、全部查询接口

### 前端修改
- `miniprogram/pages/lucky-draw/lucky-draw.wxml` - 优化抽奖页面UI
- `miniprogram/pages/lucky-draw/lucky-draw.js` - 优化抽奖逻辑，添加活动配置、倒计时、剩余次数

### 前端新增
- `miniprogram/pages/draw-rule/*` - 活动规则页面
- `miniprogram/pages/draw-records/*` - 中奖记录页面
- `miniprogram/pages/draw-activity-setting/*` - 抽奖活动设置页面
- `miniprogram/pages/draw-chance-rule/*` - 抽奖机会规则页面
- `miniprogram/pages/draw-risk-config/*` - 风控配置页面

### 前端配置修改
- `miniprogram/app.json` - 注册新页面路由
- `miniprogram/pages/profile/profile.wxml` - 添加抽奖管理入口
- `miniprogram/pages/profile/profile.js` - 添加跳转方法

## 风险与问题
- 微信 appid 和 secret 需要在 application.yml 中配置
- 需要先创建数据库并执行 schema.sql 初始化
- Phase 7 需执行 migration_draw_v2.sql 数据库迁移脚本
- Phase 7 涉及较多后端和前端改动，需全面测试

## 项目完成总结

**Phase 1 - 项目初始化与基础框架** ✅
- 搭建小程序基础架构，实现登录注册

**Phase 2 - 促销抢购功能** ✅
- 实现促销商品管理、抢购预约、扫码核销

**Phase 3 - 积分商城功能** ✅
- 实现积分规则、商品兑换、待领取管理、每日签到、积分明细

**Phase 4 - 幸运抽奖 & 完善** ✅
- 实现九宫格幸运抽奖功能，完善商家端管理，整体优化体验

**Phase 5 - 商品中心完善与积分商品管理** ✅
- 修复商品中心数据一致性问题，完善 B 端商品管理能力

**Phase 6 - Bug 修复与功能完善** ✅
- 修复现有功能的 Bug，完善用户体验

**Phase 7 - 抽奖功能优化与增强** 🔄
- 根据新需求优化抽奖功能，添加抽奖机会管理、活动设置、风控配置
