# Phase 5: 商品中心完善与积分商品管理

## 阶段目标
修复商品中心模块与现有促销活动管理的数据一致性问题，补充缺失的积分商品管理页面，使 B 端商家能够完整管理商品库、促销活动及积分兑换商品。

## 前置状态
- 后端商品中心基础代码（Product 实体/Mapper/Service/Controller）已存在
- 后端 PromotionProductController/Service 和 PointsProductController/Service 已扩展管理接口
- 小程序 `product-manage` 和 `promotion-manage` 页面已存在
- `migration_product.sql` 已在数据库表添加 `product_id` 字段
- `app.json` 和 `profile` 路由/入口已部分更新

## 任务清单

### Task 1: 修复后端实体类字段缺失
**文件:**
- `backend/src/main/java/com/wxshop/member/entity/PromotionProduct.java`
- `backend/src/main/java/com/wxshop/member/entity/PointsProduct.java`

**改动:**
- 在 `PromotionProduct` 中添加 `private Long productId;` 字段
- 在 `PointsProduct` 中添加 `private Long productId;` 字段
- 确保 MyBatis-Plus 能正确映射数据库列 `product_id`

**验证:**
- `cd backend && mvn compile` 成功通过

---

### Task 2: 修复促销活动管理页面编辑回显
**文件:**
- `miniprogram/pages/promotion-manage/promotion-manage.js`

**改动:**
- 在 `editActivity` 方法中，根据 `activity.productId` 从 `products` 列表中查找对应商品，正确设置 `selectedProduct`
- 确保编辑弹窗中"选择商品"区域回显已关联的商品名称

**验证:**
- 打开促销活动管理 → 编辑已有活动 → 商品选择器正确显示已选商品

---

### Task 3: 创建积分商品管理页面
**新增文件:**
- `miniprogram/pages/points-product-manage/points-product-manage.json`
- `miniprogram/pages/points-product-manage/points-product-manage.wxml`
- `miniprogram/pages/points-product-manage/points-product-manage.wxss`
- `miniprogram/pages/points-product-manage/points-product-manage.js`

**功能要求:**
1. 展示积分商品列表（调用 `/api/points/products/all`）
2. 支持添加/编辑积分商品
3. 支持从商品库选择商品（调用 `/api/products/active`）
4. 自动带入商品名称、图片、类型，支持设置所需积分和库存
5. 支持上架/下架操作

**验证:**
- 页面能正常加载列表
- 添加/编辑/上下架操作正常

---

### Task 4: 更新商家管理入口
**文件:**
- `miniprogram/pages/profile/profile.wxml`
- `miniprogram/pages/profile/profile.js`
- `miniprogram/app.json`

**改动:**
- 在 `profile.wxml` 商家管理区域添加"积分商品管理"入口
- 在 `profile.js` 添加 `goToPointsProductManage` 导航方法
- 确认 `app.json` 中已注册 `pages/points-product-manage/points-product-manage`

**验证:**
- 商家用户在"我的"页面能看到"积分商品管理"入口并正常跳转

---

### Task 5: 后端编译验证
**命令:**
```bash
cd backend && mvn compile
```

**验证:**
- 编译无错误、无警告

## 验收标准
- [ ] `PromotionProduct` 和 `PointsProduct` 实体类包含 `productId` 字段
- [ ] 促销活动编辑时正确回显已选商品
- [ ] 积分商品管理页面可正常使用（增删改查、上下架）
- [ ] profile 页面新增积分商品管理入口
- [ ] 后端 `mvn compile` 通过
