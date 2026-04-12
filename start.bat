@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

REM 新闻中心网站一键启动脚本 (Windows)
echo =========================================
echo    新闻中心网站 - Windows 一键启动脚本
echo =========================================

REM 检查Java是否安装
where java >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: Java未安装，请先安装Java 17或更高版本
    pause
    exit /b 1
)

REM 检查Maven是否安装
where mvn >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: Maven未安装，请先安装Maven
    pause
    exit /b 1
)

REM 检查Node.js是否安装
where node >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: Node.js未安装，请先安装Node.js
    pause
    exit /b 1
)

REM 检查npm是否安装
where npm >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: npm未安装，请先安装npm
    pause
    exit /b 1
)

echo 环境检查通过！

REM 进入后端目录并启动
echo 正在启动后端服务...
cd backend\news-backend

REM 构建后端项目
echo 正在构建后端项目...
call mvn clean package -DskipTests

REM 启动后端服务
echo 正在启动后端服务...
start "News Backend" cmd /k "java -jar target\news-backend-0.0.1-SNAPSHOT.jar"

REM 等待后端服务启动
echo 等待后端服务启动...
timeout /t 10 /nobreak >nul

REM 进入前端目录并启动
echo 正在启动前端服务...
cd ..\..\frontend\news

REM 安装依赖
echo 正在安装前端依赖...
call npm install

REM 启动前端开发服务器
echo 正在启动前端开发服务器...
start "News Frontend" cmd /k "npm run dev"

echo.
echo =========================================
echo    启动完成！
echo =========================================
echo 后端服务地址: http://localhost:8080/api
echo 前端服务地址: http://localhost:5173
echo.
echo 要停止服务，请关闭对应的命令行窗口
echo 或使用 stop.bat 脚本
echo =========================================
pause
