package com.blog.mapper;

import com.blog.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论 MyBatis Mapper 接口
 *
 * <p>提供评论表（comment）的增删查操作，按创建时间升序展示评论。</p>
 * <p>SQL 实现位于：resources/mybatis/mapper/CommentMapper.xml</p>
 *
 * @author blog
 * @version 1.0.0
 */
@Mapper
public interface CommentMapper {

    /** 插入新评论 */
    int insert(Comment comment);

    /** 根据主键查询单条评论 */
    Comment findById(@Param("id") Long id);

    /** 根据主键删除评论 */
    int deleteById(@Param("id") Long id);

    /** 根据文章ID分页查询评论（按创建时间升序） */
    List<Comment> findByArticleId(@Param("articleId") Long articleId,
                                  @Param("offset") int offset,
                                  @Param("limit") int limit);

    /** 统计某篇文章的评论总数 */
    int countByArticleId(@Param("articleId") Long articleId);
}
