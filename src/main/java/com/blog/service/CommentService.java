package com.blog.service;

import com.blog.dto.CommentDto;
import com.blog.entity.Comment;

import java.util.List;
import java.util.Map;

/**
 * 评论服务接口
 *
 * <p>定义评论相关的业务操作：</p>
 * <ul>
 *   <li>发表评论（支持顶级评论和子回复）</li>
 *   <li>根据文章 ID 查询评论列表</li>
 * </ul>
 *
 * @author blog
 * @version 1.0.0
 */
public interface CommentService {

    /**
     * 发表评论
     * @param commentDto 评论数据（文章ID、父评论ID、内容）
     * @param userId     当前登录用户 ID
     * @return 创建后的评论实体
     */
    Comment create(CommentDto commentDto, Long userId);

    /**
     * 分页查询评论
     * @param articleId 文章 ID
     * @param page      页码（从1开始）
     * @param pageSize  每页条数
     * @return Map包含 list(评论列表) 和 total(总数)
     */
    Map<String, Object> listByArticleId(Long articleId, int page, int pageSize);
}
