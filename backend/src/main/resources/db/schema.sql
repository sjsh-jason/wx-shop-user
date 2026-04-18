-- 餐饮会员小程序 - 数据库初始化脚本
-- 创建时间: 2026-04-14
-- 合并: 抽奖 V2 迁移 (2026-04-18)

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
    `product_id` BIGINT DEFAULT NULL COMMENT '关联商品ID',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-下架, 1-上架',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_status` (`status`),
    KEY `idx_start_time` (`start_time`),
    KEY `idx_product_id` (`product_id`)
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
    `product_id` BIGINT DEFAULT NULL COMMENT '关联商品ID',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-下架, 1-上架',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_status` (`status`),
    KEY `idx_category` (`category`),
    KEY `idx_product_id` (`product_id`)
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
    `valid_days` INT DEFAULT NULL COMMENT '使用期限天数',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '奖品使用说明',
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
    `expire_time` DATETIME DEFAULT NULL COMMENT '使用截止时间',
    `verify_status` TINYINT DEFAULT 0 COMMENT '核销状态: 0-未使用, 1-已使用, 2-已过期',
    `verify_time` DATETIME DEFAULT NULL COMMENT '核销时间',
    `qr_code` VARCHAR(255) DEFAULT NULL COMMENT '核销二维码',
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

-- 抽奖活动配置表
DROP TABLE IF EXISTS `draw_activity_config`;
CREATE TABLE `draw_activity_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `title` VARCHAR(100) NOT NULL DEFAULT '幸运大抽奖' COMMENT '活动标题',
    `start_time` DATETIME DEFAULT NULL COMMENT '活动开始时间',
    `end_time` DATETIME DEFAULT NULL COMMENT '活动结束时间',
    `rule_content` TEXT COMMENT '活动规则富文本',
    `shop_logo` VARCHAR(255) DEFAULT NULL COMMENT '店铺LOGO',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='抽奖活动配置表';

-- 抽奖机会表
DROP TABLE IF EXISTS `draw_chance`;
CREATE TABLE `draw_chance` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `remaining_count` INT NOT NULL DEFAULT 0 COMMENT '剩余抽奖次数',
    `total_count` INT NOT NULL DEFAULT 0 COMMENT '累计获得次数',
    `used_count` INT NOT NULL DEFAULT 0 COMMENT '已使用次数',
    `last_update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    KEY `idx_remaining_count` (`remaining_count`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='抽奖机会表';

-- 抽奖机会规则表
DROP TABLE IF EXISTS `draw_chance_rule`;
CREATE TABLE `draw_chance_rule` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `chance_type` VARCHAR(50) NOT NULL COMMENT '机会类型: consume-消费得机会, check_in-每日签到, new_user-新用户专享',
    `trigger_condition` VARCHAR(200) DEFAULT NULL COMMENT '触发条件描述',
    `min_amount` DECIMAL(10,2) DEFAULT NULL COMMENT '最低消费金额(consume类型)',
    `give_count` INT NOT NULL DEFAULT 1 COMMENT '赠送次数',
    `daily_limit` INT DEFAULT NULL COMMENT '每日上限次数(consume类型)',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_chance_type` (`chance_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='抽奖机会规则表';

-- 抽奖机会明细表（记录每次获得和消耗）
DROP TABLE IF EXISTS `draw_chance_log`;
CREATE TABLE `draw_chance_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `type` TINYINT NOT NULL COMMENT '类型: 1-获得, 2-消耗',
    `source_type` VARCHAR(50) NOT NULL COMMENT '来源类型: consume-消费, check_in-签到, new_user-新用户, draw-抽奖消耗',
    `related_id` BIGINT DEFAULT NULL COMMENT '关联ID(订单ID/签到记录ID等)',
    `count` INT NOT NULL COMMENT '变动数量',
    `before_count` INT NOT NULL COMMENT '变动前次数',
    `after_count` INT NOT NULL COMMENT '变动后次数',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='抽奖机会明细表';

-- 风控配置表
DROP TABLE IF EXISTS `draw_risk_config`;
CREATE TABLE `draw_risk_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `config_key` VARCHAR(50) NOT NULL COMMENT '配置键: top_prize_limit-123等奖单人限中, abnormal_intercept-异常用户拦截, cheat_punishment-恶意刷奖处罚',
    `config_name` VARCHAR(100) NOT NULL COMMENT '配置名称',
    `config_type` VARCHAR(20) NOT NULL COMMENT '配置类型: boolean-布尔值, select-下拉选择',
    `config_value` VARCHAR(100) NOT NULL COMMENT '配置值',
    `default_value` VARCHAR(100) NOT NULL COMMENT '默认值',
    `options` TEXT COMMENT '选项JSON(select类型)',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '说明',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='风控配置表';

-- 用户中奖限制表（记录123等奖中奖情况）
DROP TABLE IF EXISTS `draw_prize_limit`;
CREATE TABLE `draw_prize_limit` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `prize_id` BIGINT NOT NULL COMMENT '奖品ID',
    `prize_level` TINYINT NOT NULL COMMENT '奖品等级: 1-一等奖, 2-二等奖, 3-三等奖',
    `win_count` INT NOT NULL DEFAULT 1 COMMENT '中奖次数',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_prize` (`user_id`, `prize_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户中奖限制表';

-- ============================================
-- 初始化数据
-- ============================================

-- 初始化积分规则
INSERT INTO `points_rule` (`type`, `points`, `consume_ratio`, `exchange_ratio`) VALUES
('check_in', 10, NULL, NULL),
('consume', 0, 10.00, NULL),
('exchange', 0, NULL, 5.00);

-- 初始化抽奖活动配置
INSERT INTO `draw_activity_config` (`title`, `status`) VALUES
('幸运大抽奖', 1);

-- 初始化抽奖机会规则
INSERT INTO `draw_chance_rule` (`chance_type`, `trigger_condition`, `min_amount`, `give_count`, `daily_limit`, `status`) VALUES
('consume', '小程序内实付订单完成', 0.00, 1, 3, 1),
('check_in', '会员当日首次签到', NULL, 1, NULL, 1),
('new_user', '首次注册会员并完成授权', NULL, 1, NULL, 1);

-- 初始化风控配置
INSERT INTO `draw_risk_config` (`config_key`, `config_name`, `config_type`, `config_value`, `default_value`, `options`, `description`) VALUES
('top_prize_limit', '123等奖单人限中1次', 'boolean', 'true', 'true', NULL, '中奖后自动屏蔽该奖项'),
('abnormal_intercept', '异常用户拦截', 'boolean', 'true', 'true', NULL, '拦截高频切换账号、机器操作用户'),
('cheat_punishment', '恶意刷奖处罚', 'select', 'cancel', 'cancel', '[{"value":"cancel","label":"取消中奖资格"},{"value":"restrict","label":"限制参与"},{"value":"ban","label":"封禁账号"}]', '恶意刷奖处罚方式');

-- 初始化奖品配置
INSERT INTO `prize` (`name`, `icon`, `type`, `points`, `probability`, `sort_order`, `valid_days`, `description`, `status`) VALUES
('招牌三合一（特大份）', 'food', 2, 0, 0.0600, 0, 3, '中奖后3日内到店核销使用', 1),
('招牌三合一（中份）', 'food', 2, 0, 0.0600, 1, 3, '中奖后3日内到店核销使用', 1),
('香酥鸡柳（中份）', 'food', 2, 0, 0.0200, 2, 3, '中奖后3日内到店核销使用', 1),
('全场通用5元券', 'ticket', 3, 0, 0.1800, 3, 10, '中奖后10日内使用，全场通用无门槛', 1),
('300元积分', 'star', 1, 300, 0.2500, 4, 10, '积分自动发放到账户，10日内有效', 1),
('250积分', 'star', 1, 250, 0.2000, 5, 10, '积分自动发放到账户，10日内有效', 1),
('200积分', 'star', 1, 200, 0.1800, 6, 10, '积分自动发放到账户，10日内有效', 1),
('谢谢参与', 'emoji', 0, 0, 0.0500, 7, NULL, '谢谢参与，下次好运！', 1);

-- 初始化测试商家手机号（可替换为实际商家手机号）
INSERT INTO `merchant_whitelist` (`phone`, `name`) VALUES
('15800682653', '测试商家');

-- 初始化测试商品数据
INSERT INTO `product` (`name`, `image`, `description`, `type`, `price`, `stock`, `status`) VALUES
('全场通用10元券', '', '全场通用，无门槛使用', 'coupon', 10.00, 100, 1),
('满100减30券', '', '满100元可用', 'coupon', 30.00, 50, 1),
('招牌三合一（中份）', '', '清爽解腻，招牌鸡柳 薯条年糕', 'dish', 18.00, 200, 1),
('招牌四合一(中份)', '', '外酥里嫩，香味四溢，鸡柳年糕薯条山药片', 'dish', 28.00, 80, 1),
('定制帆布袋', '', '环保材质，限量周边', 'gift', 39.00, 30, 1),
('定制水杯', '', '食品级材质，保温保冷', 'gift', 59.00, 20, 1);
