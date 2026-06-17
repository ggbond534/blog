package com.blog.exception;

/**
 * 未授权异常
 *
 * <p>当用户未登录或 token 无效时抛出，对应 HTTP 401 状态码。</p>
 * <p>由 {@link com.blog.interceptor.AuthInterceptor} 在请求预处理阶段抛出，
 * 被 {@link GlobalExceptionHandler} 统一捕获处理。</p>
 *
 * @author blog
 * @version 1.0.0
 */
public class UnauthorizedException extends RuntimeException {
    /**
     * 构造未授权异常
     *
     * @param message 错误描述（如"请先登录"）
     */
    public UnauthorizedException(String message) {
        super(message);
    }
}
