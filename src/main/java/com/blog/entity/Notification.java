package com.blog.entity;

import java.time.LocalDateTime;

/**
 * 消息通知实体类 — 对应数据库 notification 表
 *
 * <p>当用户文章被点赞时，作者会收到一条通知消息。</p>
 *
 * @author blog
 * @version 1.0.0
 */
public class Notification {
    private Long id;
    /** 接收通知的用户ID（文章作者） */
    private Long userId;
    /** 被点赞的文章ID */
    private Long articleId;
    /** 通知类型：like */
    private String type;
    /** 通知消息内容 */
    private String message;
    /** 是否已读：0=未读, 1=已读 */
    private Integer isRead;
    /** 通知创建时间 */
    private LocalDateTime createTime;

    /** 文章标题（非数据库字段，由 JOIN 查询填充，用于前端展示） */
    private String articleTitle;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getArticleId() { return articleId; }
    public void setArticleId(Long articleId) { this.articleId = articleId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Integer getIsRead() { return isRead; }
    public void setIsRead(Integer isRead) { this.isRead = isRead; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public String getArticleTitle() { return articleTitle; }
    public void setArticleTitle(String articleTitle) { this.articleTitle = articleTitle; }
}
