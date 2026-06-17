package com.blog.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户注册数据传输对象 — 用于注册请求体
 *
 * <p>包含 JSR-303 校验：用户名/密码必填、密码长度 6-32、邮箱格式校验。</p>
 *
 * @author blog
 * @version 1.0.0
 */
public class UserRegisterDto {
    /** 用户名（必填） */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /** 密码（必填，长度6-32位） */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 32, message = "密码长度必须在6到32之间")
    private String password;

    /** 邮箱地址（选填，格式校验） */
    @Email(message = "邮箱格式不正确")
    private String email;

    /** 电话号码（选填） */
    private String phone;

    /** 昵称（选填，为空则默认使用用户名） */
    private String nickname;

    // ========== Getter / Setter ==========

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
}
