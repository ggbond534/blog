package com.blog.mapper;

import com.blog.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通知 MyBatis Mapper 接口
 */
@Mapper
public interface NotificationMapper {
    /** 插入新通知 */
    int insert(Notification notification);

    /** 查询某用户的所有通知（按时间倒序） */
    List<Notification> findByUserId(@Param("userId") Long userId);

    /** 获取某用户未读通知数量 */
    int countUnread(@Param("userId") Long userId);

    /** 标记某条通知为已读 */
    int markRead(@Param("id") Long id);

    /** 标记某用户所有通知为已读 */
    int markAllRead(@Param("userId") Long userId);

    /** 查询1分钟内同一文章+作者的点赞通知数量 */
    int countRecentLike(@Param("userId") Long userId, @Param("articleId") Long articleId);
}
