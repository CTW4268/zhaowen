import pymysql
from sentence_transformers import SentenceTransformer

# 1. 加载模型 (修正拼写)
model = SentenceTransformer('shibing624/text2vec-base-chinese')

# 2. 数据库连接 (保持不变)
db_mysql = pymysql.connect(host="192.168.1.238", user="root", password="Cc20051215", database="news", cursorclass=pymysql.cursors.DictCursor)
db_manticore = pymysql.connect(host="192.168.1.238", port=9306, autocommit=True)

try:
    with db_mysql.cursor() as cur_mysql, db_manticore.cursor() as cur_manti:
        cur_mysql.execute("SELECT id, link, link_md5, title, content, UNIX_TIMESTAMP(publish_time) as pt, source, keywords, UNIX_TIMESTAMP(fetch_time) as ft FROM news")
        
        count = 0
        for row in cur_mysql:
            # --- 本地向量化 ---
            text = row['content'] if row['content'] else row['title']
            if not text: continue
            
            vector = model.encode(text).tolist()
            # 关键：手动转为 (v1,v2,...) 格式的字符串
            vec_str = "(" + ",".join(map(str, vector)) + ")"

            # --- 关键：手动拼接 SQL 绕过驱动转义 ---
            # 其他字段为了安全，我们仍然使用 escape_string 处理
            sql = f"""
            REPLACE INTO products_rt 
            (id, link, link_md5, title, content, publish_time, source, keywords, fetch_time, content_vector) 
            VALUES (
                {int(row['id'])}, 
                '{db_manticore.escape_string(str(row['link']))}', 
                '{db_manticore.escape_string(str(row['link_md5']))}', 
                '{db_manticore.escape_string(str(row['title']))}', 
                '{db_manticore.escape_string(str(row['content']))}', 
                {int(row['pt'])}, 
                '{db_manticore.escape_string(str(row['source']))}', 
                '{db_manticore.escape_string(str(row['keywords']))}', 
                {int(row['ft'])}, 
                {vec_str}
            )
            """
            # 注意：上面最后一行 vec_str 前后没有引号，这才是 Manticore 想要的
            
            try:
                cur_manti.execute(sql)
                count += 1
                if count % 100 == 0:
                    print(f"已完成 {count} 条...")
            except Exception as e:
                print(f"ID {row['id']} 写入失败: {e}")

finally:
    db_mysql.close()
    db_manticore.close()