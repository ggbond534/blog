package com.blog.controller;

import com.blog.common.ResponseResult;
import com.blog.dto.ArticleDto;
import com.blog.dto.ArticleQueryDto;
import com.blog.entity.Article;
import com.blog.entity.User;
import com.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文章控制器 — 处理文章相关的 HTTP 请求
 *
 * <p>提供以下 REST API 端点（基础路径 /api/article）：</p>
 * <ul>
 *   <li><b>POST /publish</b> — 发布文章（需登录）</li>
 *   <li><b>POST /update</b> — 更新文章（需登录、需作者权限）</li>
 *   <li><b>POST /delete</b> — 删除文章（需登录、需作者权限）</li>
 *   <li><b>GET /detail</b> — 文章详情（公开）</li>
 *   <li><b>GET /list</b> — 文章列表分页查询（公开）</li>
 * </ul>
 *
 * @author blog
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 发布文章
     *
     * @param articleDto 文章发布数据（标题、内容、分类等，经 @Validated 校验）
     * @param session    当前会话（从中获取登录用户）
     * @return 包含新文章实体的成功响应
     */
    @PostMapping("/publish")
    public ResponseResult<Article> publish(@Validated @RequestBody ArticleDto articleDto, HttpSession session) {
        // 从 session 中获取当前登录用户
        User user = (User) session.getAttribute("user");
        return ResponseResult.success(articleService.publish(articleDto, user.getId()));
    }

    /**
     * 更新文章（仅文章作者可操作）
     *
     * @param articleDto 文章更新数据（必须包含 id）
     * @param session    当前会话
     * @return 包含更新后文章实体的成功响应
     */
    @PostMapping("/update")
    public ResponseResult<Article> update(@Validated @RequestBody ArticleDto articleDto, HttpSession session) {
        User user = (User) session.getAttribute("user");
        return ResponseResult.success(articleService.update(articleDto, user.getId()));
    }

    /**
     * 删除文章（仅文章作者可操作）
     *
     * @param id      待删除的文章 ID
     * @param session 当前会话
     * @return 包含删除成功消息的响应
     */
    @PostMapping("/delete")
    public ResponseResult<?> delete(@RequestParam Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        articleService.delete(id, user.getId());
        return ResponseResult.success("删除成功", null);
    }

    /**
     * 获取文章详情（公开接口，同时会增加阅读量）
     *
     * @param id 文章 ID
     * @return 包含文章实体的成功响应
     */
    @GetMapping("/detail")
    public ResponseResult<Article> detail(@RequestParam Long id) {
        return ResponseResult.success(articleService.detail(id));
    }

    /**
     * 分页查询文章列表（公开接口，支持关键词搜索和分类筛选）
     *
     * @param queryDto 查询条件（分页、关键词、分类）
     * @return 包含 total（总数）和 records（文章列表）的 Map
     */
    @GetMapping("/list")
    public ResponseResult<Map<String, Object>> list(ArticleQueryDto queryDto) {
        List<Article> articles = articleService.list(queryDto);
        int total = articleService.count(queryDto);
        // 构建分页结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("records", articles);
        return ResponseResult.success(result);
    }
}
