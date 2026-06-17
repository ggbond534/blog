@echo off
title Blog Dev Mode
cd /d "%~dp0"
echo.
echo ==================================
echo   Dev Mode - Auto reload
echo   Edit files then refresh browser
echo   Ctrl+C to stop
echo ==================================
echo.
start "" http://localhost:8081
mvn spring-boot:run
pause
