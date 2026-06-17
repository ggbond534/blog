package com.blog.controller;

import com.blog.common.ResponseResult;
import com.blog.dto.UserLoginDto;
import com.blog.dto.UserProfileDto;
import com.blog.dto.UserRegisterDto;
import com.blog.entity.User;
import com.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器 — 处理用户认证相关的 HTTP 请求
 *
 * <p>提供以下 REST API 端点（基础路径 /api/user）：</p>
 * <ul>
 *   <li><b>POST /register</b> — 用户注册（公开）</li>
 *   <li><b>POST /login</b> — 用户登录（公开）</li>
 *   <li><b>GET /current</b> — 获取当前登录用户信息</li>
 *   <li><b>POST /logout</b> — 退出登录</li>
 * </ul>
 *
 * <p><b>安全说明：</b>登录成功后用户信息存储在 HttpSession 中；
 * 注册和登录返回的用户对象已清除密码字段。</p>
 *
 * @author blog
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册（公开接口）
     * 注册成功后返回的用户对象已清除密码字段
     *
     * @param registerDto 注册信息（用户名、密码、邮箱、电话）
     * @return 包含新用户实体的成功响应
     */
    @PostMapping("/register")
    public ResponseResult<User> register(@Validated @RequestBody UserRegisterDto registerDto) {
        User user = userService.register(registerDto);
        // 清除密码后再返回给前端
        user.setPassword(null);
        return ResponseResult.success(user);
    }

    /**
     * 用户登录（公开接口）
     * 登录成功后会将用户信息存入 HttpSession，后续请求可通过拦截器认证
     *
     * @param loginDto 登录凭证（用户名、密码）
     * @param session  当前会话（登录成功后写入用户信息）
     * @return 包含用户实体的成功响应（密码已清除）
     */
    @PostMapping("/login")
    public ResponseResult<User> login(@Validated @RequestBody UserLoginDto loginDto, HttpSession session) {
        User user = userService.login(loginDto);
        // 将用户信息存入 session，供拦截器和其他接口使用
        session.setAttribute("user", user);
        return ResponseResult.success(user);
    }

    /**
     * 获取当前登录用户信息
     * 从未过期的 session 中恢复用户登录状态
     *
     * @param session 当前会话
     * @return 包含用户实体的成功响应；未登录时返回 401
     */
    @GetMapping("/current")
    public ResponseResult<User> current(HttpSession session) {
        User user = session == null ? null : (User) session.getAttribute("user");
        if (user == null) {
            return ResponseResult.fail(401, "未登录");
        }
        // 清除密码敏感信息
        user.setPassword(null);
        return ResponseResult.success(user);
    }

    /**
     * 退出登录
     * 销毁当前 HttpSession，清除所有会话数据
     *
     * @param session 当前会话
     * @return 退出成功的响应
     */
    @PostMapping("/logout")
    public ResponseResult<?> logout(HttpSession session) {
        if (session != null) {
            // 使 session 失效，清除服务端会话数据
            session.invalidate();
        }
        return ResponseResult.success("退出成功", null);
    }

    /**
     * 更新个人资料（昵称、个人简介）
     * 需登录，从 session 中获取当前用户 ID
     *
     * @param profileDto 个人资料数据
     * @param session    当前会话
     * @return 包含更新后用户实体的成功响应
     */
    @PostMapping("/profile/update")
    public ResponseResult<User> updateProfile(@RequestBody UserProfileDto profileDto, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        User updated = userService.updateProfile(sessionUser.getId(), profileDto);
        // 同步更新 session 中的用户信息
        session.setAttribute("user", updated);
        updated.setPassword(null);
        return ResponseResult.success(updated);
    }

    /**
     * 上传用户头像（需登录）
     * 接收 multipart/form-data 中的 file 字段
     *
     * @param file    上传的头像图片
     * @param session 当前会话
     * @return 包含头像 URL 的成功响应
     */
    @PostMapping("/avatar/upload")
    public ResponseResult<Map<String, String>> uploadAvatar(@RequestParam("file") MultipartFile file, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        String avatarUrl = userService.uploadAvatar(sessionUser.getId(), file);
        // 同步更新 session 中的用户头像
        sessionUser.setAvatar(avatarUrl);
        session.setAttribute("user", sessionUser);
        Map<String, String> result = new HashMap<>();
        result.put("avatar", avatarUrl);
        return ResponseResult.success(result);
    }
}
