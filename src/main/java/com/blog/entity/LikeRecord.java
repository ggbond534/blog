package com.blog.entity;

import java.time.LocalDateTime;

/**
 * 点赞记录实体类 — 对应数据库 like_record 表
 *
 * <p>记录用户对文章的点赞行为。每个用户对同一篇文章只能有一条点赞记录，
 * 通过 (articleId + userId) 组合唯一约束保证不重复点赞。</p>
 *
 * <h3>字段说明</h3>
 * <table>
 *   <tr><td>id</td><td>主键（自增）</td></tr>
 *   <tr><td>articleId</td><td>被点赞的文章 ID</td></tr>
 *   <tr><td>userId</td><td>点赞用户 ID</td></tr>
 *   <tr><td>createTime</td><td>点赞时间</td></tr>
 * </table>
 *
 * @author blog
 * @version 1.0.0
 */
public class LikeRecord {
    /** 点赞记录主键ID */
    private Long id;
    /** 被点赞的文章ID */
    private Long articleId;
    /** 点赞用户ID */
    private Long userId;
    /** 点赞时间 */
    private LocalDateTime createTime;

    // ========== Getter / Setter ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getArticleId() { return articleId; }
    public void setArticleId(Long articleId) { this.articleId = articleId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
