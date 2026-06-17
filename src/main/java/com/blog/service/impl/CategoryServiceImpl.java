package com.blog.service.impl;

import com.blog.entity.Category;
import com.blog.exception.BadRequestException;
import com.blog.mapper.CategoryMapper;
import com.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类服务实现类
 *
 * <p>实现 {@link CategoryService} 接口，包含分类增删改查的核心逻辑。</p>
 *
 * <h3>关键业务规则</h3>
 * <ul>
 *   <li>创建分类时校验名称唯一性</li>
 *   <li>更新分类前校验分类是否存在</li>
 *   <li>删除分类为物理删除操作</li>
 * </ul>
 *
 * @author blog
 * @version 1.0.0
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 创建新分类（先校验名称是否已存在）
     */
    @Override
    public Category create(Category category) {
        if (categoryMapper.findByName(category.getName()) != null) {
            throw new BadRequestException("分类名称已存在");
        }
        categoryMapper.insert(category);
        return category;
    }

    /**
     * 更新分类（先校验分类是否存在）
     */
    @Override
    public Category update(Category category) {
        Category exist = categoryMapper.findById(category.getId());
        if (exist == null) {
            throw new BadRequestException("分类不存在");
        }
        categoryMapper.update(category);
        return category;
    }

    /**
     * 删除分类（物理删除）
     */
    @Override
    public void delete(Long id) {
        categoryMapper.deleteById(id);
    }

    /**
     * 获取所有分类列表
     */
    @Override
    public List<Category> list() {
        return categoryMapper.findAll();
    }
}
