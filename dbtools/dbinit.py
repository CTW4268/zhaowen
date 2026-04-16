import mysql.connector
from mysql.connector import errorcode

# 1. 数据库配置信息（请根据你的实际情况修改）
db_config = {
    "host": "101.35.241.235",
    "port": "53306",
    "user": "root",
    "password": "Cc20051215",
    "database": "news-system"
}

# 2. 将你的建表语句整理成一个列表
# 注意：建表顺序建议先建“基础表”（如国家、来源），再建“关联表”
TABLES = {}

TABLES['country'] = (
    "CREATE TABLE `country` ("
    "  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '国家ID',"
    "  `country_name` VARCHAR(50) NOT NULL COMMENT '国家名称',"
    "  `continent` VARCHAR(20) COMMENT '所属大洲',"
    "  `weight` TINYINT DEFAULT 1 COMMENT '权重 ',"
    "  `status` TINYINT DEFAULT 1 COMMENT '是否启用：1启用 0禁用'"
    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='国家地区表';"
)

TABLES['news_source'] = (
    "CREATE TABLE `news_source` ("
    "  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '来源ID',"
    "  `source_name` VARCHAR(100) NOT NULL COMMENT '来源名称',"
    "  `weight` TINYINT DEFAULT 3 COMMENT '权重 1~5',"
    "  `status` TINYINT DEFAULT 1 COMMENT '是否启用'"
    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='新闻来源媒体表';"
)

TABLES['author'] = (
    "CREATE TABLE `author` ("
    "  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '作者ID',"
    "  `author_name` VARCHAR(50) NOT NULL COMMENT '作者姓名',"
    "  `country_id` INT COMMENT '所属国家ID',"
    "  `status` TINYINT DEFAULT 1 COMMENT '是否启用'"
    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='新闻作者表';"
)

TABLES['news'] = (
    "CREATE TABLE `news` ("
    "  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '新闻ID',"
    "  `title` VARCHAR(255) NOT NULL COMMENT '新闻标题',"
    "  `content` TEXT COMMENT '新闻正文',"
    "  `summary` VARCHAR(500) COMMENT '摘要',"
    "  `author_id` INT COMMENT '作者ID',"
    "  `source_id` INT COMMENT '来源ID',"
    "  `country_id` INT COMMENT '关联国家ID',"
    "  `is_domestic` TINYINT DEFAULT 0 COMMENT '1国内 0国外',"
    "  `publish_time` DATETIME COMMENT '发布时间',"
    "  `view_num` INT DEFAULT 0 COMMENT '阅读量',"
    "  `like_num` INT DEFAULT 0 COMMENT '点赞量',"
    "  `collect_num` INT DEFAULT 0 COMMENT '收藏量',"
    "  `final_weight` DECIMAL(10,2) DEFAULT 0 COMMENT '最终排序权重',"
    "  KEY `idx_publish_time` (`publish_time`),"
    "  KEY `idx_country` (`country_id`),"
    "  KEY `idx_final_weight` (`final_weight`)"
    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='新闻主表';"
)

TABLES['user'] = (
    "CREATE TABLE `user` ("
    "  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',"
    "  `ncname` VARCHAR(50) NOT NULL COMMENT '昵称',"
    "  `email` VARCHAR(100) UNIQUE NOT NULL COMMENT '邮箱',"
    "  `password` VARCHAR(255) NOT NULL COMMENT '密码哈希',"
    "  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'"
    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';"
)

TABLES['user_collect'] = (
    "CREATE TABLE `user_collect` ("
    "  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '收藏ID',"
    "  `user_id` INT NOT NULL COMMENT '用户ID',"
    "  `news_id` INT NOT NULL COMMENT '新闻ID',"
    "  `collect_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',"
    "  UNIQUE KEY `uk_user_news` (`user_id`, `news_id`)"
    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收藏表';"
)

def create_database(cursor):
    try:
        # 使用反引号包裹数据库名，避免连字符等特殊字符导致的语法错误
        cursor.execute(
            f"CREATE DATABASE IF NOT EXISTS `{db_config['database']}` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci"
        )
        print(f"✅ 数据库 '{db_config['database']}' 创建成功或已存在")
    except mysql.connector.Error as err:
        print(f"❌ 创建数据库失败: {err}")
        exit(1)

def run_setup():
    try:
        # 尝试连接
        cnx = mysql.connector.connect(user=db_config['user'], 
                                      password=db_config['password'],
                                      host=db_config['host'],
                                      port=db_config['port']
                                      )
        cursor = cnx.cursor()
        
        # 确保数据库存在
        create_database(cursor)
        cnx.database = db_config['database']

        for table_name in TABLES:
            table_description = TABLES[table_name]
            try:
                print(f"正在创建表 {table_name}: ", end='')
                cursor.execute(table_description)
            except mysql.connector.Error as err:
                if err.errno == errorcode.ER_TABLE_EXISTS_ERROR:
                    print("该表已存在。")
                else:
                    print(err.msg)
            else:
                print("成功！")

        cursor.close()
        cnx.close()
        print("\n所有表操作已完成。")

    except mysql.connector.Error as err:
        if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
            print("用户名或密码错误。")
        else:
            print(err)

if __name__ == "__main__":
    run_setup()