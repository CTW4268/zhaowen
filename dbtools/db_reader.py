#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
数据库读取工具 - 用于读取和验证 news-system 数据库
支持查询统计、数据验证和导出功能
"""

import mysql.connector
from mysql.connector import errorcode
import json
from datetime import datetime, date
from tabulate import tabulate
import sys


# 数据库配置
DB_CONFIG = {
    "host": "101.35.241.235",
    "port": 53306,
    "user": "root",
    "password": "Cc20051215",
    "database": "news-system"
}


class DatabaseReader:
    """数据库读取器"""
    
    def __init__(self):
        self.conn = None
        self.cursor = None
    
    def connect(self):
        """连接数据库"""
        try:
            self.conn = mysql.connector.connect(**DB_CONFIG)
            self.cursor = self.conn.cursor(dictionary=True)
            print("✅ 数据库连接成功")
            return True
        except mysql.connector.Error as err:
            print(f"❌ 数据库连接失败: {err}")
            return False
    
    def disconnect(self):
        """断开数据库连接"""
        if self.cursor:
            self.cursor.close()
        if self.conn and self.conn.is_connected():
            self.conn.close()
            print("✅ 数据库连接已关闭")
    
    def get_table_stats(self):
        """获取所有表的统计信息"""
        print("\n" + "=" * 60)
        print("数据库表统计信息")
        print("=" * 60)
        
        tables = ['country', 'author', 'news_source', 'news', 'user', 'user_collect']
        
        for table in tables:
            try:
                self.cursor.execute(f"SELECT COUNT(*) as count FROM {table}")
                result = self.cursor.fetchone()
                count = result['count'] if result else 0
                print(f"  {table:20s}: {count:>8,} 条记录")
            except Exception as e:
                print(f"  {table:20s}: 错误 - {e}")
    
    def get_news_summary(self, limit=10):
        """获取新闻摘要（包含关联信息）"""
        print("\n" + "=" * 60)
        print(f"新闻数据摘要 (前{limit}条)")
        print("=" * 60)
        
        query = """
            SELECT 
                n.id,
                n.title,
                a.author_name,
                s.source_name,
                c.country_name,
                n.is_domestic,
                n.publish_time,
                n.view_num,
                n.like_num,
                n.collect_num
            FROM news n
            LEFT JOIN author a ON n.author_id = a.id
            LEFT JOIN news_source s ON n.source_id = s.id
            LEFT JOIN country c ON n.country_id = c.id
            ORDER BY n.publish_time DESC
            LIMIT %s
        """
        
        self.cursor.execute(query, (limit,))
        rows = self.cursor.fetchall()
        
        if not rows:
            print("  没有找到新闻数据")
            return
        
        # 格式化输出
        data = []
        for row in rows:
            data.append([
                row['id'],
                row['title'][:40] + "..." if len(row['title']) > 40 else row['title'],
                row['author_name'] or "未知",
                row['source_name'] or "未知",
                row['country_name'] or "未知",
                "国内" if row['is_domestic'] == 1 else "海外",
                str(row['publish_time'])[:19] if row['publish_time'] else "未知",
                row['view_num'],
                row['like_num'],
                row['collect_num']
            ])
        
        headers = ["ID", "标题", "作者", "来源", "国家", "类型", "发布时间", "阅读", "点赞", "收藏"]
        print(tabulate(data, headers=headers, tablefmt="grid"))
    
    def get_country_distribution(self):
        """获取国家分布统计"""
        print("\n" + "=" * 60)
        print("国家分布统计")
        print("=" * 60)
        
        query = """
            SELECT 
                c.country_name,
                c.continent,
                COUNT(n.id) as news_count
            FROM country c
            LEFT JOIN news n ON c.id = n.country_id
            GROUP BY c.id, c.country_name, c.continent
            ORDER BY news_count DESC
        """
        
        self.cursor.execute(query)
        rows = self.cursor.fetchall()
        
        if not rows:
            print("  没有数据")
            return
        
        data = [[row['country_name'], row['continent'] or "未知", row['news_count']] for row in rows]
        headers = ["国家", "大洲", "新闻数量"]
        print(tabulate(data, headers=headers, tablefmt="grid"))
    
    def get_source_distribution(self):
        """获取来源分布统计"""
        print("\n" + "=" * 60)
        print("新闻来源分布")
        print("=" * 60)
        
        query = """
            SELECT 
                s.source_name,
                COUNT(n.id) as news_count,
                AVG(n.view_num) as avg_views
            FROM news_source s
            LEFT JOIN news n ON s.id = n.source_id
            GROUP BY s.id, s.source_name
            ORDER BY news_count DESC
        """
        
        self.cursor.execute(query)
        rows = self.cursor.fetchall()
        
        if not rows:
            print("  没有数据")
            return
        
        data = [[row['source_name'], row['news_count'], f"{row['avg_views']:.0f}" if row['avg_views'] else 0] for row in rows]
        headers = ["来源", "新闻数量", "平均阅读量"]
        print(tabulate(data, headers=headers, tablefmt="grid"))
    
    def check_data_integrity(self):
        """检查数据完整性"""
        print("\n" + "=" * 60)
        print("数据完整性检查")
        print("=" * 60)
        
        checks = [
            ("新闻无作者", "SELECT COUNT(*) as count FROM news WHERE author_id IS NULL"),
            ("新闻无来源", "SELECT COUNT(*) as count FROM news WHERE source_id IS NULL"),
            ("新闻无国家", "SELECT COUNT(*) as count FROM news WHERE country_id IS NULL"),
            ("孤立作者记录", "SELECT COUNT(*) as count FROM author a LEFT JOIN news n ON a.id = n.author_id WHERE n.id IS NULL"),
            ("孤立来源记录", "SELECT COUNT(*) as count FROM news_source s LEFT JOIN news n ON s.id = n.source_id WHERE n.id IS NULL"),
            ("孤立国家记录", "SELECT COUNT(*) as count FROM country c LEFT JOIN news n ON c.id = n.country_id WHERE n.id IS NULL"),
        ]
        
        issues = []
        for name, query in checks:
            self.cursor.execute(query)
            result = self.cursor.fetchone()
            count = result['count'] if result else 0
            status = "⚠️" if count > 0 else "✅"
            print(f"  {status} {name}: {count}")
            if count > 0:
                issues.append((name, count))
        
        if issues:
            print(f"\n  发现 {len(issues)} 个数据完整性问题")
        else:
            print("\n  ✅ 数据完整性良好")
    
    def export_to_json(self, output_file="news_export.json"):
        """导出新闻数据为JSON"""
        print(f"\n正在导出数据到 {output_file}...")
        
        query = """
            SELECT 
                n.*,
                a.author_name,
                s.source_name,
                c.country_name,
                c.continent
            FROM news n
            LEFT JOIN author a ON n.author_id = a.id
            LEFT JOIN news_source s ON n.source_id = s.id
            LEFT JOIN country c ON n.country_id = c.id
            ORDER BY n.publish_time DESC
        """
        
        self.cursor.execute(query)
        rows = self.cursor.fetchall()
        
        # 转换日期时间为字符串
        for row in rows:
            for key, value in row.items():
                if isinstance(value, (datetime, date)):
                    row[key] = value.isoformat()
        
        with open(output_file, 'w', encoding='utf-8') as f:
            json.dump(rows, f, ensure_ascii=False, indent=2)
        
        print(f"✅ 成功导出 {len(rows)} 条记录到 {output_file}")
    
    def run_custom_query(self, query):
        """执行自定义查询"""
        try:
            self.cursor.execute(query)
            rows = self.cursor.fetchall()
            
            if not rows:
                print("  查询结果为空")
                return
            
            # 显示结果
            headers = list(rows[0].keys())
            data = [list(row.values()) for row in rows]
            print(tabulate(data, headers=headers, tablefmt="grid"))
            print(f"\n共 {len(rows)} 条记录")
            
        except Exception as e:
            print(f"❌ 查询执行失败: {e}")


def main():
    """主函数"""
    print("=" * 60)
    print("数据库读取工具 - news-system")
    print("=" * 60)
    
    reader = DatabaseReader()
    
    if not reader.connect():
        sys.exit(1)
    
    try:
        # 显示菜单
        while True:
            print("\n请选择操作:")
            print("  1. 查看表统计信息")
            print("  2. 查看新闻摘要")
            print("  3. 查看国家分布")
            print("  4. 查看来源分布")
            print("  5. 检查数据完整性")
            print("  6. 导出为JSON")
            print("  7. 执行自定义SQL")
            print("  0. 退出")
            
            choice = input("\n请输入选项 (0-7): ").strip()
            
            if choice == '1':
                reader.get_table_stats()
            elif choice == '2':
                limit = input("  显示条数 (默认10): ").strip()
                limit = int(limit) if limit.isdigit() else 10
                reader.get_news_summary(limit)
            elif choice == '3':
                reader.get_country_distribution()
            elif choice == '4':
                reader.get_source_distribution()
            elif choice == '5':
                reader.check_data_integrity()
            elif choice == '6':
                filename = input("  输出文件名 (默认news_export.json): ").strip()
                filename = filename if filename else "news_export.json"
                reader.export_to_json(filename)
            elif choice == '7':
                sql = input("  输入SQL查询: ").strip()
                if sql:
                    reader.run_custom_query(sql)
            elif choice == '0':
                break
            else:
                print("  无效选项，请重新选择")
    
    except KeyboardInterrupt:
        print("\n\n程序被用户中断")
    finally:
        reader.disconnect()


if __name__ == "__main__":
    main()
