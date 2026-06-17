package com.blog.mapper;

import com.blog.entity.LikeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 点赞记录 MyBatis Mapper 接口
 *
 * <p>提供点赞记录表（like_record）的增删查操作，
 * 通过 (articleId + userId) 组合条件进行查询和删除。</p>
 * <p>SQL 实现位于：resources/mybatis/mapper/LikeRecordMapper.xml</p>
 *
 * @author blog
 * @version 1.0.0
 */
@Mapper
public interface LikeRecordMapper {

    /** 插入点赞记录 */
    int insert(LikeRecord likeRecord);

    /** 根据文章ID和用户ID删除点赞记录（取消点赞） */
    int deleteByArticleIdAndUserId(@Param("articleId") Long articleId, @Param("userId") Long userId);

    /** 根据文章ID和用户ID查询点赞记录（判断是否已点赞） */
    LikeRecord findByArticleIdAndUserId(@Param("articleId") Long articleId, @Param("userId") Long userId);

    /** 查询某用户的所有点赞记录 */
    List<LikeRecord> findByUserId(@Param("userId") Long userId);
}
