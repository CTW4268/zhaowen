# 新闻数据迁移脚本使用说明

## 概述

此脚本用于将原始的新闻数据迁移到新的规范化数据库结构中。它会将包含作者名、来源名和国家名的扁平化数据拆分为独立的关系表。

## 数据库结构

### 新表结构
- `country`: 国家地区表
- `news_source`: 新闻来源媒体表  
- `author`: 新闻作者表
- `news`: 新闻主表（通过外键关联上述三张表）

## 使用方法

### 基本用法
```bash
python migrate_news_data.py
```

### 指定源表名
如果你的原始数据表名不是 `old_news`，可以指定表名：
```bash
python migrate_news_data.py your_table_name
```

## 脚本功能

1. **自动检测源表**: 检查指定的源表是否存在
2. **创建示例数据**: 如果源表不存在，会创建示例数据用于测试
3. **提取唯一值**: 从源表中提取唯一的国家、作者和来源
4. **建立映射关系**: 为每个唯一的国家、作者、来源创建记录并建立ID映射
5. **数据迁移**: 将原始数据转换为使用外键的新格式并插入到新表中

## 注意事项

1. **备份数据**: 在运行迁移前，请务必备份重要数据
2. **表结构匹配**: 确保源表包含以下字段：
   - `title`, `content`, `summary`
   - `author_name`, `source_name`, `country_name`
   - `is_domestic`, `publish_time`, `view_num`, `like_num`, `collect_num`

3. **重复数据处理**: 脚本会避免重复插入相同的国家、作者和来源
4. **外键关联**: 迁移后的数据通过ID关联，而不是字符串匹配

## 自定义配置

如果需要修改数据库连接信息，请编辑脚本开头的 `db_config` 字典：

```python
db_config = {
    "host": "your_host",
    "port": "your_port", 
    "user": "your_username",
    "password": "your_password",
    "database": "your_database"
}
```

## 故障排除

- 如果遇到连接错误，请检查数据库配置和网络连接
- 如果某些数据未正确迁移，请检查源表中的字段名称是否匹配
- 查看控制台输出以了解迁移进度和可能的错误信息