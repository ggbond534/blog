#!/bin/bash
# 开发模式启动 — 改代码后自动生效
cd "$(dirname "$0")"
echo "🔧 开发模式启动中..."
echo "   改完前端代码直接刷新浏览器即可"
echo "   改完 Java 代码需要重启: Ctrl+C 然后重新运行此脚本"
echo ""
mvn spring-boot:run
