package com.blog.dto;

/**
 * 文章查询数据传输对象 — 用于文章列表的分页查询和筛选
 *
 * <p>前端通过 URL 查询参数传递，Spring MVC 自动绑定到此对象。</p>
 *
 * <h3>字段说明</h3>
 * <ul>
 *   <li><b>pageNum：</b>当前页码（默认 1）</li>
 *   <li><b>pageSize：</b>每页条数（默认 10）</li>
 *   <li><b>keyword：</b>搜索关键词（模糊匹配标题和正文）</li>
 *   <li><b>categoryId：</b>分类筛选（null 表示不筛选）</li>
 * </ul>
 *
 * @author blog
 * @version 1.0.0
 */
public class ArticleQueryDto {
    /** 当前页码（从1开始，默认1） */
    private Integer pageNum = 1;
    /** 每页显示条数（默认10） */
    private Integer pageSize = 10;
    /** 搜索关键词（对标题和内容进行模糊匹配） */
    private String keyword;
    /** 按分类ID筛选（null 表示全部分类） */
    private Long categoryId;

    // ========== Getter / Setter ==========

    public Integer getPageNum() { return pageNum; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }

    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}
