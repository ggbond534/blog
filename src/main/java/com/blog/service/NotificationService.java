package com.blog.service;

import com.blog.entity.Notification;

import java.util.List;

/**
 * 通知服务接口
 */
public interface NotificationService {
    /** 查询用户的所有通知 */
    List<Notification> listByUser(Long userId);

    /** 获取未读通知数量 */
    int unreadCount(Long userId);

    /** 标记单条通知为已读 */
    void markRead(Long id);

    /** 标记全部已读 */
    void markAllRead(Long userId);

    /** 创建点赞通知（1分钟内同一文章不重复通知） */
    void createLikeNotification(Long articleAuthorId, Long articleId, String articleTitle, String likerNickname);
}
