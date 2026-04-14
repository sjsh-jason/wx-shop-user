# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

餐饮会员小程序，包含 Java Spring Boot 后端和原生微信小程序前端。支持 C 端会员（扫码注册、促销抢购预约、积分商城、抽奖、签到）和 B 端商家（扫码核销、商品管理、积分规则配置）。

## 常用命令

### 后端（backend/）

```bash
# 本地启动（需 MySQL 8.0+）
cd backend && mvn spring-boot:run

# 打包
cd backend && mvn clean package

# 运行打包后的 JAR
java -jar backend/target/member-1.0.0.jar
```

### 数据库初始化

数据库脚本位于 `backend/src/main/resources/db/schema.sql`，默认数据库名为 `wx_shop_member`，连接配置在 `backend/src/main/resources/application.yml` 中。

### 小程序前端（miniprogram/）

使用微信开发者工具打开 `miniprogram` 目录进行编译预览，无 npm 构建流程。

## 后端架构

### 技术栈

- Spring Boot 2.7.18（Java 11）
- MyBatis-Plus 3.5.3
- MySQL 8.0
- JWT 0.11.5
- Hutool 5.8.20、Lombok

### 代码结构

采用典型的 Controller-Service-Mapper-Entity 分层：

- `controller/` — REST API 入口，统一返回 `Result<T>`
- `service/` — 业务逻辑
- `mapper/` — MyBatis-Plus Mapper 接口
- `entity/` — 数据实体，与数据库表一一对应
- `dto/` — 请求/响应 DTO
- `common/` — `Result<T>` 统一响应包装
- `config/` — 配置类（JWT、微信、MyBatis-Plus 自动填充）
- `util/` — `JwtUtil`、`WxUtil`

### 统一响应格式

所有 Controller 返回 `Result<T>`（`backend/src/main/java/com/wxshop/member/common/Result.java`），成功时 `code=200`，失败时 `code=500`。

### 认证逻辑

- 登录接口：`POST /api/auth/wechat/login`，通过 wx.login 的 code 换取 openid，返回 JWT token 和用户信息。
- JWT 配置在 `application.yml` 的 `jwt.*` 中，由 `JwtConfig` 读取，`JwtUtil` 负责生成/解析 token。
- 目前 Controller 层未配置统一的 JWT 拦截器，若需鉴权需在各接口中手动调用校验。

### MyBatis-Plus 全局配置

- `map-underscore-to-camel-case: true`
- 逻辑删除字段：`deleted`（删除值为 1，未删除为 0）
- 自动填充：`create_time`、`update_time` 由 `MyMetaObjectHandler` 自动维护

## 小程序前端架构

### 页面结构

底部 Tab 栏包含 4 个入口：首页（index）、兑换（mall）、卡券（coupons）、我的（profile）。

B 端商家页面通过手机号白名单控制权限，相关页面包括：扫码核销（scan）、奖品管理（prize-manage）、客户列表（customer-list）。

### 网络请求

所有后端请求通过 `app.js` 中 `App({ request: ... })` 封装的 Promise 方法发起：

- 基础 URL 配置在 `globalData.baseUrl`（默认 `http://localhost:8080`）
- 自动在 Header 中携带 `Authorization: Bearer <token>`
- 业务错误通过 `wx.showToast` 提示；401 时自动清除 token 并跳转登录页

调用方式示例：

```js
const app = getApp();
app.request({ url: '/api/promotion/list', method: 'GET' }).then(...)
```

## 开发规范

- 函数使用小驼峰命名，组件使用大驼峰命名，常量使用全大写下划线分隔
- 代码注释使用中文
- 优先使用函数式组件
- 每个 PR 只解决一个问题
- 代码需包含单元测试（当前 `backend/src/test` 尚为空，新功能需补充）
