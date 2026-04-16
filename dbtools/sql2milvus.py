import mysql.connector
from pymilvus import connections, Collection, CollectionSchema, FieldSchema, DataType, utility
import numpy as np
from sentence_transformers import SentenceTransformer
import time

# --- 配置区 ---
MYSQL_CONFIG = {
    "host": "101.35.241.235",
    "user": "root",
    "password": "Cc20051215",
    "database": "news", 
    "port": "53306"
}

# Milvus 配置
MILVUS_CONFIG = {
    "host": "localhost",  # Milvus 服务器地址
    "port": "19530",       # Milvus 端口
}

# 向量模型配置
MODEL_NAME = 'sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2'
VECTOR_DIM = 384  # 该模型的向量维度

# --- 初始化连接 ---
db_mysql = mysql.connector.connect(**MYSQL_CONFIG)
cur_ms = db_mysql.cursor(dictionary=True)

# 加载向量模型
print(f"正在加载向量模型: {MODEL_NAME}...")
model = SentenceTransformer(MODEL_NAME)
print("✅ 模型加载完成")

def setup_milvus():
    """初始化 Milvus 连接并创建集合"""
    print("正在连接 Milvus...")
    try:
        connections.connect(
            alias="default",
            host=MILVUS_CONFIG["host"],
            port=MILVUS_CONFIG["port"]
        )
        print("✅ Milvus 连接成功")
    except Exception as e:
        print(f"❌ Milvus 连接失败: {e}")
        raise e
    
    # 检查并删除旧集合
    collection_name = "news_vectors"
    if utility.has_collection(collection_name):
        print(f"正在删除旧集合: {collection_name}...")
        utility.drop_collection(collection_name)
        print("✅ 旧集合已删除")
    
    # 定义字段 schema
    fields = [
        FieldSchema(name="id", dtype=DataType.INT64, is_primary=True, auto_id=False),
        FieldSchema(name="link", dtype=DataType.VARCHAR, max_length=500),
        FieldSchema(name="link_md5", dtype=DataType.VARCHAR, max_length=100),
        FieldSchema(name="publish_time", dtype=DataType.INT64),  # 使用时间戳
        FieldSchema(name="source", dtype=DataType.VARCHAR, max_length=200),
        FieldSchema(name="fetch_time", dtype=DataType.INT64),  # 使用时间戳
        FieldSchema(name="title", dtype=DataType.VARCHAR, max_length=1000),
        FieldSchema(name="content", dtype=DataType.VARCHAR, max_length=50000),
        FieldSchema(name="keywords", dtype=DataType.VARCHAR, max_length=1000),
        FieldSchema(name="content_vector", dtype=DataType.FLOAT_VECTOR, dim=VECTOR_DIM)
    ]
    
    schema = CollectionSchema(fields, description="新闻向量集合")
    
    print(f"正在创建集合: {collection_name}...")
    collection = Collection(name=collection_name, schema=schema)
    
    # 创建索引 (使用 IVF_FLAT 索引，适合中等规模数据)
    index_params = {
        "metric_type": "COSINE",  # 余弦相似度
        "index_type": "IVF_FLAT",
        "params": {"nlist": 128}
    }
    
    print("正在为 content_vector 字段创建索引...")
    collection.create_index(field_name="content_vector", index_params=index_params)
    print("✅ 索引创建完成")
    
    # 加载集合到内存
    print("正在加载集合到内存...")
    collection.load()
    print("✅ 集合已加载到内存，可以进行搜索")
    
    return collection

def generate_vectors(texts):
    """批量生成文本向量"""
    if not texts:
        return []
    
    # 过滤空文本
    valid_texts = [t if t and len(t.strip()) > 0 else " " for t in texts]
    
    # 批量编码
    embeddings = model.encode(valid_texts, batch_size=32, show_progress_bar=False)
    
    # 转换为列表格式
    return [emb.tolist() for emb in embeddings]

def sync_data():
    """从 MySQL 同步数据到 Milvus"""
    print("\n开始从 MySQL 同步数据...")
    
    # 从 MySQL 获取数据
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
    
    print(f"获取到 {total} 条数据，准备同步至 Milvus...")
    
    # 连接到 Milvus 集合
    collection_name = "news_vectors"
    collection = Collection(collection_name)
    
    # 批量处理
    batch_size = 100
    success_count = 0
    
    for i in range(0, total, batch_size):
        batch = rows[i:i+batch_size]
        
        # 提取字段
        ids = [r['id'] for r in batch]
        links = [r['link'] or "" for r in batch]
        link_md5s = [r['link_md5'] or "" for r in batch]
        publish_times = [r['pt'] or 0 for r in batch]
        sources = [r['source'] or "" for r in batch]
        fetch_times = [r['ft'] or 0 for r in batch]
        titles = [r['title'] or "" for r in batch]
        contents = [r['content'] or "" for r in batch]
        keywords_list = [r['keywords'] or "" for r in batch]
        
        # 生成内容向量（优先使用 content，如果为空则使用 title）
        vector_texts = []
        for r in batch:
            text = r['content'] if r['content'] and len(r['content'].strip()) > 10 else r['title']
            vector_texts.append(text if text else " ")
        
        print(f"  正在生成 {len(vector_texts)} 个向量...")
        vectors = generate_vectors(vector_texts)
        
        # 准备插入数据
        entities = [
            ids,
            links,
            link_md5s,
            publish_times,
            sources,
            fetch_times,
            titles,
            contents,
            keywords_list,
            vectors
        ]
        
        try:
            # 插入数据
            collection.insert(entities)
            success_count += len(batch)
            print(f"进度：{success_count}/{total} 已同步")
        except Exception as e:
            print(f"❌ 批量插入时发生异常 (在 {i} 到 {i+batch_size} 条): {e}")
            break
    
    # 刷新集合，确保数据持久化
    print("\n正在刷新集合...")
    collection.flush()
    print(f"✅ 数据同步完成！共同步 {success_count}/{total} 条记录")
    print(f"集合统计信息:")
    print(f"  - 总记录数: {collection.num_entities}")
    print(f"  - 向量维度: {VECTOR_DIM}")
    print(f"  - 索引类型: IVF_FLAT")
    print(f"  - 相似度度量: COSINE")

def test_search(query_text, top_k=5):
    """测试向量搜索功能"""
    print(f"\n🔍 测试搜索: '{query_text}'")
    
    # 生成查询向量
    query_vector = model.encode([query_text])[0].tolist()
    
    # 连接到集合
    collection_name = "news_vectors"
    collection = Collection(collection_name)
    
    # 设置搜索参数
    search_params = {
        "metric_type": "COSINE",
        "params": {"nprobe": 10}
    }
    
    # 执行搜索
    results = collection.search(
        data=[query_vector],
        anns_field="content_vector",
        param=search_params,
        limit=top_k,
        output_fields=["id", "title", "source", "publish_time"]
    )
    
    # 打印结果
    print(f"\n找到 {len(results[0])} 条相关结果:")
    print("-" * 80)
    for hit in results[0]:
        print(f"ID: {hit.id}")
        print(f"标题: {hit.entity.get('title')[:100]}...")
        print(f"来源: {hit.entity.get('source')}")
        print(f"相似度: {hit.score:.4f}")
        print("-" * 80)

if __name__ == "__main__":
    try:
        # 1. 初始化 Milvus
        setup_milvus()
        
        # 2. 同步数据
        sync_data()
        
        
    except Exception as e:
        print(f"\n❌ 程序执行出错: {e}")
        import traceback
        traceback.print_exc()
    finally:
        # 关闭 MySQL 连接
        if db_mysql.is_connected():
            cur_ms.close()
            db_mysql.close()
            print("✅ MySQL 连接已关闭")
        
        # 断开 Milvus 连接
        try:
            connections.disconnect("default")
            print("✅ Milvus 连接已断开")
        except:
            pass
        
        print("\n所有的数据库连接已安全释放。")