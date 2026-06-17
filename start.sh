#!/bin/bash
# 青栈博客 - 一键启动脚本
# 用法：双击 start.sh 或在终端运行 ./start.sh

cd "$(dirname "$0")"

echo "🌊 青栈博客启动中..."
echo ""

# 检查是否已在运行
if [ -f app.pid ] && kill -0 $(cat app.pid) 2>/dev/null; then
    echo "⚠️  应用已在运行中 (PID: $(cat app.pid))"
    echo "   浏览器打开: http://localhost:8081"
    echo "   停止请运行: ./stop.sh"
    exit 0
fi

# 如果 jar 不存在，先编译
if [ ! -f target/blog-1.0.0.jar ]; then
    echo "📦 首次运行，正在编译（约30秒）..."
    mvn package -DskipTests -q
    echo ""
fi

# 启动应用（后台运行，日志写入 app.log）
nohup java -jar target/blog-1.0.0.jar > app.log 2>&1 &
echo $! > app.pid

sleep 3

if kill -0 $(cat app.pid) 2>/dev/null; then
    echo "✅ 启动成功！"
    echo "   🌐 打开浏览器访问: http://localhost:8081"
    echo "   📋 查看日志: tail -f app.log"
    echo "   🛑 停止服务: ./stop.sh"
else
    echo "❌ 启动失败，请查看 app.log"
    rm -f app.pid
fi

echo ""
read -p "按回车键退出..."
