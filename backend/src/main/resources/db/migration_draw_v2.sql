-- 抽奖功能优化 - 数据库迁移脚本
-- 创建时间: 2026-04-18

USE wx_shop_member;

-- ============================================
-- 1. 新增表结构
-- ============================================

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
-- 2. 修改现有表结构
-- ============================================

-- 修改 prize 表，添加使用期限字段
ALTER TABLE `prize`
ADD COLUMN `valid_days` INT DEFAULT NULL COMMENT '使用期限天数' AFTER `coupon_value`,
ADD COLUMN `description` VARCHAR(500) DEFAULT NULL COMMENT '奖品使用说明' AFTER `valid_days`;

-- 修改 lucky_draw 表，添加使用截止时间和核销状态
ALTER TABLE `lucky_draw`
ADD COLUMN `expire_time` DATETIME DEFAULT NULL COMMENT '使用截止时间' AFTER `status`,
ADD COLUMN `verify_status` TINYINT DEFAULT 0 COMMENT '核销状态: 0-未使用, 1-已使用, 2-已过期' AFTER `expire_time`,
ADD COLUMN `verify_time` DATETIME DEFAULT NULL COMMENT '核销时间' AFTER `verify_status`,
ADD COLUMN `qr_code` VARCHAR(255) DEFAULT NULL COMMENT '核销二维码' AFTER `verify_time`;

-- ============================================
-- 3. 初始化数据
-- ============================================

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

-- 初始化奖品默认配置（更新现有奖品为需求文档中的配置）
-- 先清空现有奖品
DELETE FROM `prize`;

-- 插入新的默认奖品配置
INSERT INTO `prize` (`name`, `icon`, `type`, `points`, `probability`, `sort_order`, `valid_days`, `description`, `status`) VALUES
('招牌三合一（特大份）', 'food', 2, 0, 0.0600, 0, 3, '中奖后3日内到店核销使用', 1),
('招牌三合一（中份）', 'food', 2, 0, 0.0600, 1, 3, '中奖后3日内到店核销使用', 1),
('香酥鸡柳（中份）', 'food', 2, 0, 0.0200, 2, 3, '中奖后3日内到店核销使用', 1),
('全场通用5元券', 'ticket', 3, 0, 0.1800, 3, 10, '中奖后10日内使用，全场通用无门槛', 1),
('300元积分', 'star', 1, 300, 0.2500, 4, 10, '积分自动发放到账户，10日内有效', 1),
('250积分', 'star', 1, 250, 0.2000, 5, 10, '积分自动发放到账户，10日内有效', 1),
('200积分', 'star', 1, 200, 0.1800, 6, 10, '积分自动发放到账户，10日内有效', 1),
('谢谢参与', 'emoji', 0, 0, 0.0500, 7, NULL, '谢谢参与，下次好运！', 1);
