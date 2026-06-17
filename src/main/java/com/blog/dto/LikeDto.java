package com.blog.dto;

import javax.validation.constraints.NotNull;

/**
 * 点赞数据传输对象 — 用于点赞/取消点赞的请求体
 *
 * <p>仅需传递被点赞的文章 ID，用户信息从 session 中获取。</p>
 *
 * @author blog
 * @version 1.0.0
 */
public class LikeDto {
    /** 被点赞的文章ID（必填） */
    @NotNull(message = "文章ID不能为空")
    private Long articleId;

    public Long getArticleId() { return articleId; }
    public void setArticleId(Long articleId) { this.articleId = articleId; }
}
