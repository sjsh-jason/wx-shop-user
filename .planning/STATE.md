# 项目状态

## 当前状态
- **当前阶段**: Phase 6 已完成
- **当前里程碑**: 商品中心完善与 Bug 修复迭代
- **最后更新**: 2026-04-16

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

## 进行中
- 无

## 待开始
- 无（所有规划阶段已完成）

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

## 风险与问题
- 微信 appid 和 secret 需要在 application.yml 中配置
- 需要先创建数据库并执行 schema.sql 初始化
- Phase 6 涉及较多前端页面修复，需逐项验证交互流程

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
