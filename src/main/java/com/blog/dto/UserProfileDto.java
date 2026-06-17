package com.blog.dto;

/**
 * 用户个人资料更新数据传输对象
 *
 * <p>用于更新昵称和个人简介，头像通过单独的文件上传接口处理。</p>
 *
 * @author blog
 * @version 1.0.0
 */
public class UserProfileDto {
    /** 用户昵称（显示名） */
    private String nickname;
    /** 个人简介 */
    private String bio;

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
}
