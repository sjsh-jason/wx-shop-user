-- 商品中心模块 - 数据库迁移脚本
-- 执行时间: 2026-04-16

USE wx_shop_member;

-- 商品中心表（基础商品库）
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '商品名称',
    `image` VARCHAR(255) DEFAULT NULL COMMENT '商品图片',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '商品描述',
    `type` VARCHAR(50) NOT NULL COMMENT '商品类型: coupon-代金券, dish-菜品, gift-其他礼品',
    `price` DECIMAL(10,2) DEFAULT NULL COMMENT '商品价格',
    `stock` INT NOT NULL DEFAULT 0 COMMENT '库存',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-下架, 1-上架',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_type` (`type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品中心表';

-- 修改促销商品表，关联商品中心
ALTER TABLE `promotion_product`
ADD COLUMN `product_id` BIGINT DEFAULT NULL COMMENT '关联商品ID' AFTER `id`,
ADD KEY `idx_product_id` (`product_id`);

-- 修改积分商品表，关联商品中心
ALTER TABLE `points_product`
ADD COLUMN `product_id` BIGINT DEFAULT NULL COMMENT '关联商品ID' AFTER `id`,
ADD KEY `idx_product_id` (`product_id`);

-- 初始化测试商品数据
INSERT INTO `product` (`name`, `image`, `description`, `type`, `price`, `stock`, `status`) VALUES
('全场通用10元券', '', '全场通用，无门槛使用', 'coupon', 10.00, 100, 1),
('满100减30券', '', '满100元可用', 'coupon', 30.00, 50, 1),
('招牌冻柠茶', '', '清爽解腻，招牌饮品', 'dish', 15.00, 200, 1),
('脆皮炸鸡翅', '', '外酥里嫩，香味四溢', 'dish', 28.00, 80, 1),
('定制帆布袋', '', '环保材质，限量周边', 'gift', 39.00, 30, 1),
('定制水杯', '', '食品级材质，保温保冷', 'gift', 59.00, 20, 1);
