package com.blog.service;

import com.blog.dto.UserLoginDto;
import com.blog.dto.UserRegisterDto;
import com.blog.dto.UserProfileDto;
import com.blog.entity.User;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户服务接口
 *
 * <p>定义用户相关的核心业务操作：</p>
 * <ul>
 *   <li>用户注册 — 校验用户名唯一性，MD5 加密密码后存储</li>
 *   <li>用户登录 — 校验用户名和密码（MD5 密文比对）</li>
 *   <li>用户查询 — 根据 ID 查询用户信息</li>
 *   <li>个人资料更新 — 更新昵称和个人简介</li>
 *   <li>头像上传 — 保存头像文件并更新数据库</li>
 * </ul>
 *
 * @author blog
 * @version 1.0.0
 */
public interface UserService {

    /**
     * 用户注册
     * @param registerDto 注册信息（用户名、密码、邮箱、电话、昵称）
     * @return 注册成功的用户实体（密码已加密）
     * @throws com.blog.exception.BadRequestException 用户名已存在时抛出
     */
    User register(UserRegisterDto registerDto);

    /**
     * 用户登录
     * @param loginDto 登录凭证（用户名、密码）
     * @return 登录成功的用户实体（密码字段已置为 null）
     * @throws com.blog.exception.BadRequestException 用户不存在或密码错误时抛出
     */
    User login(UserLoginDto loginDto);

    /**
     * 根据 ID 查询用户
     * @param id 用户 ID
     * @return 用户实体，不存在时返回 null
     */
    User findById(Long id);

    /**
     * 更新用户个人资料（昵称、个人简介）
     * @param userId 当前用户 ID
     * @param dto    个人资料数据
     * @return 更新后的用户实体
     */
    User updateProfile(Long userId, UserProfileDto dto);

    /**
     * 上传用户头像
     * @param userId 当前用户 ID
     * @param file   上传的头像图片文件
     * @return 头像的 URL 访问路径
     */
    String uploadAvatar(Long userId, MultipartFile file);
}
