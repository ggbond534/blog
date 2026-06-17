package com.blog.interceptor;

import com.blog.entity.User;
import com.blog.exception.UnauthorizedException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 身份认证拦截器
 *
 * <p>在请求到达 Controller 之前执行，检查当前会话中是否存在已登录用户。</p>
 * <p>如果用户未登录（session 为 null 或 session 中没有 user 属性），
 * 抛出 {@link UnauthorizedException}，由全局异常处理器返回 401 响应。</p>
 *
 * <p>拦截范围由 {@link com.blog.config.WebConfig} 中配置决定，
 * 公开接口（登录、注册、文章列表/详情、分类列表）不经过此拦截器。</p>
 *
 * @author blog
 * @version 1.0.0
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    /**
     * 请求预处理：校验用户是否已登录
     *
     * @param request  HTTP 请求对象
     * @param response HTTP 响应对象
     * @param handler  处理器（Controller 方法）
     * @return true 表示放行请求，false 表示拦截（此处通过异常终止）
     * @throws UnauthorizedException 当 session 不存在或 session 中没有用户信息时抛出
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取当前会话，参数 false 表示没有 session 时不创建新的
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new UnauthorizedException("请先登录");
        }
        // 从 session 中获取已登录用户信息
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new UnauthorizedException("请先登录");
        }
        // 用户已登录，放行请求
        return true;
    }
}
