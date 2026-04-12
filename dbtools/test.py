import mysql.connector
import time

# --- 配置区 ---
MYSQL_CONFIG = {
    "host": "192.168.1.221",
    "user": "root",
    "password": "Cc20051215",
    "database": "news" 
}
MANTICORE_CONFIG = {
    "host": "127.0.0.1",
    "port": 9306
}

# --- 初始化连接 ---
db_mysql = mysql.connector.connect(**MYSQL_CONFIG)
db_manticore = mysql.connector.connect(**MANTICORE_CONFIG)
cur_ms = db_mysql.cursor(dictionary=True)
cur_mc = db_manticore.cursor()

def setup_manticore():
    print("正在清理旧表...")
    cur_mc.execute("DROP TABLE IF EXISTS products_rt")
    
    # 【核心修复】使用绝对无换行、无缩进的单行 SQL。
    # 所有向量参数作为 FLOAT_VECTOR 的属性，用空格隔开，放在表定义的括号内部。
    create_sql = (
        "CREATE TABLE products_rt ("
        "link string, "
        "link_md5 string, "
        "publish_time timestamp, "
        "source string, "
        "fetch_time timestamp, "
        "title text, "
        "content text, "
        "keywords text, "
        "content_vector float_vector "
        "knn_type='hnsw' "
        "hnsw_similarity='cosine' "
        "model_name='sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2' "
        "from='content' "
        "use_gpu='1'"
        ")"
    )
    
    print("正在创建 products_rt 表并配置 GPU 向量引擎...")
    try:
        cur_mc.execute(create_sql)
        print("✅ 表结构已就绪，GPU 加速指令已下发。")
    except mysql.connector.Error as e:
        print(f"❌ 建表报错: {e}")
        print(f"发送的原始 SQL: {create_sql}")
        raise e

def sync_data():
    print("\n开始从 MySQL 同步数据...")
    # 从 MySQL 捞取原始数据 (转化为 UNIX 时间戳以适配 Manticore)
    cur_ms.execute("""
        SELECT id, link, link_md5, title, content, 
               UNIX_TIMESTAMP(publish_time) as pt, 
               source, keywords, 
               UNIX_TIMESTAMP(fetch_time) as ft 
        FROM news
    """)
    
    rows = cur_ms.fetchall()
    total = len(rows)
    
    if total == 0:
        print("MySQL 中没有找到数据。")
        return
        
    print(f"获取到 {total} 条数据，准备批量同步至 Manticore...")
    
    # 批量插入以提高 GPU 吞吐量
    batch_size = 50 
    
    # 插入语句 (Manticore 默认拥有隐含的 id 主键，因此这里直接指定 id)
    insert_sql = """
    REPLACE INTO products_rt (id, link, link_md5, publish_time, source, fetch_time, title, content, keywords)
    VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
    """
    
    for i in range(0, total, batch_size):
        batch = rows[i:i+batch_size]
        
        # 组装批量参数
        batch_data = [
            (r['id'], r['link'], r['link_md5'], r['pt'], r['source'], r['ft'], r['title'], r['content'], r['keywords'])
            for r in batch
        ]
        
        try:
            cur_mc.executemany(insert_sql, batch_data)
            db_manticore.commit()
            print(f"进度：{min(i+batch_size, total)}/{total} 同步中...")
        except mysql.connector.Error as e:
            print(f"❌ 批量插入时发生异常 (在 {i} 到 {i+batch_size} 条): {e}")
            break

    print("\n✅ 数据同步任务结束！")
    print("提示：如果这是你第一次运行，Manticore 将会在后台连接 Hugging Face 下载模型。")
    print("请在 WSL2 终端运行 `watch -n 1 nvidia-smi` 监控 GPU 发力状况。")

if __name__ == "__main__":
    try:
        setup_manticore()
        sync_data()
    finally:
        # 确保安全关闭游标和数据库连接
        if db_mysql.is_connected():
            cur_ms.close()
            db_mysql.close()
        if db_manticore.is_connected():
            cur_mc.close()
            db_manticore.close()
        print("所有的数据库连接已安全释放。")