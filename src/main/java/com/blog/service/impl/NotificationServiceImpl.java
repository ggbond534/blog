package com.blog.service.impl;

import com.blog.entity.Notification;
import com.blog.mapper.NotificationMapper;
import com.blog.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public List<Notification> listByUser(Long userId) {
        return notificationMapper.findByUserId(userId);
    }

    @Override
    public int unreadCount(Long userId) {
        return notificationMapper.countUnread(userId);
    }

    @Override
    public void markRead(Long id) {
        notificationMapper.markRead(id);
    }

    @Override
    public void markAllRead(Long userId) {
        notificationMapper.markAllRead(userId);
    }

    @Override
    public void createLikeNotification(Long articleAuthorId, Long articleId, String articleTitle, String likerNickname) {
        if (articleAuthorId == null) return;

        // 1分钟内同一文章已有点赞通知，跳过（去重）
        int recent = notificationMapper.countRecentLike(articleAuthorId, articleId);
        if (recent > 0) return;

        Notification notif = new Notification();
        notif.setUserId(articleAuthorId);
        notif.setArticleId(articleId);
        notif.setType("like");
        notif.setMessage(likerNickname + " 赞了你的文章《" + articleTitle + "》");
        notificationMapper.insert(notif);
    }
}
