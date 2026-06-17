package com.blog.mapper;

import com.blog.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户 MyBatis Mapper 接口
 *
 * <p>提供用户表（user）的增删改查操作。</p>
 * <p>SQL 实现位于：resources/mybatis/mapper/UserMapper.xml</p>
 *
 * @author blog
 * @version 1.0.0
 */
@Mapper
public interface UserMapper {

    /** 插入新用户（注册） */
    int insert(User user);

    /** 根据用户名查询用户（登录校验、唯一性检查） */
    User findByUsername(@Param("username") String username);

    /** 根据主键查询用户 */
    User findById(@Param("id") Long id);

    /** 更新用户信息 */
    int update(User user);

    /** 更新用户个人资料（昵称、个人简介） */
    int updateProfile(User user);

    /** 更新用户头像路径 */
    int updateAvatar(@Param("id") Long id, @Param("avatar") String avatar);

    /** 根据主键删除用户 */
    int deleteById(@Param("id") Long id);

    /** 查询所有用户列表 */
    List<User> findAll();
}
