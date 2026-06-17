package com.blog.entity;

import java.time.LocalDateTime;

/**
 * 用户实体类 — 对应数据库 user 表
 *
 * <p>博客系统的用户账号信息，密码使用 MD5 加密存储。</p>
 *
 * <h3>字段说明</h3>
 * <table>
 *   <tr><td>id</td><td>主键（自增）</td></tr>
 *   <tr><td>username</td><td>用户名（唯一，用于登录）</td></tr>
 *   <tr><td>password</td><td>密码（MD5 加密后的密文）</td></tr>
 *   <tr><td>email</td><td>邮箱地址</td></tr>
 *   <tr><td>phone</td><td>电话号码</td></tr>
 *   <tr><td>nickname</td><td>用户昵称（显示用，默认同用户名）</td></tr>
 *   <tr><td>avatar</td><td>头像图片 URL 路径</td></tr>
 *   <tr><td>bio</td><td>个人简介</td></tr>
 *   <tr><td>createTime</td><td>注册时间</td></tr>
 *   <tr><td>updateTime</td><td>最后更新时间</td></tr>
 * </table>
 *
 * @author blog
 * @version 1.0.0
 */
public class User {
    /** 用户主键ID */
    private Long id;
    /** 用户名（唯一，登录凭证） */
    private String username;
    /** 密码（MD5加密后的密文，返回前端时应置为 null） */
    private String password;
    /** 邮箱地址 */
    private String email;
    /** 电话号码 */
    private String phone;
    /** 用户昵称（显示用，注册时默认同用户名） */
    private String nickname;
    /** 头像图片URL路径 */
    private String avatar;
    /** 个人简介 */
    private String bio;
    /** 注册时间 */
    private LocalDateTime createTime;
    /** 最后更新时间 */
    private LocalDateTime updateTime;

    // ========== Getter / Setter ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
