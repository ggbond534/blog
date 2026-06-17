package com.blog.service.impl;

import com.blog.dto.CommentDto;
import com.blog.entity.Comment;
import com.blog.exception.BadRequestException;
import com.blog.mapper.CommentMapper;
import com.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论服务实现类
 *
 * <p>实现 {@link CommentService} 接口，处理评论的创建和查询逻辑。</p>
 * <p>评论支持层级结构：parentId 为 null 表示一级评论，非 null 表示对某条评论的回复。</p>
 *
 * @author blog
 * @version 1.0.0
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    /**
     * 发表评论
     * 从 CommentDto 构建 Comment 实体，设置用户 ID 和时间后插入数据库
     */
    @Override
    public Comment create(CommentDto commentDto, Long userId) {
        Comment comment = new Comment();
        comment.setArticleId(commentDto.getArticleId());
        comment.setUserId(userId);
        // parentId 可为 null，表示顶级评论
        comment.setParentId(commentDto.getParentId());
        comment.setContent(commentDto.getContent());
        comment.setCreateTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());

        commentMapper.insert(comment);
        return comment;
    }

    /**
     * 分页查询评论列表
     */
    @Override
    public Map<String, Object> listByArticleId(Long articleId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Comment> list = commentMapper.findByArticleId(articleId, offset, pageSize);
        int total = commentMapper.countByArticleId(articleId);
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return result;
    }
}
