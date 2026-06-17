package com.blog.controller;

import com.blog.common.ResponseResult;
import com.blog.entity.LikeRecord;
import com.blog.entity.User;
import com.blog.mapper.LikeRecordMapper;
import com.blog.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 点赞控制器 — 处理点赞相关的 HTTP 请求
 *
 * <p>提供以下 REST API 端点（基础路径 /api/like）：</p>
 * <ul>
 *   <li><b>POST /add</b> — 点赞（需登录）</li>
 *   <li><b>POST /remove</b> — 取消点赞（需登录）</li>
 * </ul>
 * <p>每个用户对每篇文章只能点赞一次，重复点赞会返回错误。</p>
 *
 * @author blog
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private LikeRecordMapper likeRecordMapper;

    /**
     * 切换点赞状态（需登录）
     * 已点赞则取消，未点赞则点赞
     */
    @PostMapping("/toggle")
    public ResponseResult<Map<String, Object>> toggle(@RequestParam Long articleId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        boolean liked = likeService.toggle(articleId, user.getId());
        Map<String, Object> result = new HashMap<>();
        result.put("liked", liked);
        result.put("message", liked ? "点赞成功" : "已取消点赞");
        return ResponseResult.success(result);
    }

    /**
     * 获取当前用户已点赞的文章ID列表（用于前端显示红心）
     */
    @GetMapping("/my-likes")
    public ResponseResult<List<Long>> myLikes(HttpSession session) {
        User user = (User) session.getAttribute("user");
        // 查询该用户的所有点赞记录，提取文章ID
        List<LikeRecord> records = likeRecordMapper.findByUserId(user.getId());
        List<Long> articleIds = records.stream().map(LikeRecord::getArticleId).collect(Collectors.toList());
        return ResponseResult.success(articleIds);
    }

    /**
     * 点赞（需登录）
     */
    @PostMapping("/add")
    public ResponseResult<?> like(@RequestParam Long articleId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        likeService.like(articleId, user.getId());
        return ResponseResult.success("点赞成功", null);
    }

    /**
     * 取消点赞（需登录）
     */
    @PostMapping("/remove")
    public ResponseResult<?> unlike(@RequestParam Long articleId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        likeService.unlike(articleId, user.getId());
        return ResponseResult.success("取消点赞成功", null);
    }
}
