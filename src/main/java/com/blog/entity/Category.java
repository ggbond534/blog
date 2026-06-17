package com.blog.entity;

/**
 * 分类实体类 — 对应数据库 category 表
 *
 * <p>文章分类管理，支持将分类归入"日常"或"其他"两个分组，并支持排序。</p>
 *
 * <h3>字段说明</h3>
 * <table>
 *   <tr><td>id</td><td>主键（自增）</td></tr>
 *   <tr><td>name</td><td>分类名称（唯一）</td></tr>
 *   <tr><td>description</td><td>分类描述</td></tr>
 *   <tr><td>groupType</td><td>所属分组（"日常" 或 "其他"）</td></tr>
 *   <tr><td>sortOrder</td><td>排序权重（数值越大越靠前）</td></tr>
 * </table>
 *
 * @author blog
 * @version 1.0.0
 */
public class Category {
    /** 分类主键ID */
    private Long id;
    /** 分类名称（唯一约束） */
    private String name;
    /** 分类描述 */
    private String description;
    /** 所属分组类型：日常 / 其他 */
    private String groupType;
    /** 排序权重（数值越大越靠前） */
    private Integer sortOrder;

    // ========== Getter / Setter ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getGroupType() { return groupType; }
    public void setGroupType(String groupType) { this.groupType = groupType; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}
