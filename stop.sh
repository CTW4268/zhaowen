#!/bin/bash

# 新闻中心网站一键停止脚本 (Linux)
echo "========================================="
echo "   新闻中心网站 - Linux 一键停止脚本"
echo "========================================="

# 查找并停止后端进程
BACKEND_PIDS=$(ps aux | grep 'news-backend-0.0.1-SNAPSHOT.jar' | grep -v grep | awk '{print $2}')
if [ -n "$BACKEND_PIDS" ]; then
    echo "正在停止后端服务..."
    kill $BACKEND_PIDS
    echo "后端服务已停止"
else
    echo "未找到运行中的后端服务"
fi

# 查找并停止前端进程
FRONTEND_PIDS=$(ps aux | grep 'npm run dev' | grep -v grep | awk '{print $2}')
if [ -n "$FRONTEND_PIDS" ]; then
    echo "正在停止前端服务..."
    kill $FRONTEND_PIDS
    echo "前端服务已停止"
else
    echo "未找到运行中的前端服务"
fi

echo "========================================="
echo "   所有服务已停止"
echo "========================================="
