# CLAUDE.md

餐饮会员小程序项目指南。

## 项目概述
Java Spring Boot 后端 + 原生微信小程序前端。支持：
- C端：扫码注册、促销抢购预约、积分商城、抽奖、签到
- B端：扫码核销、商品管理、积分规则配置

## 常用命令

### 后端（backend/）
```bash
cd backend && mvn spring-boot:run      # 本地启动（需 MySQL 8.0+）
cd backend && mvn clean package         # 打包
java -jar backend/target/member-1.0.0.jar  # 运行
```

### 数据库
脚本：`backend/src/main/resources/db/schema.sql`
库名：`wx_shop_member`
配置：`backend/src/main/resources/application.yml`

### 小程序前端（miniprogram/）
使用微信开发者工具打开 `miniprogram` 目录，无 npm 构建流程。

## 后端架构

### 技术栈
Spring Boot 2.7.18（Java 11）+ MyBatis-Plus 3.5.3 + MySQL 8.0 + JWT 0.11.5 + Hutool 5.8.20 + Lombok

### 代码结构
- `controller/` — REST API，统一返回 `Result<T>`
- `service/` — 业务逻辑
- `mapper/` — MyBatis-Plus Mapper
- `entity/` — 数据实体
- `dto/` — 请求/响应 DTO
- `common/` — `Result<T>` 统一响应
- `config/` — JWT、微信、MyBatis-Plus 配置
- `util/` — `JwtUtil`、`WxUtil`

### 认证
- 登录：`POST /api/auth/wechat/login`，返回 JWT token
- JWT 配置：`application.yml` 的 `jwt.*`
- 暂无统一拦截器，需手动校验

### MyBatis-Plus 配置
- `map-underscore-to-camel-case: true`
- 逻辑删除：`deleted`（1=删除，0=未删除）
- 自动填充：`create_time`、`update_time`

## 小程序前端

### 页面结构
Tab 栏：首页（index）、兑换（mall）、卡券（coupons）、我的（profile）
B端页面：扫码核销（scan）、奖品管理（prize-manage）、客户列表（customer-list）

### 网络请求
`app.js` 封装 `request` 方法：
- baseUrl：`http://localhost:8080`
- 自动携带 `Authorization: Bearer <token>`
- 401 自动清除 token 并跳转登录页

```js
const app = getApp();
app.request({ url: '/api/promotion/list', method: 'GET' }).then(...)
```

## 开发规范
- 函数小驼峰，组件大驼峰，常量全大写下划线
- 注释中文，代码英文
- 优先函数式组件
- 每个 PR 只解决一个问题
- 代码需包含单元测试
