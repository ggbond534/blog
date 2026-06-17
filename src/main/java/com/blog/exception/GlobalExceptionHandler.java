package com.blog.exception;

import com.blog.common.ResponseResult;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * <p>使用 @RestControllerAdvice 统一拦截所有 Controller 抛出的异常，
 * 返回格式化的 {@link ResponseResult} JSON 响应，避免异常信息直接暴露给前端。</p>
 *
 * <h3>处理的异常类型</h3>
 * <ul>
 *   <li>{@link MethodArgumentNotValidException} — @Validated 校验失败（JSON 请求体）</li>
 *   <li>{@link BindException} — 参数绑定/校验失败（表单请求）</li>
 *   <li>{@link UnauthorizedException} — 未登录（返回 401）</li>
 *   <li>{@link BadRequestException} — 业务逻辑错误（返回 400）</li>
 *   <li>{@link HttpMessageNotReadableException} — 请求体解析失败</li>
 *   <li>{@link Exception} — 兜底处理未知异常（返回 500）</li>
 * </ul>
 *
 * @author blog
 * @version 1.0.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理 @Validated 校验失败异常（JSON 请求体）
     * 收集所有字段校验错误信息，拼接后返回
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult<?> handleValidException(MethodArgumentNotValidException ex) {
        StringBuilder message = new StringBuilder();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            message.append(error.getDefaultMessage()).append(";");
        }
        return ResponseResult.fail(message.toString());
    }

    /**
     * 处理参数绑定/校验失败异常（表单请求）
     * 同样收集所有字段错误信息并返回
     */
    @ExceptionHandler(BindException.class)
    public ResponseResult<?> handleBindException(BindException ex) {
        StringBuilder message = new StringBuilder();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            message.append(error.getDefaultMessage()).append(";");
        }
        return ResponseResult.fail(message.toString());
    }

    /**
     * 处理未授权异常 — 返回 401 状态码
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseResult<?> handleUnauthorized(UnauthorizedException ex) {
        return ResponseResult.fail(401, ex.getMessage());
    }

    /**
     * 处理业务逻辑异常 — 返回 400 状态码
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseResult<?> handleBadRequest(BadRequestException ex) {
        return ResponseResult.fail(400, ex.getMessage());
    }

    /**
     * 处理请求体不可读异常（如 JSON 格式错误）
     * 尝试提取根因消息，避免暴露框架内部信息
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseResult<?> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String msg = ex.getMessage();
        if (ex.getCause() != null) {
            msg = ex.getCause().getMessage();
        }
        return ResponseResult.fail(400, "请求体解析失败: " + (msg != null ? msg : "格式错误"));
    }

    /**
     * 兜底异常处理器 — 捕获所有未明确处理的异常，返回 500
     * 同时打印堆栈方便排查问题
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult<?> handleException(Exception ex) {
        ex.printStackTrace();
        return ResponseResult.fail(500, "服务器内部错误");
    }
}
