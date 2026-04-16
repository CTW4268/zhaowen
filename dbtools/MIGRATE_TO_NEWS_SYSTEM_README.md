# 数据库迁移脚本使用说明

## 概述

此脚本用于将 `news` 数据库（旧扁平化结构）中的数据迁移到 `news-system` 数据库（新规范化结构）。

## 功能特点

1. **自动创建目标表结构**：如果目标数据库中不存在必要的表，会自动创建
2. **智能数据映射**：自动提取国家、作者、来源并建立关联关系
3. **去重处理**：避免重复迁移相同的数据
4. **进度显示**：实时显示迁移进度和统计信息

## 数据库结构对比

### 源数据库 (news_db) - 扁平化结构
```
news 表:
- id, title, content, summary
- type (DOMESTIC/OVERSEAS)
- province, region, country, category
- source, author
- view_count, is_carousel, carousel_order
- published_at, created_at
```

### 目标数据库 (news-system) - 规范化结构
```
country 表: id, country_name, continent, weight, status
author 表: id, author_name, country_id, status
news_source 表: id, source_name, weight, status
news 表: id, title, content, summary, 
        author_id, source_id, country_id,
        is_domestic, publish_time, 
        view_num, like_num, collect_num, final_weight
```

## 前置条件

1. Python 3.6+
2. 安装 mysql-connector-python:
   ```bash
   pip install mysql-connector-python
   ```

3. 确保两个数据库都可以访问：
   - 源数据库: `news_db`
   - 目标数据库: `news-system`

## 使用方法

### 基本用法

```bash
cd d:\Desktop\zhaowen\dbtools
python migrate_to_news_system.py
```

### 在 Windows PowerShell 中

```powershell
cd "d:\Desktop\zhaowen\dbtools"
python migrate_to_news_system.py
```

### 在 CMD 中

```cmd
cd "d:\Desktop\zhaowen\dbtools"
python migrate_to_news_system.py
```

## 配置说明

如果需要修改数据库连接信息，请编辑脚本开头的配置部分：

```python
# 源数据库配置 (news_db)
source_db_config = {
    "host": "101.35.241.235",
    "port": 53306,
    "user": "root",
    "password": "Cc20051215",
    "database": "news_db"
}

# 目标数据库配置 (news-system)
target_db_config = {
    "host": "101.35.241.235",
    "port": 53306,
    "user": "root",
    "password": "Cc20051215",
    "database": "news-system"
}
```

## 迁移步骤

脚本会自动执行以下步骤：

1. **连接数据库**：分别连接源数据库和目标数据库
2. **创建表结构**：在目标数据库中创建必要的表（如果不存在）
3. **迁移国家数据**：提取所有唯一的国家名称并创建记录
4. **迁移作者数据**：提取所有唯一的作者并建立与国家的关联
5. **迁移来源数据**：提取所有唯一的新闻来源
6. **迁移新闻数据**：将新闻数据转换并插入到新结构中

## 输出示例

```
============================================================
数据库迁移工具: news_db -> news-system
============================================================

连接源数据库 (news_db)...
连接目标数据库 (news-system)...
检查/创建目标表结构...
目标表结构检查/创建完成
源数据库中共有 100 条新闻数据

[1/4] 迁移国家数据...
  处理了 15 个国家

[2/4] 迁移作者数据...
  处理了 25 个作者

[3/4] 迁移新闻来源数据...
  处理了 10 个新闻来源

[4/4] 迁移新闻数据...
  已迁移 100 条...

迁移统计:
  成功迁移: 100 条
  跳过重复: 0 条
  源数据总数: 100 条

数据迁移完成！

✅ 数据迁移成功完成！
```

## 注意事项

1. **备份数据**：在运行迁移前，建议先备份目标数据库
2. **网络连接**：确保可以连接到远程MySQL服务器
3. **权限要求**：数据库用户需要有读写权限
4. **重复执行**：脚本支持重复执行，会自动跳过已存在的数据

## 故障排除

### 问题1: ModuleNotFoundError: No module named 'mysql.connector'

**解决方案**：
```bash
pip install mysql-connector-python
```

### 问题2: 连接超时或无法连接

**解决方案**：
- 检查网络连接
- 确认MySQL服务器地址和端口正确
- 检查防火墙设置

### 问题3: Access denied

**解决方案**：
- 检查用户名和密码是否正确
- 确认用户有相应的数据库权限

### 问题4: 数据库不存在

**解决方案**：
- 先在MySQL中创建目标数据库：
```sql
CREATE DATABASE `news-system` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

## 迁移后验证

迁移完成后，可以在目标数据库中执行以下SQL验证数据：

```sql
-- 查看统计表数量
SELECT COUNT(*) FROM news;
SELECT COUNT(*) FROM country;
SELECT COUNT(*) FROM author;
SELECT COUNT(*) FROM news_source;

-- 查看新闻详情
SELECT n.title, a.author_name, s.source_name, c.country_name
FROM news n
LEFT JOIN author a ON n.author_id = a.id
LEFT JOIN news_source s ON n.source_id = s.id
LEFT JOIN country c ON n.country_id = c.id
LIMIT 10;
```
