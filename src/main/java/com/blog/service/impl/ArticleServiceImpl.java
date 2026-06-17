package com.blog.service.impl;

import com.blog.dto.ArticleDto;
import com.blog.dto.ArticleQueryDto;
import com.blog.entity.Article;
import com.blog.exception.BadRequestException;
import com.blog.mapper.ArticleMapper;
import com.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章服务实现类
 *
 * <p>实现 {@link ArticleService} 接口的全部方法，包含发布、更新、删除、详情和列表查询的核心逻辑。</p>
 *
 * <h3>关键业务规则</h3>
 * <ul>
 *   <li>发布文章时自动设置作者、阅读量(0)、点赞量(0)和时间戳</li>
 *   <li>摘要为空时自动截取正文前 120 个字符</li>
 *   <li>更新/删除前校验文章存在性及用户是否为作者</li>
 *   <li>查看详情时通过数据库原子操作递增阅读量</li>
 * </ul>
 *
 * @author blog
 * @version 1.0.0
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 发布新文章
     * 从 ArticleDto 构建 Article 实体并设置元数据后插入数据库
     */
    @Override
    public Article publish(ArticleDto articleDto, Long userId) {
        Article article = new Article();
        article.setUserId(userId);
        article.setCategoryId(articleDto.getCategoryId());
        article.setTitle(articleDto.getTitle());
        article.setContent(articleDto.getContent());
        article.setSummary(articleDto.getSummary());
        // 初始阅读量和点赞量为 0
        article.setViewCount(0);
        article.setLikeCount(0);
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());

        // 摘要为空时自动截取正文前 120 个字符
        if (article.getSummary() == null || article.getSummary().isEmpty()) {
            article.setSummary(article.getContent().length() > 120
                    ? article.getContent().substring(0, 120)
                    : article.getContent());
        }

        articleMapper.insert(article);
        return article;
    }

    /**
     * 更新文章
     * 先校验文章是否存在，再校验当前用户是否为作者，最后更新字段和修改时间
     */
    @Override
    public Article update(ArticleDto articleDto, Long userId) {
        Article exist = articleMapper.findById(articleDto.getId());
        if (exist == null) {
            throw new BadRequestException("文章不存在");
        }
        // 只有文章作者才可以编辑
        if (!exist.getUserId().equals(userId)) {
            throw new BadRequestException("无权限编辑该文章");
        }

        exist.setTitle(articleDto.getTitle());
        exist.setContent(articleDto.getContent());
        exist.setSummary(articleDto.getSummary());
        exist.setCategoryId(articleDto.getCategoryId());
        exist.setUpdateTime(LocalDateTime.now());

        articleMapper.update(exist);
        return exist;
    }

    /**
     * 删除文章
     * 校验文章存在性和用户权限后执行物理删除
     */
    @Override
    public void delete(Long id, Long userId) {
        Article exist = articleMapper.findById(id);
        if (exist == null) {
            throw new BadRequestException("文章不存在");
        }
        if (!exist.getUserId().equals(userId)) {
            throw new BadRequestException("无权限删除该文章");
        }
        articleMapper.deleteById(id);
    }

    /**
     * 获取文章详情
     * 查询文章并原子性地增加阅读量
     */
    @Override
    public Article detail(Long id) {
        Article article = articleMapper.findById(id);
        if (article == null) {
            throw new BadRequestException("文章不存在");
        }
        // 阅读量 +1（数据库层面原子自增，避免并发问题）
        articleMapper.incrementViewCount(id);
        return article;
    }

    /**
     * 分页查询文章列表（支持关键词搜索和分类筛选）
     */
    @Override
    public List<Article> list(ArticleQueryDto queryDto) {
        return articleMapper.queryArticles(queryDto);
    }

    /**
     * 统计符合条件的文章总数
     */
    @Override
    public int count(ArticleQueryDto queryDto) {
        return articleMapper.countArticles(queryDto);
    }
}
