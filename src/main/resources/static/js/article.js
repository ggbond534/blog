/**
 * article.js — 文章列表页脚本
 *
 * 功能：
 *   1. 加载分类数据填充筛选下拉框
 *   2. 分页加载文章列表（支持关键词搜索和分类筛选）
 *   3. 渲染文章卡片（标题、分类、统计、摘要）
 *   4. 分页控件（上一页/下一页）
 *   5. 跳转到文章详情页和发布页
 *
 * 依赖：main.js（request、showMessage）
 */

// ====== DOM 元素 ======
const articleList = document.getElementById('articleList');
const searchInput = document.getElementById('searchInput');
const categoryFilter = document.getElementById('categoryFilter');
const searchBtn = document.getElementById('searchBtn');
const publishBtn = document.getElementById('publishBtn');
const prevPage = document.getElementById('prevPage');
const nextPage = document.getElementById('nextPage');
const pageInfo = document.getElementById('pageInfo');

// ====== 状态 ======
let pageNum = 1;
let pageSize = 5;
let total = 0;
let categoryMap = new Map();
let likedArticleIds = new Set(); // 当前用户已点赞的文章ID集合

/**
 * 格式化日期为中文格式
 * @param {string} value - ISO 日期字符串
 * @returns {string} 如 "2026/06/16 14:30"
 */
function formatDateTime(value) {
    if (!value) return '';
    const date = new Date(value);
    return date.toLocaleString('zh-CN', {
        year: 'numeric', month: '2-digit', day: '2-digit',
        hour: '2-digit', minute: '2-digit'
    });
}

/**
 * 加载分类数据
 * @returns {Promise<boolean>}
 */
function loadCategories() {
    return fetch('/api/category/list', {credentials: 'include'})
        .then(res => res.json())
        .then(data => {
            if (data.code === 200) {
                categoryFilter.innerHTML = '<option value="">全部分类</option>';
                categoryMap.clear();
                data.data.forEach(item => {
                    categoryMap.set(item.id, item.name);
                    const option = document.createElement('option');
                    option.value = item.id;
                    option.innerText = item.name;
                    categoryFilter.appendChild(option);
                });
                return true;
            }
            showMessage(data.message || '分类加载失败', 'error');
            return false;
        }).catch(() => {
            showMessage('网络异常，无法加载分类', 'error');
            return false;
        });
}

/**
 * 更新分页控件状态
 */
function updatePagination() {
    const totalPages = Math.max(1, Math.ceil(total / pageSize));
    pageInfo.innerText = total === 0 ? '暂无文章' : `第 ${pageNum} 页 / 共 ${totalPages} 页`;
    prevPage.disabled = pageNum <= 1;
    nextPage.disabled = pageNum >= totalPages;
}

/**
 * 渲染单篇文章卡片
 * @param {object} item - 文章数据
 * @returns {HTMLElement}
 */
function renderArticleCard(item) {
    const categoryName = categoryMap.get(item.categoryId) || `分类 ${item.categoryId || '未知'}`;
    const summary = item.summary || (item.content ? item.content.substring(0, 120) + '…' : '暂无摘要');
    const authorName = item.authorNickname || '匿名作者';
    const authorAvatar = item.authorAvatar || '';
    const authorInitial = authorName.charAt(0).toUpperCase();
    const isLiked = likedArticleIds.has(item.id);
    const card = document.createElement('div');
    card.className = 'article-card';
    card.innerHTML = `
        <div>
            <h3>${item.title || '未命名文章'}</h3>
            <div class="article-meta">
                <span>📂 ${categoryName}</span>
                <span>👀 ${item.viewCount || 0} 阅读</span>
                <span>❤️ ${item.likeCount || 0} 点赞</span>
                <span>🕐 ${formatDateTime(item.createTime)}</span>
            </div>
            <p style="color:var(--text-secondary);line-height:1.7;">${summary}</p>
        </div>
        <div class="article-author">
            ${authorAvatar
                ? `<img src="${authorAvatar}" class="avatar-img-sm" alt="" onerror="this.style.display='none'">`
                : `<span class="author-placeholder">${authorInitial}</span>`}
            <span class="author-name">${authorName}</span>
        </div>
        <div class="card-actions">
            <button class="btn-like ${isLiked ? 'liked' : ''}" data-article-id="${item.id}">
                ${isLiked ? '❤️' : '🤍'} <span class="like-count">${item.likeCount || 0}</span>
            </button>
            <button onclick="viewArticle(${item.id})">查看详情 →</button>
        </div>
    `;
    return card;
}

/**
 * 加载文章列表
 */
function loadArticles() {
    const params = new URLSearchParams();
    params.set('pageNum', pageNum);
    params.set('pageSize', pageSize);
    const keyword = searchInput.value.trim();
    if (keyword) params.set('keyword', keyword);
    const categoryId = categoryFilter.value;
    if (categoryId) params.set('categoryId', categoryId);

    fetch('/api/article/list?' + params.toString(), {credentials: 'include'})
        .then(res => res.json())
        .then(data => {
            articleList.innerHTML = '';
            if (data.code !== 200) {
                articleList.innerHTML = '<p>加载文章失败，请稍后重试。</p>';
                showMessage(data.message || '文章加载失败', 'error');
                total = 0;
                updatePagination();
                return;
            }

            total = Number(data.data.total || 0);
            updatePagination();

            const records = Array.isArray(data.data.records) ? data.data.records : [];
            if (records.length === 0) {
                articleList.innerHTML = '<p>📭 当前没有符合条件的文章，试试更换关键词或分类。</p>';
                return;
            }

            records.forEach(item => articleList.appendChild(renderArticleCard(item)));
        })
        .catch(() => {
            articleList.innerHTML = '<p>文章加载失败，请检查网络后重试。</p>';
            showMessage('网络异常，文章加载失败', 'error');
            total = 0;
            updatePagination();
        });
}

/** 跳转文章详情 */
function viewArticle(id) {
    window.location.href = `/article-detail.html?id=${id}`;
}

// ====== 事件绑定 ======
searchBtn.addEventListener('click', () => { pageNum = 1; loadArticles(); });
prevPage.addEventListener('click', () => { if (pageNum > 1) { pageNum--; loadArticles(); } });
nextPage.addEventListener('click', () => {
    const totalPages = Math.max(1, Math.ceil(total / pageSize));
    if (pageNum < totalPages) { pageNum++; loadArticles(); }
});
publishBtn.addEventListener('click', () => { window.location.href = '/publish.html'; });

// ====== 点赞切换（事件委托） ======
articleList.addEventListener('click', function (e) {
    const btn = e.target.closest('.btn-like');
    if (!btn) return;
    const articleId = parseInt(btn.dataset.articleId);
    toggleLike(articleId, btn);
});

function toggleLike(articleId, btn) {
    fetch('/api/like/toggle', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        credentials: 'include',
        body: 'articleId=' + articleId
    })
        .then(res => res.json())
        .then(data => {
            if (data.code === 200) {
                const liked = data.data.liked;
                if (liked) {
                    likedArticleIds.add(articleId);
                    btn.classList.add('liked');
                    btn.innerHTML = '❤️ <span class="like-count">' + (parseInt(btn.querySelector('.like-count').textContent) + 1) + '</span>';
                } else {
                    likedArticleIds.delete(articleId);
                    btn.classList.remove('liked');
                    btn.innerHTML = '🤍 <span class="like-count">' + Math.max(0, parseInt(btn.querySelector('.like-count').textContent) - 1) + '</span>';
                }
            }
        });
}

/** 加载当前用户的点赞列表 */
function loadMyLikes() {
    return fetch('/api/like/my-likes', { credentials: 'include' })
        .then(res => res.json())
        .then(data => {
            if (data.code === 200 && data.data) {
                likedArticleIds = new Set(data.data);
            }
        })
        .catch(() => {});
}

// ====== 初始化 ======
loadMyLikes().then(() => loadCategories().finally(loadArticles));
