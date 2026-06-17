@echo off
echo Stopping Blog Server...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":8081" ^| findstr "LISTENING"') do (
    taskkill /f /pid %%a >nul 2>&1
)
echo Done.
pause
