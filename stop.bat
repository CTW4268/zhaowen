@echo off
chcp 65001 >nul

REM 新闻中心网站一键停止脚本 (Windows)
echo =========================================
echo    新闻中心网站 - Windows 一键停止脚本
echo =========================================

REM 查找并停止后端进程
echo 正在查找后端服务进程...
for /f "tokens=2" %%i in ('tasklist ^| findstr "java.exe"') do (
    echo 正在停止后端进程: %%i
    taskkill /F /PID %%i >nul 2>&1
)

REM 查找并停止前端进程
echo 正在查找前端服务进程...
for /f "tokens=2" %%i in ('tasklist ^| findstr "node.exe"') do (
    echo 正在停止前端进程: %%i
    taskkill /F /PID %%i >nul 2>&1
)

echo =========================================
echo    所有服务已停止
echo =========================================
pause
