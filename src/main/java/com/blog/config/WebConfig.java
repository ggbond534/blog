package com.blog.config;

import com.blog.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Spring MVC Web 配置类
 *
 * <p>主要职责：注册身份认证拦截器（AuthInterceptor），对 API 请求进行登录校验。</p>
 *
 * <h3>拦截规则</h3>
 * <ul>
 *   <li><b>拦截路径：</b>所有 /api/** 路径的请求都需要通过认证拦截器</li>
 *   <li><b>排除路径（无需登录即可访问）：</b>
 *     <ul>
 *       <li>/api/user/login — 用户登录</li>
 *       <li>/api/user/register — 用户注册</li>
 *       <li>/api/article/list — 文章列表</li>
 *       <li>/api/article/detail — 文章详情</li>
 *       <li>/api/category/list — 分类列表</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * @author blog
 * @version 1.0.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /** 注入认证拦截器 */
    @Autowired
    private AuthInterceptor authInterceptor;

    /** 头像上传路径 */
    @Value("${upload.avatar.path:uploads/avatars/}")
    private String avatarPath;

    /**
     * 静态资源映射：上传的头像文件
     * 本地开发由 Spring Boot 默认 static 目录提供
     * 云端部署通过此映射从外部目录提供
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir;
        if (avatarPath.startsWith("/") || avatarPath.contains(":")) {
            uploadDir = Paths.get(avatarPath);
        } else {
            Path staticDir = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static");
            if (Files.exists(staticDir)) {
                uploadDir = staticDir.resolve(avatarPath);
            } else {
                uploadDir = Paths.get(System.getProperty("user.dir"), avatarPath);
            }
        }
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir.toAbsolutePath() + "/");
    }

    /**
     * 注册拦截器到 Spring MVC 拦截器链
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/user/login",
                        "/api/user/register",
                        "/api/article/list",
                        "/api/article/detail",
                        "/api/category/list"
                );
    }
}
