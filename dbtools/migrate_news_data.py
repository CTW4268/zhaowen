import mysql.connector
from mysql.connector import errorcode
import re
from datetime import datetime

# 1. 数据库配置信息（请根据你的实际情况修改）
db_config = {
    "host": "101.35.241.235",
    "port": "53306",
    "user": "root",
    "password": "Cc20051215",
    "database": "news-system"
}

def migrate_data(source_table='old_news'):
    """
    将原始news数据迁移到新的规范化表结构中
    
    Args:
        source_table: 源数据表名，默认为'old_news'
    """
    try:
        # 连接数据库
        cnx = mysql.connector.connect(**db_config)
        cursor = cnx.cursor(dictionary=True)
        
        print("开始数据迁移...")
        
        # 检查源表是否存在
        cursor.execute(f"SHOW TABLES LIKE '{source_table}'")
        table_exists = cursor.fetchone()
        
        if not table_exists:
            print(f"警告: 源表 '{source_table}' 不存在")
            print("为了演示，我们将创建一个示例的原始数据表")
            
            # 创建一个示例的原始数据表
            create_old_table_query = f"""
            CREATE TABLE IF NOT EXISTS {source_table} (
            id INT PRIMARY KEY AUTO_INCREMENT,
            title VARCHAR(255) NOT NULL,
            content TEXT,
            summary VARCHAR(500),
            author_name VARCHAR(100),  -- 原始的作者名称
            source_name VARCHAR(100),  -- 原始的来源名称
            country_name VARCHAR(50),  -- 原始的国家名称
            is_domestic TINYINT DEFAULT 0,
            publish_time DATETIME,
            view_num INT DEFAULT 0,
            like_num INT DEFAULT 0,
            collect_num INT DEFAULT 0,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
            """
            
            cursor.execute(create_old_table_query)
            
            # 插入一些示例数据用于测试
            sample_data_query = f"""
            INSERT IGNORE INTO {source_table} (title, content, summary, author_name, source_name, country_name, is_domestic, publish_time, view_num, like_num, collect_num) VALUES
        ('中国科技发展迅速', '中国在科技领域取得了巨大进步...', '中国科技进展摘要', '张三', '人民日报', '中国', 1, '2023-01-15 10:30:00', 1000, 50, 20),
        ('美国股市波动', '美国股市近期出现大幅波动...', '美股波动摘要', 'John Smith', 'CNN', '美国', 0, '2023-01-16 14:20:00', 800, 30, 15),
        ('日本新技术发布', '日本公司发布了最新技术...', '日本新技术摘要', '田中太郎', '朝日新闻', '日本', 0, '2023-01-17 09:15:00', 600, 25, 10);
            """
            
            cursor.execute(sample_data_query)
            cnx.commit()
            
            print("示例数据已准备就绪")
        else:
            print(f"找到源表: {source_table}")
        
        # 获取所有唯一的国家
        cursor.execute(f"SELECT DISTINCT country_name FROM {source_table} WHERE country_name IS NOT NULL AND country_name != ''")
        countries = cursor.fetchall()
        
        country_mapping = {}  # 用于存储国家名称到ID的映射
        for country in countries:
            country_name = country['country_name']
            # 检查国家是否已存在
            cursor.execute("SELECT id FROM country WHERE country_name = %s", (country_name,))
            existing_country = cursor.fetchone()
            
            if existing_country:
                country_id = existing_country['id']
            else:
                # 插入新国家
                cursor.execute(
                    "INSERT INTO country (country_name, continent, weight, status) VALUES (%s, %s, %s, %s)",
                    (country_name, '未知', 1, 1)
                )
                country_id = cursor.lastrowid
            
            country_mapping[country_name] = country_id
        
        print(f"处理了 {len(country_mapping)} 个国家")
        
        # 获取所有唯一的作者
        cursor.execute("SELECT DISTINCT author_name FROM old_news WHERE author_name IS NOT NULL AND author_name != ''")
        authors = cursor.fetchall()
        
        author_mapping = {}  # 用于存储作者名称到ID的映射
        for author in authors:
            author_name = author['author_name']
            # 检查作者是否已存在
            cursor.execute("SELECT id FROM author WHERE author_name = %s", (author_name,))
            existing_author = cursor.fetchone()
            
            if existing_author:
                author_id = existing_author['id']
            else:
                # 尝试从国家映射中找到对应的国家ID
                country_id = None
                # 查找该作者对应的国家（取第一个匹配的国家）
                cursor.execute(f"SELECT country_name FROM {source_table} WHERE author_name = %s AND country_name IS NOT NULL LIMIT 1", (author_name,))
                author_country = cursor.fetchone()
                if author_country and author_country['country_name'] in country_mapping:
                    country_id = country_mapping[author_country['country_name']]
                
                # 插入新作者
                cursor.execute(
                    "INSERT INTO author (author_name, country_id, status) VALUES (%s, %s, %s)",
                    (author_name, country_id, 1)
                )
                author_id = cursor.lastrowid
            
            author_mapping[author_name] = author_id
        
        print(f"处理了 {len(author_mapping)} 个作者")
        
        # 获取所有唯一的来源
        cursor.execute("SELECT DISTINCT source_name FROM old_news WHERE source_name IS NOT NULL AND source_name != ''")
        sources = cursor.fetchall()
        
        source_mapping = {}  # 用于存储来源名称到ID的映射
        for source in sources:
            source_name = source['source_name']
            # 检查来源是否已存在
            cursor.execute("SELECT id FROM news_source WHERE source_name = %s", (source_name,))
            existing_source = cursor.fetchone()
            
            if existing_source:
                source_id = existing_source['id']
            else:
                # 插入新来源
                cursor.execute(
                    "INSERT INTO news_source (source_name, weight, status) VALUES (%s, %s, %s)",
                    (source_name, 3, 1)
                )
                source_id = cursor.lastrowid
            
            source_mapping[source_name] = source_id
        
        print(f"处理了 {len(source_mapping)} 个新闻来源")
        
        # 迁移新闻数据
        cursor.execute(f"SELECT * FROM {source_table}")
        old_news_list = cursor.fetchall()
        
        migrated_count = 0
        for old_news in old_news_list:
            # 获取对应的外键ID
            country_id = country_mapping.get(old_news['country_name']) if old_news['country_name'] else None
            author_id = author_mapping.get(old_news['author_name']) if old_news['author_name'] else None
            source_id = source_mapping.get(old_news['source_name']) if old_news['source_name'] else None
            
            # 插入到新的news表
            cursor.execute(
                """INSERT INTO news 
                   (title, content, summary, author_id, source_id, country_id, is_domestic, 
                    publish_time, view_num, like_num, collect_num, final_weight) 
                   VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)""",
                (
                    old_news['title'],
                    old_news['content'],
                    old_news['summary'],
                    author_id,
                    source_id,
                    country_id,
                    old_news['is_domestic'],
                    old_news['publish_time'],
                    old_news['view_num'],
                    old_news['like_num'],
                    old_news['collect_num'],
                    0.0  # 初始权重设为0
                )
            )
            migrated_count += 1
        
        cnx.commit()
        print(f"成功迁移了 {migrated_count} 条新闻数据")
        
        # 关闭连接
        cursor.close()
        cnx.close()
        
        print("数据迁移完成！")
        
    except mysql.connector.Error as err:
        if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
            print("用户名或密码错误。")
        elif err.errno == errorcode.ER_BAD_DB_ERROR:
            print("数据库不存在。")
        else:
            print(f"发生错误: {err}")
        return False
    
    return True

if __name__ == "__main__":
    import sys
    
    # 从命令行参数获取源表名，如果没有提供则使用默认值
    source_table = 'old_news'
    if len(sys.argv) > 1:
        source_table = sys.argv[1]
    
    print(f"使用源表: {source_table}")
    success = migrate_data(source_table)
    if success:
        print("\n✅ 数据迁移成功完成！")
    else:
        print("\n❌ 数据迁移失败！")