package com.blog.entity;

import java.time.LocalDateTime;

/**
 * 评论实体类 — 对应数据库 comment 表
 *
 * <p>文章评论，支持一级评论和子评论（通过 parentId 实现层级关系）。</p>
 *
 * <h3>字段说明</h3>
 * <table>
 *   <tr><td>id</td><td>主键（自增）</td></tr>
 *   <tr><td>articleId</td><td>被评论的文章 ID</td></tr>
 *   <tr><td>userId</td><td>评论者用户 ID</td></tr>
 *   <tr><td>parentId</td><td>父评论 ID（用于子评论/回复，可为 null 表示一级评论）</td></tr>
 *   <tr><td>content</td><td>评论内容</td></tr>
 *   <tr><td>createTime</td><td>评论时间</td></tr>
 *   <tr><td>updateTime</td><td>最后更新时间</td></tr>
 * </table>
 *
 * @author blog
 * @version 1.0.0
 */
public class Comment {
    /** 评论主键ID */
    private Long id;
    /** 被评论的文章ID */
    private Long articleId;
    /** 评论者用户ID */
    private Long userId;
    /** 父评论ID（子回复时指向上级评论，NULL表示顶级评论） */
    private Long parentId;
    /** 评论内容 */
    private String content;
    /** 评论创建时间 */
    private LocalDateTime createTime;
    /** 评论最后更新时间 */
    private LocalDateTime updateTime;

    /** 评论者昵称（非数据库字段，由 JOIN 查询填充） */
    private String authorNickname;
    /** 评论者头像URL（非数据库字段，由 JOIN 查询填充） */
    private String authorAvatar;

    // ========== Getter / Setter ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getArticleId() { return articleId; }
    public void setArticleId(Long articleId) { this.articleId = articleId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

    public String getAuthorNickname() { return authorNickname; }
    public void setAuthorNickname(String authorNickname) { this.authorNickname = authorNickname; }

    public String getAuthorAvatar() { return authorAvatar; }
    public void setAuthorAvatar(String authorAvatar) { this.authorAvatar = authorAvatar; }
}
