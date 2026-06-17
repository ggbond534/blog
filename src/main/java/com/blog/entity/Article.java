package com.blog.entity;

import java.time.LocalDateTime;

/**
 * 文章实体类 — 对应数据库 article 表
 *
 * <p>每一篇文章包含标题、正文、摘要、所属分类、作者、阅读量和点赞量等信息。</p>
 *
 * <h3>字段说明</h3>
 * <table>
 *   <tr><td>id</td><td>主键（自增）</td></tr>
 *   <tr><td>userId</td><td>作者用户 ID</td></tr>
 *   <tr><td>categoryId</td><td>所属分类 ID</td></tr>
 *   <tr><td>title</td><td>文章标题</td></tr>
 *   <tr><td>content</td><td>文章正文内容</td></tr>
 *   <tr><td>summary</td><td>文章摘要（发布时可为空，自动截取正文前120字符）</td></tr>
 *   <tr><td>viewCount</td><td>阅读量（每次查看详情 +1）</td></tr>
 *   <tr><td>likeCount</td><td>点赞量（每次点赞 +1，取消点赞 -1）</td></tr>
 *   <tr><td>createTime</td><td>创建时间</td></tr>
 *   <tr><td>updateTime</td><td>最后更新时间</td></tr>
 * </table>
 *
 * @author blog
 * @version 1.0.0
 */
public class Article {
    /** 文章主键ID */
    private Long id;
    /** 作者用户ID */
    private Long userId;
    /** 所属分类ID */
    private Long categoryId;
    /** 文章标题 */
    private String title;
    /** 文章正文内容 */
    private String content;
    /** 文章摘要 */
    private String summary;
    /** 阅读量计数 */
    private Integer viewCount;
    /** 点赞量计数 */
    private Integer likeCount;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 最后更新时间 */
    private LocalDateTime updateTime;

    /** 作者昵称（非数据库字段，由 JOIN 查询填充） */
    private String authorNickname;
    /** 作者头像URL（非数据库字段，由 JOIN 查询填充） */
    private String authorAvatar;

    // ========== Getter / Setter ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }

    public Integer getLikeCount() { return likeCount; }
    public void setLikeCount(Integer likeCount) { this.likeCount = likeCount; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

    public String getAuthorNickname() { return authorNickname; }
    public void setAuthorNickname(String authorNickname) { this.authorNickname = authorNickname; }

    public String getAuthorAvatar() { return authorAvatar; }
    public void setAuthorAvatar(String authorAvatar) { this.authorAvatar = authorAvatar; }
}
