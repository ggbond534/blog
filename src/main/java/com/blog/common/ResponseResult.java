package com.blog.common;

/**
 * 统一 API 响应结果封装类
 *
 * <p>所有 REST 接口的返回值都使用此类包装，确保前端收到的 JSON 格式统一：</p>
 * <pre>{@code
 * {
 *   "code": 200,       // 状态码
 *   "message": "success", // 提示信息
 *   "data": { ... }    // 响应数据（泛型）
 * }
 * }</pre>
 *
 * <p>提供静态工厂方法快速创建成功/失败响应：</p>
 * <ul>
 *   <li>{@link #success(Object)} — 200 成功响应（带数据）</li>
 *   <li>{@link #success(String, Object)} — 200 成功响应（自定义消息 + 数据）</li>
 *   <li>{@link #fail(String)} — 500 失败响应</li>
 *   <li>{@link #fail(int, String)} — 自定义状态码的失败响应</li>
 * </ul>
 *
 * @param <T> 响应数据的类型
 * @author blog
 * @version 1.0.0
 */
public class ResponseResult<T> {
    /** HTTP 状态码（200 成功，400 请求错误，401 未授权，500 服务器错误） */
    private int code;

    /** 响应提示信息 */
    private String message;

    /** 响应数据载体（泛型，可为 null） */
    private T data;

    /** 无参构造 */
    public ResponseResult() {
    }

    /** 全参构造 */
    public ResponseResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 创建成功响应（状态码 200）
     *
     * @param data 响应数据
     * @param <T>  数据类型
     * @return 包含数据的成功响应
     */
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(200, "success", data);
    }

    /**
     * 创建带自定义消息的成功响应（状态码 200）
     *
     * @param message 自定义成功消息
     * @param data    响应数据
     * @param <T>     数据类型
     * @return 包含数据和自定义消息的成功响应
     */
    public static <T> ResponseResult<T> success(String message, T data) {
        return new ResponseResult<>(200, message, data);
    }

    /**
     * 创建自定义状态码的失败响应
     *
     * @param code    错误状态码（如 400、401、500）
     * @param message 错误描述
     * @param <T>     数据类型
     * @return 失败响应（data 为 null）
     */
    public static <T> ResponseResult<T> fail(int code, String message) {
        return new ResponseResult<>(code, message, null);
    }

    /**
     * 创建默认服务器错误响应（状态码 500）
     *
     * @param message 错误描述
     * @param <T>     数据类型
     * @return 服务器错误响应
     */
    public static <T> ResponseResult<T> fail(String message) {
        return new ResponseResult<>(500, message, null);
    }

    // ========== Getter / Setter ==========

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
