package com.blog.service;

import com.blog.entity.Category;

import java.util.List;

/**
 * 分类服务接口
 *
 * <p>定义分类管理相关的业务操作：创建、更新、删除和列表查询。</p>
 * <p>分类名称具有唯一性约束，创建时会进行重复名校验。</p>
 *
 * @author blog
 * @version 1.0.0
 */
public interface CategoryService {

    /**
     * 创建新分类
     * @param category 分类实体（含名称、描述、分组、排序）
     * @return 创建后的分类实体（含自增 ID）
     * @throws com.blog.exception.BadRequestException 分类名称已存在时抛出
     */
    Category create(Category category);

    /**
     * 更新分类信息
     * @param category 分类实体（需包含 id）
     * @return 更新后的分类实体
     * @throws com.blog.exception.BadRequestException 分类不存在时抛出
     */
    Category update(Category category);

    /**
     * 删除分类
     * @param id 分类 ID
     */
    void delete(Long id);

    /**
     * 获取所有分类列表（按分组和排序规则返回）
     * @return 分类实体列表
     */
    List<Category> list();
}
