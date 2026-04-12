import mysql.connector

# --- 配置区 ---
MANTICORE_CONFIG = {
    "host": "127.0.0.1",
    "port": 9306,
    "autocommit": True
}

# 连接 Manticore
db_manticore = mysql.connector.connect(**MANTICORE_CONFIG)
cur_mc = db_manticore.cursor()

def delete_empty_content(table_name="products_rt"):
    """删除 content 字段为空的所有行"""
    print(f"正在检查表 {table_name} 中的数据...")
    
    # 先统计总数
    cur_mc.execute(f"SELECT COUNT(*) FROM {table_name}")
    total_count = cur_mc.fetchone()[0]
    print(f"当前总记录数：{total_count}")
    
    # 统计 content 为空的记录数（Manticore 不支持 TRIM，只检查 NULL 和空字符串）
    cur_mc.execute(f"""
        SELECT COUNT(*) FROM {table_name} 
        WHERE content IS NULL OR content = ''
    """)
    empty_count = cur_mc.fetchone()[0]
    print(f"content 为空的记录数：{empty_count}")
    
    if empty_count == 0:
        print("✅ 没有需要删除的空数据。")
        return
    
    # 确认删除
    confirm = input(f"\n⚠️  确定要删除 {empty_count} 条空数据吗？(yes/no): ")
    if confirm.lower() != 'yes':
        print("已取消删除操作。")
        return
    
    # 执行删除
    print("开始删除空数据...")
    cur_mc.execute(f"""
        DELETE FROM {table_name} 
        WHERE content IS NULL OR content = ''
    """)
    
    # 提交事务
    db_manticore.commit()
    
    # 验证结果
    cur_mc.execute(f"SELECT COUNT(*) FROM {table_name}")
    remaining_count = cur_mc.fetchone()[0]
    print(f"✅ 删除完成！剩余记录数：{remaining_count}")
    print(f"共删除 {empty_count} 条记录。")

if __name__ == "__main__":
    try:
        delete_empty_content("products_rt")
    except Exception as e:
        print(f"❌ 发生错误：{e}")
        import traceback
        traceback.print_exc()
    finally:
        cur_mc.close()
        db_manticore.close()