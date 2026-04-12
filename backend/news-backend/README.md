# 新闻中心网站后端系统

基于 Spring Boot 3.x 的新闻网站后端 API 服务，为 Vue 3 前端提供完整的后端支持。

## 📋 技术栈

- **框架**: Spring Boot 3.2.0
- **数据库**: MySQL 8.0+
- **ORM**: Spring Data JPA
- **安全认证**: JWT + Spring Security
- **API 文档**: Swagger/OpenAPI 3.0
- **构建工具**: Maven

## 🚀 快速开始

### 1. 环境要求

- JDK 17+
- MySQL 8.0+
- Maven 3.6+

### 2. 数据库配置

#### 2.1 创建数据库
```bash
# 方式一：使用提供的初始化脚本
mysql -u root -p < src/main/resources/init-db.sql

# 方式二：手动创建
mysql -u root -p
> CREATE DATABASE news_db DEFAULT CHARACTER SET utf8mb4;
```

#### 2.2 修改配置文件
编辑 `src/main/resources/application-dev.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/news_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root          # 修改为你的数据库用户名
    password: your_password # 修改为你的数据库密码
```

### 3. 启动应用

```bash
# 开发环境启动
mvn spring-boot:run

# 或者打包后运行
mvn clean package
java -jar target/news-backend-0.0.1-SNAPSHOT.jar
```

### 4. 访问应用

- **API 基础路径**: `http://localhost:8080/api`
- **Swagger UI**: `http://localhost:8080/api/swagger-ui.html`
- **API 文档**: `http://localhost:8080/api/v3/api-docs`

## 📖 API 接口说明

### 用户认证模块

| 接口 | 方法 | 描述 | 认证 |
|------|------|------|------|
| `/auth/register` | POST | 用户注册 | ❌ |
| `/auth/login` | POST | 用户登录 | ❌ |

### 新闻内容模块

| 接口 | 方法 | 描述 | 认证 |
|------|------|------|------|
| `/news/carousel` | GET | 获取轮播图新闻 | ❌ |
| `/news/recommend` | GET | 获取推荐新闻 | ❌ |
| `/news/domestic` | GET | 获取国内新闻 | ❌ |
| `/news/overseas` | GET | 获取海外新闻 | ❌ |
| `/news/politics` | GET | 获取政治新闻 | ❌ |
| `/news/{id}` | GET | 获取新闻详情 | ❌ |

### 收藏管理模块

| 接口 | 方法 | 描述 | 认证 |
|------|------|------|------|
| `/favorites` | GET | 获取收藏列表 | ✅ |
| `/favorites` | POST | 添加收藏 | ✅ |
| `/favorites/{newsId}` | DELETE | 取消收藏 | ✅ |
| `/favorites/check/{newsId}` | GET | 检查收藏状态 | ✅ |

### 阅读历史模块

| 接口 | 方法 | 描述 | 认证 |
|------|------|------|------|
| `/history` | GET | 获取历史记录 | ✅ |
| `/history/clear` | DELETE | 清空历史 | ✅ |
| `/history/{recordId}` | DELETE | 删除单条历史 | ✅ |

### 反馈与帮助模块

| 接口 | 方法 | 描述 | 认证 |
|------|------|------|------|
| `/feedback` | POST | 提交反馈 | ❌ |
| `/help/faq` | GET | 获取常见问题 | ❌ |

## 🔐 认证说明

### JWT Token 使用

1. **登录获取 Token**
```json
POST /api/auth/login
{
  "username": "testuser",
  "password": "123456"
}

// 响应
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "testuser",
    "avatar": "/avatar/default.png",
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```

2. **携带 Token 访问受保护接口**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

## 📊 数据库表结构

- **users**: 用户表
- **news**: 新闻表
- **favorites**: 收藏表
- **reading_history**: 阅读历史表
- **feedbacks**: 反馈表

详细表结构请查看 `src/main/resources/init-db.sql`

## ⚙️ 配置说明

### 主要配置文件

- `application.yml`: 主配置文件
- `application-dev.yml`: 开发环境配置
- `application-prod.yml`: 生产环境配置

### 关键配置项

```yaml
# JWT 配置（生产环境务必修改密钥）
jwt:
  secret: your-secret-key-change-this-in-production-minimum-32-chars
  expiration: 604800000  # 7 天

# 服务器端口
server:
  port: 8080
```

## 🧪 测试账户

系统预置了两个测试账户：

| 用户名 | 密码 | 说明 |
|--------|------|------|
| testuser | 123456 | 普通用户 |
| admin | 123456 | 管理员 |

## 📝 开发指南

### 添加新的 API 接口

1. 在对应的 Controller 中添加方法
2. 在 Service 层实现业务逻辑
3. 使用 Swagger 注解添加文档
4. 测试接口功能

### 统一响应格式

所有接口返回统一格式：
```json
{
  "code": 200,      // 状态码
  "message": "success",  // 消息
  "data": {}        // 数据
}
```

## 🛠️ 常见问题

### 1. 启动时报数据库连接错误
- 检查 MySQL 是否已启动
- 确认数据库用户名密码正确
- 确认数据库 `news_db` 已创建

### 2. 跨域问题
已在 `CorsConfig.java` 中配置允许跨域，如需修改允许的源，请编辑该配置文件。

### 3. Token 失效
Token 有效期为 7 天，过期后需要重新登录。

## 📦 部署

### 生产环境部署

1. 修改 `application-prod.yml` 中的数据库配置
2. 设置环境变量（可选）：
   ```bash
   export DB_HOST=your-db-host
   export DB_PORT=3306
   export DB_USERNAME=root
   export DB_PASSWORD=your-password
   ```
3. 打包并运行：
   ```bash
   mvn clean package -P prod
   java -jar target/news-backend-0.0.1-SNAPSHOT.jar
   ```

## 📄 License

MIT License

## 👥 联系方式

如有问题，请通过以下方式联系：
- Email: support@example.com
- Issue: GitHub Issues
