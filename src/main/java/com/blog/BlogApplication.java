package com.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 博客系统 Spring Boot 启动类
 *
 * <p>@SpringBootApplication 是一个组合注解，包含：</p>
 * <ul>
 *   <li>@SpringBootConfiguration — 标识为配置类</li>
 *   <li>@EnableAutoConfiguration — 自动配置 Spring 上下文</li>
 *   <li>@ComponentScan — 扫描当前包及子包中的组件</li>
 * </ul>
 *
 * <p>运行 main 方法即可启动内嵌 Tomcat，默认监听 application.properties 中配置的端口。</p>
 *
 * @author blog
 * @version 1.0.0
 */
@SpringBootApplication
public class BlogApplication {

    /**
     * 应用入口方法
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }
}
