-- ============================================
-- news-system 数据库初始化脚本
-- 适用于 MySQL 8.0+
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `news-system` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `news-system`;

-- ============================================
-- 基础数据表
-- ============================================

-- 国家地区表
CREATE TABLE IF NOT EXISTS `country` (
    `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '国家ID',
    `country_name` VARCHAR(50) NOT NULL UNIQUE COMMENT '国家名称',
    `continent` VARCHAR(20) COMMENT '所属大洲',
    `weight` TINYINT DEFAULT 1 COMMENT '权重',
    `status` TINYINT DEFAULT 1 COMMENT '是否启用：1启用 0禁用',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_country_name` (`country_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='国家地区表';

-- 新闻来源媒体表
CREATE TABLE IF NOT EXISTS `news_source` (
    `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '来源ID',
    `source_name` VARCHAR(100) NOT NULL UNIQUE COMMENT '来源名称',
    `weight` TINYINT DEFAULT 3 COMMENT '权重 1~5',
    `status` TINYINT DEFAULT 1 COMMENT '是否启用',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_source_name` (`source_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='新闻来源媒体表';

-- 新闻作者表
CREATE TABLE IF NOT EXISTS `author` (
    `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '作者ID',
    `author_name` VARCHAR(100) NOT NULL UNIQUE COMMENT '作者姓名',
    `country_id` INT COMMENT '所属国家ID',
    `status` TINYINT DEFAULT 1 COMMENT '是否启用',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`country_id`) REFERENCES `country`(`id`) ON DELETE SET NULL,
    INDEX `idx_author_name` (`author_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='新闻作者表';

-- ============================================
-- 核心业务表
-- ============================================

-- 新闻主表
CREATE TABLE IF NOT EXISTS `news` (
    `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '新闻ID',
    `title` VARCHAR(255) NOT NULL COMMENT '新闻标题',
    `content` TEXT COMMENT '新闻正文',
    `summary` VARCHAR(500) COMMENT '摘要',
    `author_id` INT COMMENT '作者ID',
    `source_id` INT COMMENT '来源ID',
    `country_id` INT COMMENT '关联国家ID',
    `is_domestic` TINYINT DEFAULT 0 COMMENT '1国内 0国外',
    `publish_time` DATETIME COMMENT '发布时间',
    `view_num` INT DEFAULT 0 COMMENT '阅读量',
    `like_num` INT DEFAULT 0 COMMENT '点赞量',
    `collect_num` INT DEFAULT 0 COMMENT '收藏量',
    `final_weight` DECIMAL(10,2) DEFAULT 0 COMMENT '最终排序权重',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`author_id`) REFERENCES `author`(`id`) ON DELETE SET NULL,
    FOREIGN KEY (`source_id`) REFERENCES `news_source`(`id`) ON DELETE SET NULL,
    FOREIGN KEY (`country_id`) REFERENCES `country`(`id`) ON DELETE SET NULL,
    INDEX `idx_publish_time` (`publish_time`),
    INDEX `idx_is_domestic` (`is_domestic`),
    INDEX `idx_final_weight` (`final_weight`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='新闻主表';

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `ncname` VARCHAR(50) NOT NULL COMMENT '昵称',
    `email` VARCHAR(100) UNIQUE NOT NULL COMMENT '邮箱',
    `password` VARCHAR(255) NOT NULL COMMENT '密码哈希',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 用户收藏表
CREATE TABLE IF NOT EXISTS `user_collect` (
    `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '收藏ID',
    `user_id` INT NOT NULL COMMENT '用户ID',
    `news_id` INT NOT NULL COMMENT '新闻ID',
    `collect_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    UNIQUE KEY `uk_user_news` (`user_id`, `news_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_news_id` (`news_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户收藏表';

-- ============================================
-- 插入测试数据 - 国家
-- ============================================
INSERT INTO `country` (`country_name`, `continent`, `weight`, `status`) VALUES
('中国', '亚洲', 5, 1),
('美国', '美洲', 4, 1),
('日本', '亚洲', 3, 1),
('英国', '欧洲', 3, 1),
('法国', '欧洲', 3, 1),
('德国', '欧洲', 3, 1),
('俄罗斯', '欧洲', 3, 1),
('印度', '亚洲', 2, 1),
('韩国', '亚洲', 2, 1),
('澳大利亚', '大洋洲', 2, 1),
('加拿大', '美洲', 2, 1),
('巴西', '美洲', 1, 1),
('南非', '非洲', 1, 1),
('埃及', '非洲', 1, 1),
('新加坡', '亚洲', 2, 1);

-- ============================================
-- 插入测试数据 - 新闻来源
-- ============================================
INSERT INTO `news_source` (`source_name`, `weight`, `status`) VALUES
('新华网', 5, 1),
('人民网', 5, 1),
('央视网', 4, 1),
('人民日报', 5, 1),
('新华社', 5, 1),
('路透社', 4, 1),
('法新社', 4, 1),
('美联社', 4, 1),
('共同社', 3, 1),
('BBC', 4, 1),
('CNN', 4, 1),
('纽约时报', 4, 1),
('卫报', 3, 1),
('朝日新闻', 3, 1),
('澳联社', 2, 1);

-- ============================================
-- 插入测试数据 - 作者
-- ============================================
INSERT INTO `author` (`author_name`, `country_id`, `status`) VALUES
('记者张三', 1, 1),
('记者李四', 1, 1),
('记者王五', 1, 1),
('记者赵六', 1, 1),
('John Smith', 2, 1),
('Pierre Dupont', 5, 1),
('Tanaka Hiroshi', 3, 1),
('James Wilson', 4, 1),
('Maria Garcia', 12, 1),
('Ahmed Hassan', 14, 1);

-- ============================================
-- 插入测试数据 - 国内新闻
-- ============================================
INSERT INTO `news` (`title`, `summary`, `content`, `author_id`, `source_id`, `country_id`, `is_domestic`, `publish_time`, `view_num`, `like_num`, `collect_num`) VALUES
('北京市发布新政策促进科技创新', '北京市政府出台多项措施支持科技创新发展', '详细内容...', 1, 1, 1, 1, NOW(), 1000, 50, 20),
('上海市优化营商环境新举措', '上海推出新一轮营商环境改革措施', '详细内容...', 2, 2, 1, 1, DATE_SUB(NOW(), INTERVAL 1 DAY), 800, 40, 15),
('广东省加强与港澳合作', '粤港澳大湾区建设取得新进展', '详细内容...', 3, 3, 1, 1, DATE_SUB(NOW(), INTERVAL 2 DAY), 600, 30, 10),
('江苏省推进民生工程建设', '江苏省加大民生工程投入力度', '详细内容...', 4, 4, 1, 1, DATE_SUB(NOW(), INTERVAL 3 DAY), 500, 25, 8);

-- ============================================
-- 插入测试数据 - 海外新闻
-- ============================================
INSERT INTO `news` (`title`, `summary`, `content`, `author_id`, `source_id`, `country_id`, `is_domestic`, `publish_time`, `view_num`, `like_num`, `collect_num`) VALUES
('中美举行经贸磋商', '中美双方就经贸问题举行磋商', '详细内容...', 5, 6, 2, 0, NOW(), 1200, 60, 25),
('欧盟推出新的经济刺激计划', '欧盟委员会提出经济复苏方案', '详细内容...', 6, 7, 5, 0, DATE_SUB(NOW(), INTERVAL 1 DAY), 900, 45, 18),
('日本加强科技创新投入', '日本政府增加科研预算', '详细内容...', 7, 9, 3, 0, DATE_SUB(NOW(), INTERVAL 2 DAY), 700, 35, 12),
('澳大利亚改善民生福利', '澳大利亚政府提高福利标准', '详细内容...', 9, 15, 10, 0, DATE_SUB(NOW(), INTERVAL 3 DAY), 400, 20, 8);

-- ============================================
-- 插入测试数据 - 用户（密码为 123456 的 BCrypt 加密）
-- ============================================
INSERT INTO `user` (`ncname`, `email`, `password`) VALUES
('testuser', 'test@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8WqR3nGhS1vPzKxQ.YLzJ5xCcVWtC'),
('admin', 'admin@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8WqR3nGhS1vPzKxQ.YLzJ5xCcVWtC');

-- ============================================
-- 完成提示
-- ============================================
SELECT '数据库初始化完成！' AS message;
SELECT COUNT(*) AS country_count FROM country;
SELECT COUNT(*) AS source_count FROM news_source;
SELECT COUNT(*) AS author_count FROM author;
SELECT COUNT(*) AS news_count FROM news;
SELECT COUNT(*) AS user_count FROM user;
