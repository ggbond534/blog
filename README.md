# 个人博客系统（Java 后端）

技术栈：Java 11 + Spring Boot 2.7.x + MyBatis 2.2.x + MySQL 5.7 + Maven 3.8+

## 功能概览

- 用户注册、登录、密码 MD5 加密、参数校验、登录状态校验
- 文章发布、编辑、删除、分页查询、模糊搜索、分类筛选、阅读计数
- 分类管理、评论发布与展示、楼中楼回复、点赞功能
- 统一返回结果封装、全局异常处理

## 本地运行

1. 创建数据库 `blog`
2. 修改 `src/main/resources/application.properties` 中的数据库连接配置
3. 运行 `mvn spring-boot:run`
4. 使用 Postman 调试接口

## 数据库脚本

SQL 声明文件：`src/main/resources/sql/schema.sql`
