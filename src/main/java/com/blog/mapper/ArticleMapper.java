package com.blog.mapper;

import com.blog.entity.Article;
import com.blog.dto.ArticleQueryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章 MyBatis Mapper 接口
 *
 * <p>提供文章表（article）的全部数据库操作：
 * 增删改查、阅读量/点赞量自增、条件分页查询及计数。</p>
 *
 * <p>SQL 实现位于：resources/mybatis/mapper/ArticleMapper.xml</p>
 *
 * @author blog
 * @version 1.0.0
 */
@Mapper
public interface ArticleMapper {

    /** 插入新文章，使用数据库自增主键，插入后 article.id 被回填 */
    int insert(Article article);

    /** 更新文章标题、内容、摘要、分类和更新时间 */
    int update(Article article);

    /** 根据主键删除文章 */
    int deleteById(@Param("id") Long id);

    /** 根据主键查询单篇文章 */
    Article findById(@Param("id") Long id);

    /** 阅读量 +1（原子操作，防止并发问题） */
    int incrementViewCount(@Param("id") Long id);

    /** 点赞量 +1（原子操作） */
    int incrementLikeCount(@Param("id") Long id);

    /** 条件分页查询文章列表（支持关键词搜索和分类筛选，按创建时间倒序） */
    List<Article> queryArticles(ArticleQueryDto queryDto);

    /** 统计符合条件的文章总数（用于分页计算） */
    int countArticles(ArticleQueryDto queryDto);
}
