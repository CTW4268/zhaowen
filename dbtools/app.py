"""
Flask + Manticore：对 content 语义向量做 KNN 检索（仅服务端自动向量化）。

查询文本由 Manticore 按向量列上的 model_name / from 等配置编码，应用只发送 SQL。

环境变量：
  MANTICORE_HOST / MANTICORE_PORT — 连接
  MANTICORE_VECTOR_FIELD — 向量列名，默认 content_vector
  MANTICORE_TABLE — 检索表名，默认 products_rt
  KNN_K — 返回近邻条数，默认 20
"""
import os
import re
import time

from flask import Flask, render_template, request

import pymysql

app = Flask(__name__)

MANTICORE_HOST = os.environ.get("MANTICORE_HOST", "127.0.0.1")
MANTICORE_PORT = int(os.environ.get("MANTICORE_PORT", "9306"))
MANTICORE_TABLE = os.environ.get("MANTICORE_TABLE", "products_rt")
MANTICORE_VECTOR_FIELD = os.environ.get("MANTICORE_VECTOR_FIELD", "content_vector")
KNN_K = int(os.environ.get("KNN_K", "20"))


def get_connection():
    return pymysql.connect(
        host=MANTICORE_HOST,
        port=MANTICORE_PORT,
        user="root",
        password="",
        charset="utf8mb4",
        cursorclass=pymysql.cursors.DictCursor,
        autocommit=True
    )


def validate_identifier(name: str) -> str:
    if not name or not re.match(r"^[a-zA-Z_][a-zA-Z0-9_]*$", name):
        raise ValueError("无效的标识符")
    return name


def normalize_query(q: str) -> str:
    q = (q or "").strip()
    if len(q) > 4000:
        q = q[:4000]
    return q


def run_knn_search(query: str) -> tuple[list, float]:
    table = validate_identifier(MANTICORE_TABLE)
    field = validate_identifier(MANTICORE_VECTOR_FIELD)
    t0 = time.perf_counter()
    conn = get_connection()
    try:
        with conn.cursor() as cur:
            sql = f"""
                SELECT title, content, keywords, link, source, knn_dist() AS d
                FROM {table}
                WHERE knn({field}, %s, %s)
                LIMIT 50
            """
            cur.execute(sql, (KNN_K, query))
            rows = cur.fetchall()
            
            # 过滤掉 content 为空的项
            filtered_rows = [
                row for row in rows 
                if row.get('content') and str(row['content']).strip()
            ]
            
            elapsed_ms = round((time.perf_counter() - t0) * 1000, 2)
            return filtered_rows, elapsed_ms
    finally:
        conn.close()


@app.route("/", methods=["GET", "POST"])
def index():
    query = ""
    rows = []
    error = None
    elapsed_ms = None

    if request.method == "POST":
        query = request.form.get("q", "").strip()
    else:
        query = request.args.get("q", "").strip()

    if query:
        safe = normalize_query(query)
        if not safe:
            error = "请输入有效检索内容。"
        else:
            try:
                rows, elapsed_ms = run_knn_search(safe)
            except Exception as e:
                error = f"连接或查询失败：{e}"

    return render_template(
        "index.html",
        query=query,
        rows=rows,
        error=error,
        elapsed_ms=elapsed_ms,
        manticore_host=MANTICORE_HOST,
        manticore_port=MANTICORE_PORT,
        table_name=MANTICORE_TABLE,
        vector_field=MANTICORE_VECTOR_FIELD,
        knn_k=KNN_K,
    )


if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=5000)
