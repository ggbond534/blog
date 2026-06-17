package com.blog.service.impl;

import com.blog.dto.UserLoginDto;
import com.blog.dto.UserProfileDto;
import com.blog.dto.UserRegisterDto;
import com.blog.entity.User;
import com.blog.exception.BadRequestException;
import com.blog.mapper.UserMapper;
import com.blog.service.UserService;
import com.blog.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 用户服务实现类
 *
 * <p>实现 {@link UserService} 接口，包含注册和登录的核心逻辑。</p>
 *
 * <h3>安全说明</h3>
 * <ul>
 *   <li>密码使用 MD5 加密存储（生产环境建议升级为 BCrypt）</li>
 *   <li>登录成功返回的用户对象会清除密码字段，防止密码泄露</li>
 * </ul>
 *
 * @author blog
 * @version 1.0.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /** 头像上传存储路径（相对于静态资源根目录） */
    @Value("${upload.avatar.path:uploads/avatars/}")
    private String avatarPath;

    /**
     * 用户注册
     * 校验用户名唯一性 → MD5 加密密码 → 插入数据库
     * 昵称为空时默认使用用户名
     */
    @Override
    public User register(UserRegisterDto registerDto) {
        // 校验用户名是否已被占用
        if (userMapper.findByUsername(registerDto.getUsername()) != null) {
            throw new BadRequestException("用户名已存在");
        }

        // 构建用户实体并设置注册信息
        User user = new User();
        user.setUsername(registerDto.getUsername());
        // 密码使用 MD5 加密后存储
        user.setPassword(Md5Util.md5(registerDto.getPassword()));
        user.setEmail(registerDto.getEmail());
        user.setPhone(registerDto.getPhone());
        // 昵称：如果用户没有填写，默认使用用户名
        String nickname = registerDto.getNickname();
        user.setNickname(nickname != null && !nickname.trim().isEmpty() ? nickname.trim() : registerDto.getUsername());
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        userMapper.insert(user);
        return user;
    }

    /**
     * 用户登录
     * 查询用户 → MD5 加密输入密码 → 与数据库密文比对
     */
    @Override
    public User login(UserLoginDto loginDto) {
        // 根据用户名查找用户
        User user = userMapper.findByUsername(loginDto.getUsername());
        if (user == null) {
            throw new BadRequestException("用户不存在");
        }

        // 对输入的密码进行 MD5 加密后与数据库中的密文比对
        String password = Md5Util.md5(loginDto.getPassword());
        if (!password.equals(user.getPassword())) {
            throw new BadRequestException("用户名或密码错误");
        }

        // 登录成功后清除密码字段，防止泄露给前端
        user.setPassword(null);
        return user;
    }

    /**
     * 根据 ID 查询用户
     */
    @Override
    public User findById(Long id) {
        return userMapper.findById(id);
    }

    /**
     * 更新个人资料（昵称、个人简介）
     */
    @Override
    public User updateProfile(Long userId, UserProfileDto dto) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BadRequestException("用户不存在");
        }
        user.setNickname(dto.getNickname());
        user.setBio(dto.getBio());
        userMapper.updateProfile(user);
        // 重新查询以获取最新数据
        return userMapper.findById(userId);
    }

    /**
     * 上传头像
     * 校验文件类型 → 生成唯一文件名 → 保存到本地 → 更新数据库
     */
    @Override
    public String uploadAvatar(Long userId, MultipartFile file) {
        // 校验文件是否为空
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("头像文件不能为空");
        }

        // 校验文件类型（仅允许常见图片格式）
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg")
                && !contentType.equals("image/png")
                && !contentType.equals("image/gif")
                && !contentType.equals("image/webp"))) {
            throw new BadRequestException("头像仅支持 JPEG、PNG、GIF、WebP 格式");
        }

        try {
            // 确定存储目录：支持本地开发和云端部署
            Path uploadDir;
            if (avatarPath.startsWith("/") || avatarPath.contains(":")) {
                // 绝对路径（云端）
                uploadDir = Paths.get(avatarPath);
            } else {
                // 相对路径：本地开发用 static 目录，云端用 user.dir
                Path staticDir = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static");
                if (Files.exists(staticDir)) {
                    uploadDir = staticDir.resolve(avatarPath);
                } else {
                    // JAR 部署，用 user.dir 下的 uploads 目录
                    uploadDir = Paths.get(System.getProperty("user.dir"), avatarPath);
                }
            }
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // 删除旧头像文件（如果存在）
            User currentUser = userMapper.findById(userId);
            if (currentUser != null && currentUser.getAvatar() != null && !currentUser.getAvatar().isEmpty()) {
                String oldAvatar = currentUser.getAvatar();
                String oldFilename = oldAvatar.substring(oldAvatar.lastIndexOf("/") + 1);
                Path oldFilePath = uploadDir.resolve(oldFilename);
                Files.deleteIfExists(oldFilePath);
            }

            // 生成唯一文件名（保留原始扩展名）
            String originalFilename = file.getOriginalFilename();
            String extension = ".jpg";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + extension;

            // 保存新文件
            Path filePath = uploadDir.resolve(filename);
            file.transferTo(filePath.toFile());

            // 更新数据库
            String avatarUrl = "/" + avatarPath + filename;
            userMapper.updateAvatar(userId, avatarUrl);

            return avatarUrl;
        } catch (IOException e) {
            throw new RuntimeException("头像上传失败", e);
        }
    }
}
