# 新闻中心网站

一个基于Spring Boot和Vue.js的现代化新闻管理系统，提供新闻浏览、用户管理、收藏、阅读历史等功能。

## 📋 项目简介

本项目是一个全栈新闻管理系统，包含前端和后端两部分：
- **后端**: 基于Spring Boot 3.2 + Spring Security + JWT + MySQL
- **前端**: 基于Vue 3 + Vite + TypeScript + Pinia

## 🚀 快速开始

### 环境要求

**后端:**
- Java 17+
- Maven 3.6+
- MySQL 8.0+

**前端:**
- Node.js 20.19+ 或 22.12+
- npm 或 yarn

### 一键启动

#### Linux/Mac 用户

```bash
# 赋予执行权限
chmod +x start.sh stop.sh

# 启动服务
./start.sh

# 停止服务
./stop.sh
```

#### Windows 用户

双击运行以下脚本：
- `start.bat` - 启动服务
- `stop.bat` - 停止服务

或在命令行中运行：
```cmd
start.bat
stop.bat
```

### 手动启动

#### 后端启动

```bash
cd backend/news-backend
mvn clean package -DskipTests
java -jar target/news-backend-0.0.1-SNAPSHOT.jar
```

后端服务将在 `http://localhost:8080/api` 运行

#### 前端启动

```bash
cd frontend/news
npm install
npm run dev
```

前端服务将在 `http://localhost:5173` 运行

## 📁 项目结构

```
zhaowen/
├── backend/                # 后端代码
│   └── news-backend/      # Spring Boot后端项目
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/com/example/news/
│       │   │   │   ├── config/        # 配置类
│       │   │   │   ├── controller/    # 控制器
│       │   │   │   ├── dto/           # 数据传输对象
│       │   │   │   ├── entity/        # 实体类
│       │   │   │   ├── exception/     # 异常处理
│       │   │   │   ├── repository/    # 数据访问层
│       │   │   │   ├── security/      # 安全配置
│       │   │   │   └── service/       # 业务逻辑层
│       │   │   └── resources/         # 配置文件
│       │   └── test/                  # 测试代码
│       └── pom.xml                    # Maven配置
├── frontend/              # 前端代码
│   └── news/             # Vue前端项目
│       ├── src/
│       │   ├── assets/   # 静态资源
│       │   ├── components/  # 组件
│       │   ├── composables/ # 组合式函数
│       │   ├── router/   # 路由配置
│       │   ├── stores/   # 状态管理
│       │   └── views/    # 页面视图
│       └── package.json  # npm配置
├── start.sh              # Linux启动脚本
├── stop.sh               # Linux停止脚本
├── start.bat             # Windows启动脚本
├── stop.bat              # Windows停止脚本
└── README.md             # 项目说明文档
```

## 🔧 配置说明

### 数据库配置

编辑 `backend/news-backend/src/main/resources/application-dev.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/news_db?useSSL=false&serverTimezone=UTC&characterEncoding=utf8
    username: your_username
    password: your_password
```

### JWT配置

编辑 `backend/news-backend/src/main/resources/application.yml`:

```yaml
jwt:
  secret: your-secret-key-change-this-in-production-minimum-32-chars
  expiration: 3600000  # access token过期时间（1小时）
  refresh-expiration: 604800000  # refresh token过期时间（7天）
```

## 📖 API文档

启动后端服务后，访问 Swagger UI:
```
http://localhost:8080/api/swagger-ui.html
```

## 🎯 主要功能

- ✅ 用户注册/登录（JWT认证）
- ✅ 新闻浏览（国内/国际分类）
- ✅ 新闻收藏
- ✅ 阅读历史记录
- ✅ 用户反馈
- ✅ 帮助中心
- ✅ 响应式设计

## 🛠️ 技术栈

### 后端
- Spring Boot 3.2
- Spring Security
- Spring Data JPA
- JWT (io.jsonwebtoken)
- MySQL
- Lombok
- MapStruct
- Swagger/OpenAPI 3.0

### 前端
- Vue 3
- Vite
- TypeScript
- Vue Router
- Pinia
- ESLint + Prettier

## 📝 开发指南

### 后端开发

```bash
cd backend/news-backend
mvn spring-boot:run  # 开发模式运行
```

### 前端开发

```bash
cd frontend/news
npm run dev          # 开发服务器
npm run build        # 生产构建
npm run lint         # 代码检查
npm run format       # 代码格式化
```
