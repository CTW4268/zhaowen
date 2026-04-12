-- 新闻中心网站数据库初始化脚本
-- MySQL 8.0+

-- 创建数据库
CREATE DATABASE IF NOT EXISTS news_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE news_db;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    avatar VARCHAR(255) DEFAULT '',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 新闻表
CREATE TABLE IF NOT EXISTS news (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    summary TEXT,
    content LONGTEXT,
    cover_image VARCHAR(500),
    type ENUM('DOMESTIC', 'OVERSEAS', 'POLITICS') NOT NULL,
    province VARCHAR(50),
    region VARCHAR(50),
    country VARCHAR(50),
    category VARCHAR(50),
    source VARCHAR(100),
    author VARCHAR(100),
    view_count INT DEFAULT 0,
    is_carousel BOOLEAN DEFAULT FALSE,
    carousel_order INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    published_at DATETIME,
    INDEX idx_type (type),
    INDEX idx_category (category),
    INDEX idx_province (province),
    INDEX idx_country (country),
    INDEX idx_published (published_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='新闻表';

-- 收藏表
CREATE TABLE IF NOT EXISTS favorites (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    news_id BIGINT NOT NULL,
    news_type VARCHAR(20) NOT NULL,
    news_title VARCHAR(255) NOT NULL,
    news_cover VARCHAR(500),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_news (user_id, news_id),
    INDEX idx_user_id (user_id),
    INDEX idx_news_type (news_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收藏表';

-- 阅读历史表
CREATE TABLE IF NOT EXISTS reading_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    news_id BIGINT NOT NULL,
    news_title VARCHAR(255) NOT NULL,
    news_type VARCHAR(20) NOT NULL,
    news_category VARCHAR(50),
    read_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_time (user_id, read_at),
    INDEX idx_news_id (news_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='阅读历史表';

-- 反馈表
CREATE TABLE IF NOT EXISTS feedbacks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    type ENUM('BUG', 'FEATURE', 'UI', 'OTHER') NOT NULL,
    subject VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    contact VARCHAR(100),
    status ENUM('PENDING', 'PROCESSING', 'RESOLVED', 'CLOSED') DEFAULT 'PENDING',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_status (status),
    INDEX idx_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='反馈表';

-- 插入测试数据 - 用户（密码为 123456 的 BCrypt 加密）
INSERT INTO users (username, password, avatar) VALUES 
('testuser', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8WqR3nGhS1vPzKxQ.YLzJ5xCcVWtC', '/avatar/default.png'),
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8WqR3nGhS1vPzKxQ.YLzJ5xCcVWtC', '/avatar/admin.png');

-- 插入测试数据 - 国内新闻
INSERT INTO news (title, summary, content, cover_image, type, province, category, source, author, is_carousel, carousel_order, published_at) VALUES
('北京市发布新政策促进科技创新', '北京市政府出台多项措施支持科技创新发展', '详细内容...', '/images/news/beijing-tech.jpg', 'DOMESTIC', '北京', 'shizheng', '新华网', '记者张三', TRUE, 1, NOW()),
('上海市优化营商环境新举措', '上海推出新一轮营商环境改革措施', '详细内容...', '/images/news/shanghai-business.jpg', 'DOMESTIC', '上海', 'caijing', '人民网', '记者李四', TRUE, 2, NOW()),
('广东省加强与港澳合作', '粤港澳大湾区建设取得新进展', '详细内容...', '/images/news/guangdong-cooperation.jpg', 'DOMESTIC', '广东', 'waiwu', '央视网', '记者王五', FALSE, 0, NOW()),
('江苏省推进民生工程建设', '江苏省加大民生工程投入力度', '详细内容...', '/images/news/jiangdong-livelihood.jpg', 'DOMESTIC', '江苏', 'minsheng', '新华日报', '记者赵六', FALSE, 0, NOW());

-- 插入测试数据 - 海外新闻
INSERT INTO news (title, summary, content, cover_image, type, region, country, category, source, author, is_carousel, carousel_order, published_at) VALUES
('中美举行经贸磋商', '中美双方就经贸问题举行磋商', '详细内容...', '/images/news/us-china-talk.jpg', 'OVERSEAS', '美洲', '美国', 'waiwu', '路透社', '记者 John', TRUE, 1, NOW()),
('欧盟推出新的经济刺激计划', '欧盟委员会提出经济复苏方案', '详细内容...', '/images/news/eu-economy.jpg', 'OVERSEAS', '欧洲', '法国', 'caijing', '法新社', '记者 Pierre', TRUE, 2, NOW()),
('日本加强科技创新投入', '日本政府增加科研预算', '详细内容...', '/images/news/japan-tech.jpg', 'OVERSEAS', '亚洲', '日本', 'shizheng', '共同社', '记者 Tanaka', FALSE, 0, NOW()),
('澳大利亚改善民生福利', '澳大利亚政府提高福利标准', '详细内容...', '/images/news/australia-welfare.jpg', 'OVERSEAS', '大洋洲', '澳大利亚', 'minsheng', '澳联社', '记者 Smith', FALSE, 0, NOW());

-- 插入测试数据 - 政治新闻
INSERT INTO news (title, summary, content, cover_image, type, category, source, author, is_carousel, carousel_order, published_at) VALUES
('全国两会胜利召开', '第十四届全国人民代表大会第一次会议在北京召开', '详细内容...', '/images/news/lianghui.jpg', 'POLITICS', 'shizheng', '新华网', '记者团队', TRUE, 1, NOW()),
('中央经济工作会议精神解读', '深入学习中央经济工作会议精神', '详细内容...', '/images/news/economy-meeting.jpg', 'POLITICS', 'caijing', '人民日报', '评论员', TRUE, 2, NOW()),
('全面从严治党取得新成效', '党风廉政建设和反腐败斗争取得重大成果', '详细内容...', '/images/news/party-discipline.jpg', 'POLITICS', 'shizheng', '中央纪委', '记者团队', FALSE, 0, NOW());
