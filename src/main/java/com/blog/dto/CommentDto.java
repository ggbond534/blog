package com.blog.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 评论数据传输对象 — 用于发表评论的请求体
 *
 * <h3>字段说明</h3>
 * <ul>
 *   <li><b>articleId：</b>被评论的文章 ID（必填）</li>
 *   <li><b>parentId：</b>父评论 ID（选填，用于回复某条评论）</li>
 *   <li><b>content：</b>评论内容（必填）</li>
 * </ul>
 *
 * @author blog
 * @version 1.0.0
 */
public class CommentDto {
    /** 被评论的文章ID（必填） */
    @NotNull(message = "文章ID不能为空")
    private Long articleId;

    /** 父评论ID（回复时使用，NULL表示一级评论） */
    private Long parentId;

    /** 评论内容（必填） */
    @NotBlank(message = "评论内容不能为空")
    private String content;

    // ========== Getter / Setter ==========

    public Long getArticleId() { return articleId; }
    public void setArticleId(Long articleId) { this.articleId = articleId; }

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
