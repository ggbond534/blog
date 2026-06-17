package com.blog.service;

import com.blog.dto.ArticleDto;
import com.blog.dto.ArticleQueryDto;
import com.blog.entity.Article;

import java.util.List;

/**
 * 文章服务接口
 *
 * <p>定义文章相关的核心业务操作：</p>
 * <ul>
 *   <li>发布文章 — 根据 DTO 和用户 ID 创建新文章</li>
 *   <li>更新文章 — 校验作者权限后更新文章内容</li>
 *   <li>删除文章 — 校验作者权限后删除文章</li>
 *   <li>查看文章详情 — 获取文章详情并增加阅读量</li>
 *   <li>文章列表查询 — 支持分页、关键词搜索、分类筛选</li>
 *   <li>文章计数 — 统计符合条件的文章总数</li>
 * </ul>
 *
 * @author blog
 * @version 1.0.0
 */
public interface ArticleService {

    /**
     * 发布新文章
     * @param articleDto 文章数据（标题、内容、分类等）
     * @param userId     当前登录用户 ID
     * @return 发布后的文章实体（含自增 ID）
     */
    Article publish(ArticleDto articleDto, Long userId);

    /**
     * 更新已有文章（仅文章作者可操作）
     * @param articleDto 文章更新数据
     * @param userId     当前登录用户 ID
     * @return 更新后的文章实体
     * @throws com.blog.exception.BadRequestException 文章不存在或无权限时抛出
     */
    Article update(ArticleDto articleDto, Long userId);

    /**
     * 删除文章（仅文章作者可操作）
     * @param id     文章 ID
     * @param userId 当前登录用户 ID
     * @throws com.blog.exception.BadRequestException 文章不存在或无权限时抛出
     */
    void delete(Long id, Long userId);

    /**
     * 获取文章详情（同时增加阅读量）
     * @param id 文章 ID
     * @return 文章实体
     * @throws com.blog.exception.BadRequestException 文章不存在时抛出
     */
    Article detail(Long id);

    /**
     * 分页查询文章列表
     * @param queryDto 查询条件（分页、关键词、分类筛选）
     * @return 文章实体列表
     */
    List<Article> list(ArticleQueryDto queryDto);

    /**
     * 统计符合条件的文章总数
     * @param queryDto 查询条件
     * @return 文章总数
     */
    int count(ArticleQueryDto queryDto);
}
