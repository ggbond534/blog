package com.blog.service.impl;

import com.blog.entity.Article;
import com.blog.entity.LikeRecord;
import com.blog.entity.User;
import com.blog.exception.BadRequestException;
import com.blog.mapper.ArticleMapper;
import com.blog.mapper.LikeRecordMapper;
import com.blog.mapper.UserMapper;
import com.blog.service.LikeService;
import com.blog.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 点赞服务实现类
 *
 * <p>实现 {@link LikeService} 接口，处理点赞和取消点赞的完整逻辑：
 * 同时维护点赞记录表（like_record）和文章表（article.like_count）。</p>
 *
 * <h3>点赞流程</h3>
 * <ol>
 *   <li>检查是否已点赞（同一用户对同一文章不可重复点赞）</li>
 *   <li>插入点赞记录</li>
 *   <li>文章点赞量 +1（数据库原子自增）</li>
 * </ol>
 *
 * <h3>取消点赞流程</h3>
 * <ol>
 *   <li>检查点赞记录是否存在</li>
 *   <li>删除点赞记录</li>
 *   <li>文章点赞量 -1（需先确认当前计数 > 0）</li>
 * </ol>
 *
 * @author blog
 * @version 1.0.0
 */
@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeRecordMapper likeRecordMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationService notificationService;

    /**
     * 点赞操作（含通知）
     */
    @Override
    public void like(Long articleId, Long userId) {
        // 校验是否已点赞，防止重复点赞
        if (likeRecordMapper.findByArticleIdAndUserId(articleId, userId) != null) {
            throw new BadRequestException("已点赞过");
        }

        // 创建点赞记录
        LikeRecord likeRecord = new LikeRecord();
        likeRecord.setArticleId(articleId);
        likeRecord.setUserId(userId);
        likeRecord.setCreateTime(LocalDateTime.now());
        likeRecordMapper.insert(likeRecord);

        // 文章点赞量原子自增 +1
        articleMapper.incrementLikeCount(articleId);

        // 发送通知给文章作者（不给自己发）
        Article article = articleMapper.findById(articleId);
        if (article != null && !article.getUserId().equals(userId)) {
            User liker = userMapper.findById(userId);
            String likerName = liker != null && liker.getNickname() != null ? liker.getNickname() : (liker != null ? liker.getUsername() : "有人");
            notificationService.createLikeNotification(
                article.getUserId(),
                articleId,
                article.getTitle(),
                likerName
            );
        }
    }

    /**
     * 取消点赞操作
     */
    @Override
    public void unlike(Long articleId, Long userId) {
        LikeRecord record = likeRecordMapper.findByArticleIdAndUserId(articleId, userId);
        if (record == null) {
            throw new BadRequestException("尚未点赞");
        }

        likeRecordMapper.deleteByArticleIdAndUserId(articleId, userId);

        Article article = articleMapper.findById(articleId);
        if (article != null && article.getLikeCount() != null && article.getLikeCount() > 0) {
            article.setLikeCount(article.getLikeCount() - 1);
            articleMapper.update(article);
        }
    }

    /**
     * 切换点赞状态：已点赞则取消，未点赞则点赞
     * 1分钟内对同一文章反复操作，只发一次通知
     * @return true=当前已点赞, false=当前已取消
     */
    @Override
    public boolean toggle(Long articleId, Long userId) {
        LikeRecord record = likeRecordMapper.findByArticleIdAndUserId(articleId, userId);
        if (record != null) {
            // 已点赞 → 取消
            unlike(articleId, userId);
            return false;
        } else {
            // 未点赞 → 点赞（带通知去重）
            like(articleId, userId);
            return true;
        }
    }
}
