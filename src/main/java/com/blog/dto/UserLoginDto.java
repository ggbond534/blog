package com.blog.dto;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录数据传输对象 — 用于登录请求体
 *
 * <p>包含用户名和密码两个必填字段，由 @Validated 触发校验。</p>
 *
 * @author blog
 * @version 1.0.0
 */
public class UserLoginDto {
    /** 用户名（必填） */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /** 密码（必填，明文传输，服务端 MD5 加密后比对） */
    @NotBlank(message = "密码不能为空")
    private String password;

    // ========== Getter / Setter ==========

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
