# 快速启动指南

## 📦 前提条件

确保已安装以下软件：
- JDK 17+
- MySQL 8.0+
- Maven 3.6+

## 🚀 快速开始

### 步骤 1: 配置数据库

#### 1.1 创建数据库
```bash
mysql -u root -p
```

```sql
CREATE DATABASE news_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
EXIT;
```

#### 1.2 初始化数据（可选）
```bash
mysql -u root -p news_db < src/main/resources/init-db.sql
```

### 步骤 2: 修改配置

编辑 `src/main/resources/application-dev.yml` 文件：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/news_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root          # 改为你的数据库用户名
    password: your_password # 改为你的数据库密码
```

### 步骤 3: 编译项目

```bash
cd news-backend
mvn clean package -DskipTests
```

### 步骤 4: 启动应用

#### 方式一：使用 Maven 启动（开发环境）
```bash
mvn spring-boot:run
```

#### 方式二：使用 JAR 包启动
```bash
java -jar target/news-backend-0.0.1-SNAPSHOT.jar
```

### 步骤 5: 验证启动

看到以下日志表示启动成功：
```
Started NewsApplication in X.XXX seconds
```

访问 Swagger UI: http://localhost:8080/api/swagger-ui.html

## 📖 API 测试

### 使用 Swagger UI 测试

1. 打开浏览器访问：http://localhost:8080/api/swagger-ui.html
2. 展开任意接口，点击 "Try it out"
3. 填写参数，点击 "Execute" 执行

### 使用 Postman 测试

#### 1. 注册账户
```
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "password": "123456"
}
```

响应示例：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "testuser",
    "avatar": "",
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```

**复制返回的 token**，后续请求需要使用。

#### 2. 获取新闻列表
```
GET http://localhost:8080/api/news/domestic?page=1&size=10
```

#### 3. 获取收藏列表（需要 Token）
```
GET http://localhost:8080/api/favorites?page=1&size=10
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

## 🔧 常见问题

### 问题 1: 数据库连接失败

**错误信息**: `Communications link failure`

**解决方案**:
1. 确认 MySQL 服务已启动
2. 检查数据库用户名密码是否正确
3. 确认数据库 `news_db` 已创建

### 问题 2: 端口被占用

**错误信息**: `Port 8080 was already in use`

**解决方案**:
修改 `application.yml` 中的端口号：
```yaml
server:
  port: 8081  # 改为其他端口
```

### 问题 3: JWT Token 失效

Token 有效期为 7 天，过期后需要重新登录获取新 Token。

## 🌐 前后端联调

### 前端配置

在前端项目中配置 API 基础 URL：

```javascript
// config.js
export const API_BASE_URL = 'http://localhost:8080/api';
```

### 跨域配置

后端已配置允许跨域访问，默认允许的源：
- http://localhost:3000
- http://localhost:5173

如需添加其他源，编辑 `CorsConfig.java`。

## 📊 查看日志

应用日志输出在控制台，格式如下：
```
2024-01-01 10:00:00 [main] DEBUG com.example.news.service.NewsService - 查询新闻列表
```

如需查看文件日志，可配置 Logback。

## 🎯 下一步

1. 阅读 [API.md](API.md) 了解完整接口文档
2. 使用 Swagger UI 测试所有接口
3. 根据前端需求调整接口
4. 部署到生产环境

## 📞 技术支持

如有问题，请查看：
- Spring Boot 官方文档：https://spring.io/projects/spring-boot
- Swagger 官方文档：https://swagger.io/docs/
