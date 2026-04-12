import mysql.connector
import time
import subprocess

# Manticore 连接配置
config = {
    'host': '127.0.0.1',
    'port': 9306,  # Manticore 默认 SQL 端口
    'user': 'root',
    'password': '',
    'database': ''
}

def check_gpu_process():
    """检查是否有 manticore 相关的进程正在使用 GPU"""
    try:
        # 在 WSL2 中执行 nvidia-smi 检查进程列表
        result = subprocess.check_output(["nvidia-smi"], encoding='utf-8')
        # 寻找 manticore 或 php (Buddy 进程)
        if "manticore" in result.lower() or "php" in result.lower():
            return True
        return False
    except:
        return False

def setup_and_test_gpu():
    conn = None
    try:
        conn = mysql.connector.connect(**config)
        cur = conn.cursor()

        print("1. 清理旧表...")
        cur.execute("DROP TABLE IF EXISTS products_rt")

        # 注意：MODEL_NAME 后的逗号和 USE_GPU=1 是开启加速的关键

        create_sql = """
        CREATE TABLE products_rt (
            link string,
            title text,
            content text,
            content_vector FLOAT_VECTOR
        )
        knn_type='hnsw'
        hnsw_similarity='cosine'
        model_name='sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2'
        from='content'
        use_gpu='1'
        """
        
        print("2. 创建 GPU 向量表 (这可能需要几秒钟下载/加载模型)...")
        cur.execute(create_sql)

        # 准备测试数据
        # GPU 在 Batch 模式下优势更明显，我们插入几条稍微长一点的文本
        test_data = [
            ('https://example.com/1', '深度学习', '这是一篇关于人工智能和 GPU 加速计算的研究文章。'),
            ('https://example.com/2', 'Manticore Search', 'Manticore Search 现在支持自动嵌入和向量搜索功能。'),
            ('https://example.com/3', 'WSL2 环境配置', '在 Windows 子系统中配置 CUDA 环境需要注意驱动路径映射。')
        ]

        insert_sql = "INSERT INTO products_rt (link, title, content) VALUES (%s, %s, %s)"

        print("3. 正在插入数据并触发 GPU 推理...")
        cur.executemany(insert_sql, test_data)
        
        # 立即检查 nvidia-smi
        if check_gpu_process():
            print("✅ 成功！检测到 Manticore 正在使用 GPU。")
        else:
            print("⚠️ 注意：数据已插入，但在 nvidia-smi 中未瞬时捕获到进程。")
            print("请手动在终端运行 'watch -n 0.1 nvidia-smi' 后再次运行此脚本。")

        # 验证向量是否生成
        cur.execute("SELECT id, link, content_vector FROM products_rt LIMIT 1")
        row = cur.fetchone()
        if row and row[2]:
            print(f"✅ 向量生成验证通过：ID {row[0]} 的向量长度为 {len(row[2])}")
        
    except mysql.connector.Error as e:
        print(f"❌ 发生异常: {e}")
    finally:
        if conn and conn.is_connected():
            cur.close()
            conn.close()
            print("连接已关闭。")

if __name__ == "__main__":
    setup_and_test_gpu()