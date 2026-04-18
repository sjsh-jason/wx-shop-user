# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

餐饮会员小程序项目指南。

## 项目概述

Java Spring Boot 后端 + 原生微信小程序前端。支持：
- C端：扫码注册、促销抢购预约、积分商城、抽奖、签到
- B端：扫码核销、商品管理、积分规则配置

## 常用命令

### 后端（backend/）
```bash
cd backend && mvn spring-boot:run           # 本地启动（需 MySQL 8.0+）
cd backend && mvn clean package              # 打包
cd backend && mvn test -Dtest=ClassName      # 运行单个测试类
java -jar backend/target/member-1.0.0.jar    # 运行已打包的 jar
```

### 数据库
- 初始化脚本：`backend/src/main/resources/db/schema.sql`
- 抽奖 V2 迁移：`backend/src/main/resources/db/migration_draw_v2.sql`
- 库名：`wx_shop_member`
- 配置：`backend/src/main/resources/application.yml`

### 小程序前端（miniprogram/）
使用微信开发者工具打开 `miniprogram` 目录，无 npm 构建流程。

## 后端架构

### 技术栈
Spring Boot 2.7.18（Java 11）+ MyBatis-Plus 3.5.3 + MySQL 8.0 + JWT 0.11.5 + Hutool 5.8.20 + Lombok

### 代码结构
基础包名：`com.wxshop.member`
- `controller/` — REST API，统一返回 `Result<T>`
- `service/` — 业务逻辑
- `mapper/` — MyBatis-Plus Mapper
- `entity/` — 数据实体
- `dto/` — 请求/响应 DTO
- `common/` — `Result<T>` 统一响应
- `config/` — CORS、JWT、微信、MyBatis-Plus、Web（上传资源映射）配置
- `util/` — `JwtUtil`、`WxUtil`

### 主要数据实体
| 实体 | 说明 |
|------|------|
| `User` | 会员（openid、积分、等级） |
| `MerchantWhitelist` | B端商家手机号白名单 |
| `Product` | 基础商品库（coupon/dish/gift） |
| `PromotionProduct` | 促销抢购商品 |
| `PointsProduct` | 积分商城兑换商品 |
| `PointsRule` | 积分规则配置 |
| `ExchangeRecord` | 积分兑换记录 |
| `Reservation` | 促销预约记录 |
| `CheckIn` | 每日签到记录 |
| `PointsLog` | 积分变动明细 |
| `LuckyDraw` / `Prize` / `DrawChance` / `DrawChanceRule` / `DrawRiskConfig` / `DrawActivityConfig` | 抽奖相关（奖品、机会、规则、风控、活动配置） |

### 认证
- 登录：`POST /api/auth/wechat/login`，返回 JWT token
- JWT 配置：`application.yml` 的 `jwt.*`
- 暂无统一拦截器，各 Controller 需手动校验（从 Header 提取 Bearer token 调用 `JwtUtil.parseToken`）

### MyBatis-Plus 配置
- `map-underscore-to-camel-case: true`
- 逻辑删除：`deleted`（1=删除，0=未删除）
- 自动填充：`create_time`、`update_time`（`MyMetaObjectHandler`）
- 主键策略：`id-type: auto`

### 文件上传
- 上传路径：`application.yml` 的 `upload.path`（默认 `./uploads`）
- 访问 URL 前缀：`/uploads/**`
- 映射配置：`WebConfig.addResourceHandlers`

## 小程序前端

### 页面结构
C端 Tab 页面：
- 首页（index）— 促销展示、扫码入口
- 兑换（mall）— 积分商城
- 待取（exchanged）— 兑换/预约待核销列表
- 我的（profile）— 个人信息、积分、签到、抽奖入口

C端其他页面：登录（login）、抢购（flash）、预约详情（reservation、reservation-detail）、卡券（coupons）、积分明细（points-log）、兑换详情（exchanged-detail）、抽奖（lucky-draw）、抽奖规则（draw-rule）、抽奖记录（draw-records）

B端页面（需白名单权限）：扫码核销（scan）、奖品管理（prize-manage）、客户列表（customer-list）、商品管理（product-manage、points-product-manage）、促销管理（promotion-manage）、抽奖配置（draw-activity-setting、draw-chance-rule、draw-risk-config）

### 网络请求
`app.js` 封装 `request` 方法：
- baseUrl：`http://192.168.184.1:8080`（开发环境）
- 自动携带 `Authorization: Bearer <token>`
- 401 自动清除 token 并跳转登录页

```js
const app = getApp();
app.request({ url: '/api/promotion/list', method: 'GET' }).then(...)
```

## 开发规范
- 函数小驼峰，组件大驼峰，常量全大写下划线分隔
- 注释中文，代码英文
- 优先函数式组件
- 每个 PR 只解决一个问题
- 代码需包含单元测试
