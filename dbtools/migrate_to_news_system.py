#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
数据库迁移脚本：从 news 迁移到 news-system

源表结构 (news) - 实际字段:
  id, link, link_md5, title, content, publish_time, source, keywords, fetch_time

目标结构 (news-system):
  country 表: 从 source 推断国家
  news_source 表: 从 source 字段提取
  news 表: 规范化后的数据
"""

import mysql.connector
import sys

SOURCE_DB = {
    "host": "101.35.241.235", "port": 53306,
    "user": "root", "password": "Cc20051215", "database": "news"
}

TARGET_DB = {
    "host": "101.35.241.235", "port": 53306,
    "user": "root", "password": "Cc20051215", "database": "news-system"
}

BATCH = 100


def create_tables(c):
    """创建目标表（先删除旧表）"""
    print("重建目标表结构...")

    # 先删除依赖表（外键关系：news -> author/country/news_source）
    c.execute("DROP TABLE IF EXISTS `news`")
    c.execute("DROP TABLE IF EXISTS `author`")
    c.execute("DROP TABLE IF EXISTS `country`")
    c.execute("DROP TABLE IF EXISTS `news_source`")

    c.execute("""CREATE TABLE `country` (
        `id` INT PRIMARY KEY AUTO_INCREMENT,
        `country_name` VARCHAR(100) NOT NULL UNIQUE,
        `continent` VARCHAR(50), `weight` INT DEFAULT 1,
        `status` TINYINT DEFAULT 1,
        `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        INDEX `idx_country_name` (`country_name`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4""")

    c.execute("""CREATE TABLE `news_source` (
        `id` INT PRIMARY KEY AUTO_INCREMENT,
        `source_name` VARCHAR(100) NOT NULL UNIQUE,
        `weight` INT DEFAULT 3, `status` TINYINT DEFAULT 1,
        `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        INDEX `idx_source_name` (`source_name`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4""")

    c.execute("""CREATE TABLE `author` (
        `id` INT PRIMARY KEY AUTO_INCREMENT,
        `author_name` VARCHAR(100) NOT NULL UNIQUE,
        `country_id` INT, `status` TINYINT DEFAULT 1,
        `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (`country_id`) REFERENCES `country`(`id`) ON DELETE SET NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4""")

    c.execute("""CREATE TABLE `news` (
        `id` INT PRIMARY KEY AUTO_INCREMENT,
        `title` VARCHAR(255) NOT NULL, `content` TEXT, `summary` VARCHAR(500),
        `author_id` INT, `source_id` INT, `country_id` INT,
        `is_domestic` TINYINT DEFAULT 0, `publish_time` DATETIME,
        `view_num` INT DEFAULT 0, `like_num` INT DEFAULT 0, `collect_num` INT DEFAULT 0,
        `final_weight` DECIMAL(10,2) DEFAULT 0.00,
        `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (`author_id`) REFERENCES `author`(`id`) ON DELETE SET NULL,
        FOREIGN KEY (`source_id`) REFERENCES `news_source`(`id`) ON DELETE SET NULL,
        FOREIGN KEY (`country_id`) REFERENCES `country`(`id`) ON DELETE SET NULL,
        INDEX `idx_publish_time` (`publish_time`),
        INDEX `idx_is_domestic` (`is_domestic`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4""")

    print("✅ 表结构已重建\n")


def get_country(source):
    """从来源推断国家"""
    if not source:
        return None
    m = {
        '中国': ['新华', '人民', '央视', '中新', '新浪', '搜狐', '网易', '腾讯', '凤凰', '环球', '参考', '光明'],
        '日本': ['共同社', '朝日', '读卖', '每日新闻'],
        '英国': ['BBC', '卫报', '泰晤士', '路透'],
        '美国': ['CNN', '纽约时报', '华盛顿', '华尔街', '美联社'],
        '法国': ['法新社'],
    }
    for country, kws in m.items():
        for kw in kws:
            if kw in source:
                return country
    return None


CONTINENT = {'中国': '亚洲', '日本': '亚洲', '英国': '欧洲', '法国': '欧洲', '美国': '美洲'}


def migrate():
    src = tgt = None
    try:
        print("=" * 50)
        print("迁移: news -> news-system")
        print("=" * 50 + "\n")

        src = mysql.connector.connect(**SOURCE_DB)
        sc = src.cursor(dictionary=True)
        print("✅ 源库连接成功")

        tgt = mysql.connector.connect(**TARGET_DB)
        tc = tgt.cursor(dictionary=True)
        print("✅ 目标库连接成功\n")

        create_tables(tc)
        tgt.commit()

        sc.execute("SELECT COUNT(*) as c FROM news")
        total = sc.fetchone()['c']
        print(f"源表共 {total} 条\n")
        if total == 0:
            print("无数据")
            return True

        # 1. 提取来源 -> news_source + country
        print("[1/3] 提取来源...")
        sc.execute("SELECT DISTINCT source FROM news WHERE source IS NOT NULL AND source != ''")
        sources = [r['source'] for r in sc.fetchall()]

        src_map = {}
        cty_map = {}

        for s in sources:
            tc.execute("SELECT id FROM news_source WHERE source_name=%s", (s,))
            r = tc.fetchone()
            if r:
                src_map[s] = r['id']
            else:
                tc.execute("INSERT INTO news_source(source_name,weight,status) VALUES(%s,%s,%s)", (s, 3, 1))
                src_map[s] = tc.lastrowid

            country = get_country(s)
            if country and country not in cty_map:
                tc.execute("SELECT id FROM country WHERE country_name=%s", (country,))
                r = tc.fetchone()
                if r:
                    cty_map[country] = r['id']
                else:
                    tc.execute("INSERT INTO country(country_name,continent,weight,status) VALUES(%s,%s,%s,%s)",
                               (country, CONTINENT.get(country, '未知'), 1, 1))
                    cty_map[country] = tc.lastrowid

        print(f"  {len(src_map)} 个来源, {len(cty_map)} 个国家\n")

        # 2. 迁移新闻
        print("[2/3] 迁移新闻...")
        sc.execute("SELECT id, title, content, publish_time, source, keywords FROM news ORDER BY id")
        rows = sc.fetchall()

        done = skip = 0
        buf = []

        for r in rows:
            tc.execute("SELECT id FROM news WHERE title=%s", (r['title'],))
            if tc.fetchone():
                skip += 1
                continue

            sid = src_map.get(r.get('source'))
            cid = None
            dom = 0
            if r.get('source'):
                c = get_country(r['source'])
                if c:
                    cid = cty_map.get(c)
                    dom = 1 if c == '中国' else 0

            buf.append((
                r['title'], r.get('content'),
                (r.get('keywords') or '')[:500] or None,  # keywords -> summary
                None, sid, cid, dom,
                r.get('publish_time'), 0, 0, 0, 0.0
            ))

            if len(buf) >= BATCH:
                tc.executemany(
                    "INSERT INTO news(title,content,summary,author_id,source_id,country_id,"
                    "is_domestic,publish_time,view_num,like_num,collect_num,final_weight) "
                    "VALUES(%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)", buf)
                done += len(buf)
                buf = []
                tgt.commit()
                print(f"  {done + skip}/{total}")

        if buf:
            tc.executemany(
                "INSERT INTO news(title,content,summary,author_id,source_id,country_id,"
                "is_domestic,publish_time,view_num,like_num,collect_num,final_weight) "
                "VALUES(%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)", buf)
            done += len(buf)
            tgt.commit()

        # 3. 统计
        print("\n[3/3] 统计...")
        tc.execute("SELECT COUNT(*) as c FROM news")
        print(f"\n  迁移: {done}, 跳过: {skip}, 目标库: {tc.fetchone()['c']}")

        sc.close(); src.close()
        tc.close(); tgt.close()
        print("\n✅ 完成")
        return True

    except Exception as e:
        print(f"\n❌ {e}")
        import traceback; traceback.print_exc()
        if src: src.rollback()
        if tgt: tgt.rollback()
        return False


if __name__ == "__main__":
    sys.exit(0 if migrate() else 1)
                                                        