-- 餐饮会员小程序 - 数据库初始化脚本
-- 创建时间: 2026-04-14

-- 创建数据库
CREATE DATABASE IF NOT EXISTS wx_shop_member DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE wx_shop_member;

-- 会员表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `openid` VARCHAR(64) NOT NULL COMMENT '微信openid',
    `unionid` VARCHAR(64) DEFAULT NULL COMMENT '微信unionid',
    `nickname` VARCHAR(64) DEFAULT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `points` INT NOT NULL DEFAULT 0 COMMENT '积分余额',
    `level` TINYINT NOT NULL DEFAULT 1 COMMENT '会员等级: 1-普通, 2-黄金, 3-铂金, 4-钻石',
    `total_points` INT NOT NULL DEFAULT 0 COMMENT '累计获得积分',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_openid` (`openid`),
    KEY `idx_phone` (`phone`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会员表';

-- 商家白名单表
DROP TABLE IF EXISTS `merchant_whitelist`;
CREATE TABLE `merchant_whitelist` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `phone` VARCHAR(20) NOT NULL COMMENT '商家手机号',
    `name` VARCHAR(64) DEFAULT NULL COMMENT '商家名称/备注',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_phone` (`phone`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商家白名单表';

-- 促销商品表
DROP TABLE IF EXISTS `promotion_product`;
CREATE TABLE `promotion_product` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '商品名称',
    `image` VARCHAR(255) DEFAULT NULL COMMENT '商品图片',
    `original_price` DECIMAL(10,2) NOT NULL COMMENT '原价',
    `promotion_price` DECIMAL(10,2) NOT NULL COMMENT '促销价',
    `stock` INT NOT NULL DEFAULT 0 COMMENT '库存',
    `sold_count` INT NOT NULL DEFAULT 0 COMMENT '已售数量',
    `start_time` DATETIME DEFAULT NULL COMMENT '活动开始时间',
    `end_time` DATETIME DEFAULT NULL COMMENT '活动结束时间',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-下架, 1-上架',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_status` (`status`),
    KEY `idx_start_time` (`start_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='促销商品表';

-- 预约记录表
DROP TABLE IF EXISTS `reservation`;
CREATE TABLE `reservation` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `product_id` BIGINT NOT NULL COMMENT '商品ID',
    `product_name` VARCHAR(100) DEFAULT NULL COMMENT '商品名称快照',
    `product_image` VARCHAR(255) DEFAULT NULL COMMENT '商品图片快照',
    `original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '原价快照',
    `promotion_price` DECIMAL(10,2) DEFAULT NULL COMMENT '促销价快照',
    `qr_code` VARCHAR(255) DEFAULT NULL COMMENT '核销二维码',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-待核销, 1-已核销, 2-已取消',
    `verify_time` DATETIME DEFAULT NULL COMMENT '核销时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='预约记录表';

-- 积分商品表
DROP TABLE IF EXISTS `points_product`;
CREATE TABLE `points_product` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '商品名称',
    `image` VARCHAR(255) DEFAULT NULL COMMENT '商品图片',
    `points` INT NOT NULL COMMENT '所需积分',
    `price` DECIMAL(10,2) DEFAULT NULL COMMENT '商品价值',
    `category` VARCHAR(50) DEFAULT NULL COMMENT '分类: coupon-代金券, dish-菜品, gift-周边',
    `stock` INT NOT NULL DEFAULT 0 COMMENT '库存',
    `exchanged_count` INT NOT NULL DEFAULT 0 COMMENT '已兑换数量',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-下架, 1-上架',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_status` (`status`),
    KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='积分商品表';

-- 兑换记录表
DROP TABLE IF EXISTS `exchange_record`;
CREATE TABLE `exchange_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `product_id` BIGINT NOT NULL COMMENT '商品ID',
    `product_name` VARCHAR(100) DEFAULT NULL COMMENT '商品名称快照',
    `product_image` VARCHAR(255) DEFAULT NULL COMMENT '商品图片快照',
    `points` INT DEFAULT NULL COMMENT '消耗积分快照',
    `qr_code` VARCHAR(255) DEFAULT NULL COMMENT '核销二维码',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-待领取, 1-已领取, 2-已取消',
    `verify_time` DATETIME DEFAULT NULL COMMENT '领取时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='兑换记录表';

-- 积分明细表
DROP TABLE IF EXISTS `points_log`;
CREATE TABLE `points_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `type` TINYINT NOT NULL COMMENT '类型: 1-签到, 2-消费获得, 3-兑换获得, 4-抽奖获得, 5-兑换消耗, 6-抽奖消耗',
    `points` INT NOT NULL COMMENT '积分变化(正为增加,负为减少)',
    `balance` INT NOT NULL COMMENT '变动后余额',
    `related_type` VARCHAR(50) DEFAULT NULL COMMENT '关联类型',
    `related_id` BIGINT DEFAULT NULL COMMENT '关联ID',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='积分明细表';

-- 签到记录表
DROP TABLE IF EXISTS `check_in`;
CREATE TABLE `check_in` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `check_date` DATE NOT NULL COMMENT '签到日期',
    `points` INT NOT NULL COMMENT '获得积分',
    `continuous_days` INT NOT NULL DEFAULT 1 COMMENT '连续签到天数',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_date` (`user_id`, `check_date`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='签到记录表';

-- 抽奖奖品表
DROP TABLE IF EXISTS `prize`;
CREATE TABLE `prize` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(50) NOT NULL COMMENT '奖品名称',
    `icon` VARCHAR(255) DEFAULT NULL COMMENT '奖品图标',
    `type` TINYINT NOT NULL COMMENT '类型: 0-谢谢参与, 1-积分, 2-商品, 3-代金券',
    `points` INT DEFAULT 0 COMMENT '积分数量(type=1时)',
    `product_id` BIGINT DEFAULT NULL COMMENT '关联商品ID(type=2时)',
    `coupon_value` DECIMAL(10,2) DEFAULT NULL COMMENT '代金券面值(type=3时)',
    `probability` DECIMAL(5,4) NOT NULL COMMENT '中奖概率',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='抽奖奖品表';

-- 抽奖记录表
DROP TABLE IF EXISTS `lucky_draw`;
CREATE TABLE `lucky_draw` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `prize_id` BIGINT NOT NULL COMMENT '奖品ID',
    `prize_name` VARCHAR(50) DEFAULT NULL COMMENT '奖品名称快照',
    `prize_type` TINYINT DEFAULT NULL COMMENT '奖品类型快照',
    `points` INT DEFAULT 0 COMMENT '获得积分',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-未发放, 1-已发放',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='抽奖记录表';

-- 积分规则配置表
DROP TABLE IF EXISTS `points_rule`;
CREATE TABLE `points_rule` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `type` VARCHAR(50) NOT NULL COMMENT '规则类型: check_in-签到, consume-消费, exchange-兑换',
    `points` INT NOT NULL COMMENT '积分数量',
    `consume_ratio` DECIMAL(5,2) DEFAULT NULL COMMENT '消费金额比例(消费1元得N积分)',
    `exchange_ratio` DECIMAL(5,2) DEFAULT NULL COMMENT '兑换金额比例(兑换1元得N积分)',
    `continuous_bonus` TEXT DEFAULT NULL COMMENT '连续签到奖励JSON',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='积分规则配置表';

-- 初始化积分规则
INSERT INTO `points_rule` (`type`, `points`, `consume_ratio`, `exchange_ratio`) VALUES
('check_in', 10, NULL, NULL),
('consume', 0, 10.00, NULL),
('exchange', 0, NULL, 5.00);

-- 初始化抽奖奖品
INSERT INTO `prize` (`name`, `icon`, `type`, `points`, `probability`, `sort_order`) VALUES
('免费饮品', 'cup-hot', 2, 0, 0.05, 0),
('200积分', 'star', 1, 200, 0.10, 1),
('¥10代金券', 'ticket', 3, 0, 0.15, 2),
('50积分', 'star', 1, 50, 0.20, 3),
('招牌菜1份', 'hamburger', 2, 0, 0.03, 4),
('¥50大额券', 'ticket', 3, 0, 0.02, 5),
('500积分', 'star', 1, 500, 0.05, 6),
('谢谢参与', 'emoji-funny', 0, 0, 0.40, 7);

-- 初始化测试商家手机号（可替换为实际商家手机号）
INSERT INTO `merchant_whitelist` (`phone`, `name`) VALUES
('13800138000', '测试商家');
