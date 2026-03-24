@echo off
chcp 65001 > nul
echo ========================================
echo AI学习交流社区 - 启动脚本
echo ========================================
echo.

:: 检查MySQL是否在运行
echo [1/5] 检查MySQL服务...
net start | findstr /i "MySQL" > nul
if errorlevel 1 (
    echo MySQL服务未启动，正在启动...
    net start MySQL80
    timeout /t 3 /nobreak > nul
) else (
    echo MySQL服务已运行
)
echo.

:: 设置MySQL路径
set MYSQL_PATH=D:\MySQL\MySQL Server 8.4\bin\mysql.exe
if not exist "%MYSQL_PATH%" (
    echo 错误：找不到MySQL，请确认路径是否正确
    pause
    exit /b 1
)

:: 创建数据库
echo [2/5] 创建数据库...
"%MYSQL_PATH%" -u root -p123 -e "CREATE DATABASE IF NOT EXISTS ailearning_bbs DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
if errorlevel 1 (
    echo 警告：数据库可能已存在或创建失败
)
echo.

:: 导入SQL脚本（可选，表结构会在应用启动时自动创建）
:: echo [3/5] 导入表结构...
:: type "D:\Projects\Claude\ai_learning_bbs\backend\src\main\resources\sql\schema.sql" | "%MYSQL_PATH%" -u root -p123 ailearning_bbs
:: echo.

:: 启动后端
echo [3/5] 启动后端服务...
start "后端服务" cmd /k "cd /d D:\Projects\Claude\ai_learning_bbs\backend && java -jar target\ailearning-bbs-1.0.0.jar"
echo 后端正在启动... (端口: 8080)
echo.

:: 等待后端启动
echo [4/5] 等待后端启动...
timeout /t 10 /nobreak > nul

:: 启动前端
echo [5/5] 启动前端开发服务器...
start "前端服务" cmd /k "cd /d D:\Projects\Claude\ai_learning_bbs\frontend && npm run dev"
echo 前端正在启动... (端口: 5173)
echo.

echo ========================================
echo 启动完成！
echo ========================================
echo.
echo 访问地址：
echo   前端: http://localhost:5173
echo   后端: http://localhost:8080/api
echo   Swagger文档: http://localhost:8080/swagger-ui.html
echo.
echo 按任意键退出此脚本...
pause > nul