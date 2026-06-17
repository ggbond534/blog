package com.blog.exception;

/**
 * 请求参数/业务逻辑异常
 *
 * <p>当请求参数不合法或业务逻辑校验失败时抛出，对应 HTTP 400 状态码。</p>
 * <p>常见场景：用户名已存在、文章不存在、无权限编辑、分类名称重复等。</p>
 * <p>由 {@link GlobalExceptionHandler} 统一捕获处理。</p>
 *
 * @author blog
 * @version 1.0.0
 */
public class BadRequestException extends RuntimeException {
    /**
     * 构造业务异常
     *
     * @param message 错误描述
     */
    public BadRequestException(String message) {
        super(message);
    }
}
