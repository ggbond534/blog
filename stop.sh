#!/bin/bash
# 青栈博客 - 停止脚本

cd "$(dirname "$0")"

if [ -f app.pid ]; then
    PID=$(cat app.pid)
    if kill -0 $PID 2>/dev/null; then
        kill $PID
        echo "🛑 青栈博客已停止 (PID: $PID)"
    else
        echo "⚠️  进程 $PID 已不存在"
    fi
    rm -f app.pid
else
    echo "⚠️  未找到运行中的进程"
fi
read -p "按回车键退出..."
