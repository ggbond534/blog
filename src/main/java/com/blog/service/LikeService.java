package com.blog.service;

/**
 * 点赞服务接口
 *
 * <p>定义点赞/取消点赞两个核心操作：</p>
 * <ul>
 *   <li>点赞 — 记录用户对文章的点赞，同时文章点赞量 +1</li>
 *   <li>取消点赞 — 删除点赞记录，同时文章点赞量 -1</li>
 * </ul>
 * <p>每个用户对同一篇文章只能点赞一次，重复点赞会抛出异常。</p>
 *
 * @author blog
 * @version 1.0.0
 */
public interface LikeService {

    /**
     * 点赞
     * @param articleId 文章 ID
     * @param userId    当前登录用户 ID
     * @throws com.blog.exception.BadRequestException 已点赞时抛出
     */
    void like(Long articleId, Long userId);

    /**
     * 取消点赞
     * @param articleId 文章 ID
     * @param userId    当前登录用户 ID
     * @throws com.blog.exception.BadRequestException 尚未点赞时抛出
     */
    void unlike(Long articleId, Long userId);

    /**
     * 切换点赞状态：已点赞则取消，未点赞则点赞
     * @param articleId 文章 ID
     * @param userId    当前登录用户 ID
     * @return true=已点赞, false=已取消
     */
    boolean toggle(Long articleId, Long userId);
}
