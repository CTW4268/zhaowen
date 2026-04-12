#!/bin/bash

# 新闻中心网站一键启动脚本 (Linux)
echo "========================================="
echo "   新闻中心网站 - Linux 一键启动脚本"
echo "========================================="

# 检查Java是否安装
if ! command -v java &> /dev/null; then
    echo "错误: Java未安装，请先安装Java 17或更高版本"
    exit 1
fi

# 检查Maven是否安装
if ! command -v mvn &> /dev/null; then
    echo "错误: Maven未安装，请先安装Maven"
    exit 1
fi

# 检查Node.js是否安装
if ! command -v node &> /dev/null; then
    echo "错误: Node.js未安装，请先安装Node.js"
    exit 1
fi

# 检查npm是否安装
if ! command -v npm &> /dev/null; then
    echo "错误: npm未安装，请先安装npm"
    exit 1
fi

echo "环境检查通过！"

# 进入后端目录并启动
echo "正在启动后端服务..."
cd backend/news-backend

# 构建后端项目
echo "正在构建后端项目..."
mvn clean package -DskipTests

# 启动后端服务（后台运行）
echo "正在启动后端服务..."
nohup java -jar target/news-backend-0.0.1-SNAPSHOT.jar > ../backend.log 2>&1 &
BACKEND_PID=$!
echo "后端服务已启动，PID: $BACKEND_PID"

# 等待后端服务启动
echo "等待后端服务启动..."
sleep 10

# 进入前端目录并启动
echo "正在启动前端服务..."
cd ../../frontend/news

# 安装依赖
echo "正在安装前端依赖..."
npm install

# 启动前端开发服务器
echo "正在启动前端开发服务器..."
npm run dev &
FRONTEND_PID=$!
echo "前端服务已启动，PID: $FRONTEND_PID"

echo ""
echo "========================================="
echo "   启动完成！"
echo "========================================="
echo "后端服务地址: http://localhost:8080/api"
echo "前端服务地址: http://localhost:5173"
echo "后端日志文件: backend/backend.log"
echo ""
echo "要停止服务，请运行:"
echo "  kill $BACKEND_PID $FRONTEND_PID"
echo "或使用 stop.sh 脚本"
echo "========================================="
