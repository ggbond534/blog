package com.blog.mapper;

import com.blog.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分类 MyBatis Mapper 接口
 *
 * <p>提供分类表（category）的增删改查操作，按 group_type 和 sort_order 排序。</p>
 * <p>SQL 实现位于：resources/mybatis/mapper/CategoryMapper.xml</p>
 *
 * @author blog
 * @version 1.0.0
 */
@Mapper
public interface CategoryMapper {

    /** 插入新分类，使用数据库自增主键 */
    int insert(Category category);

    /** 根据主键查询分类 */
    Category findById(@Param("id") Long id);

    /** 根据名称查询分类（用于判断名称是否重复） */
    Category findByName(@Param("name") String name);

    /** 更新分类信息（名称、描述、分组、排序） */
    int update(Category category);

    /** 根据主键删除分类 */
    int deleteById(@Param("id") Long id);

    /** 查询所有分类，按 groupType 升序、sortOrder 降序、id 降序排列 */
    List<Category> findAll();
}
