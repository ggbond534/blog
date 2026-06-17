package com.blog.controller;

import com.blog.common.ResponseResult;
import com.blog.entity.Category;
import com.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类控制器 — 处理分类相关的 HTTP 请求
 *
 * <p>提供以下 REST API 端点（基础路径 /api/category）：</p>
 * <ul>
 *   <li><b>GET /list</b> — 分类列表（公开）</li>
 *   <li><b>POST /create</b> — 创建分类（需登录）</li>
 *   <li><b>POST /update</b> — 更新分类（需登录）</li>
 *   <li><b>POST /delete</b> — 删除分类（需登录）</li>
 * </ul>
 *
 * @author blog
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取所有分类列表（公开接口）
     * 前端文章列表页、发布页、分类管理页均依赖此接口
     *
     * @return 包含分类列表的成功响应
     */
    @GetMapping("/list")
    public ResponseResult<List<Category>> list() {
        return ResponseResult.success(categoryService.list());
    }

    /**
     * 创建新分类（需登录）
     *
     * @param category 分类数据（名称、描述、分组、排序）
     * @return 包含新分类实体的成功响应
     */
    @PostMapping("/create")
    public ResponseResult<Category> create(@Validated @RequestBody Category category) {
        return ResponseResult.success(categoryService.create(category));
    }

    /**
     * 更新分类信息（需登录）
     *
     * @param category 包含 id 的完整分类数据
     * @return 包含更新后分类实体的成功响应
     */
    @PostMapping("/update")
    public ResponseResult<Category> update(@Validated @RequestBody Category category) {
        return ResponseResult.success(categoryService.update(category));
    }

    /**
     * 删除分类（需登录）
     *
     * @param id 待删除的分类 ID
     * @return 包含删除成功消息的响应
     */
    @PostMapping("/delete")
    public ResponseResult<?> delete(@RequestParam Long id) {
        categoryService.delete(id);
        return ResponseResult.success("删除成功", null);
    }
}
