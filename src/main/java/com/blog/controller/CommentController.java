package com.blog.controller;

import com.blog.common.ResponseResult;
import com.blog.dto.CommentDto;
import com.blog.entity.Comment;
import com.blog.entity.User;
import com.blog.mapper.CommentMapper;
import com.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 评论控制器 — 处理评论相关的 HTTP 请求
 *
 * <p>提供以下 REST API 端点（基础路径 /api/comment）：</p>
 * <ul>
 *   <li><b>POST /publish</b> — 发表评论（需登录）</li>
 *   <li><b>GET /list</b> — 查询某篇文章的评论列表（公开）</li>
 * </ul>
 *
 * @author blog
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentMapper commentMapper;

    /**
     * 发表评论（需登录）
     * 支持顶级评论（parentId=null）和子回复（parentId 指向父评论）
     *
     * @param commentDto 评论数据（文章ID、父评论ID、内容）
     * @param session    当前会话（获取登录用户）
     * @return 包含新评论实体的成功响应
     */
    @PostMapping("/publish")
    public ResponseResult<Comment> publish(@Validated @RequestBody CommentDto commentDto, HttpSession session) {
        User user = (User) session.getAttribute("user");
        return ResponseResult.success(commentService.create(commentDto, user.getId()));
    }

    /**
     * 分页查询评论（默认每页5条）
     */
    @GetMapping("/list")
    public ResponseResult<Map<String, Object>> list(@RequestParam Long articleId,
                                                     @RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "5") int pageSize) {
        return ResponseResult.success(commentService.listByArticleId(articleId, page, pageSize));
    }

    /**
     * 删除评论（需登录，仅评论作者可删）
     */
    @PostMapping("/delete")
    public ResponseResult<?> delete(@RequestParam Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Comment comment = commentMapper.findById(id);
        if (comment == null) {
            return ResponseResult.fail(404, "评论不存在");
        }
        if (!comment.getUserId().equals(user.getId())) {
            return ResponseResult.fail(403, "只能删除自己的评论");
        }
        commentMapper.deleteById(id);
        return ResponseResult.success("删除成功", null);
    }
}
