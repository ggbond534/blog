package com.blog.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 文章数据传输对象 — 用于文章发布和更新的请求体
 *
 * <p>包含 JSR-303 校验注解，在 Controller 层通过 @Validated 触发校验。</p>
 *
 * <h3>字段说明</h3>
 * <ul>
 *   <li><b>id：</b>更新时必传（根据 ID 找到原文章），发布时可为 null</li>
 *   <li><b>title：</b>文章标题（必填）</li>
 *   <li><b>content：</b>文章正文（必填）</li>
 *   <li><b>summary：</b>文章摘要（选填，为空时自动截取正文前120字）</li>
 *   <li><b>categoryId：</b>所属分类 ID（必填）</li>
 * </ul>
 *
 * @author blog
 * @version 1.0.0
 */
public class ArticleDto {
    /** 文章ID（发布时为 null，更新时必传） */
    private Long id;

    /** 文章标题（必填） */
    @NotBlank(message = "文章标题不能为空")
    private String title;

    /** 文章正文内容（必填） */
    @NotBlank(message = "文章内容不能为空")
    private String content;

    /** 文章摘要（可为空，系统自动生成） */
    private String summary;

    /** 所属分类ID（必填） */
    @NotNull(message = "分类不能为空")
    private Long categoryId;

    // ========== Getter / Setter ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}
