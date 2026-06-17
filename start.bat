@echo off
title Blog Server
cd /d "%~dp0"

echo.
echo ================================
echo   Blog Server - Starting...
echo ================================
echo.

REM Build jar if not exists
if not exist "target\blog-1.0.0.jar" (
    echo [1/2] First run, building jar...
    call mvn package -DskipTests -q
    echo [1/2] Build done.
    echo.
)

echo [2/2] Starting server on port 8081...
echo       Open: http://localhost:8081
echo.

start "" http://localhost:8081
java -jar target\blog-1.0.0.jar

pause
