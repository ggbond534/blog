/**
 * article-detail.js — 文章详情页脚本
 *
 * 功能：
 *   1. 从 URL 参数获取文章 ID
 *   2. 加载并渲染文章详情
 *   3. 加载并渲染评论列表
 *   4. 提交新评论
 *
 * 依赖：main.js（showMessage）
 */

// ====== DOM 元素 ======
const articleDetail = document.getElementById('articleDetail');
const commentContent = document.getElementById('commentContent');
const commentBtn = document.getElementById('commentBtn');
const commentList = document.getElementById('commentList');

// 从 URL 获取文章 ID
const query = new URLSearchParams(window.location.search);
const articleId = query.get('id');

/**
 * 格式化日期
 */
function formatDateTime(value) {
    if (!value) return '';
    return new Date(value).toLocaleString('zh-CN', {
        year: 'numeric', month: '2-digit', day: '2-digit',
        hour: '2-digit', minute: '2-digit'
    });
}

/**
 * 加载文章详情
 */
function loadArticle() {
    if (!articleId) {
        articleDetail.innerHTML = '<p>未找到文章 ID，请从文章列表进入。</p>';
        return;
    }
    fetch('/api/article/detail?id=' + articleId, {credentials: 'include'})
        .then(res => res.json())
        .then(data => {
            if (data.code === 200) {
                const item = data.data;
                const authorName = item.authorNickname || '匿名作者';
                const authorAvatar = item.authorAvatar || '';
                const authorInitial = authorName.charAt(0).toUpperCase();
                articleDetail.innerHTML = `
                    <h2 style="font-size:1.6rem;margin-bottom:14px;">${item.title}</h2>
                    <div class="article-author" style="margin-bottom:14px;">
                        ${authorAvatar
                            ? `<img src="${authorAvatar}" class="avatar-img-sm" alt="" onerror="this.style.display='none'">`
                            : `<span class="author-placeholder">${authorInitial}</span>`}
                        <span class="author-name">${authorName}</span>
                    </div>
                    <div class="article-meta">
                        <span>📂 分类 ${item.categoryId}</span>
                        <span>👀 ${item.viewCount || 0} 阅读</span>
                        <span>❤️ ${item.likeCount || 0} 点赞</span>
                        <span>🕐 ${formatDateTime(item.createTime)}</span>
                    </div>
                    <hr style="border:none;border-top:1px solid var(--border-light);margin:18px 0;">
                    <div style="line-height:1.9;color:var(--text-primary);font-size:1.02rem;">${item.content}</div>
                `;
            } else {
                articleDetail.innerHTML = '<p>文章加载失败。</p>';
                showMessage(data.message || '文章加载失败', 'error');
            }
        }).catch(() => {
            articleDetail.innerHTML = '<p>文章加载失败，请刷新重试。</p>';
            showMessage('网络异常，文章加载失败', 'error');
        });
}

// ====== 评论分页状态 ======
let commentPage = 1;
let commentPageSize = 5;
let commentTotal = 0;

/**
 * 加载评论列表（分页）
 */
function loadComments(page) {
    if (!articleId) {
        commentList.innerHTML = '<p>无法加载评论。</p>';
        return;
    }
    const p = page || commentPage;
    fetch('/api/comment/list?articleId=' + articleId + '&page=' + p + '&pageSize=' + commentPageSize, {credentials: 'include'})
        .then(res => res.json())
        .then(data => {
            if (data.code === 200) {
                commentList.innerHTML = '';
                const list = data.data.list || [];
                commentTotal = data.data.total || 0;
                commentPage = p;

                if (list.length === 0) {
                    commentList.innerHTML = '<p>💬 还没有评论，快来发表第一条吧。</p>';
                    return;
                }

                const currentUserId = window.currentUser ? window.currentUser.id : null;

                list.forEach(item => {
                    const div = document.createElement('div');
                    div.className = 'article-card';
                    const authorName = item.authorNickname || '匿名';
                    const authorAvatar = item.authorAvatar || '';
                    const authorInitial = authorName.charAt(0).toUpperCase();
                    const isOwner = currentUserId && item.userId === currentUserId;
                    div.innerHTML = `
                        <div class="article-author" style="border-top:none;padding-top:0;margin-bottom:12px;">
                            ${authorAvatar
                                ? `<img src="${authorAvatar}" class="avatar-img-sm" alt="" onerror="this.style.display='none'">`
                                : `<span class="author-placeholder">${authorInitial}</span>`}
                            <span class="author-name">${authorName}</span>
                            <span style="color:var(--text-muted);font-size:0.82rem;margin-left:auto;">${formatDateTime(item.createTime)}</span>
                            ${isOwner ? `<button class="btn-del-comment" data-id="${item.id}" style="background:none;border:none;color:var(--danger);cursor:pointer;font-size:0.85rem;padding:2px 8px;min-width:auto;box-shadow:none;margin-left:8px;">🗑️</button>` : ''}
                        </div>
                        <p style="color:var(--text-primary);">${item.content}</p>
                        ${item.parentId ? '<p class="comment-meta" style="margin-top:6px;">↩️ 回复</p>' : ''}
                    `;
                    commentList.appendChild(div);
                });

                // 渲染分页控件
                renderCommentPagination();
            } else {
                showMessage(data.message || '评论加载失败', 'error');
            }
        }).catch(() => showMessage('网络异常，评论加载失败', 'error'));
}

/** 渲染评论分页控件 */
function renderCommentPagination() {
    const totalPages = Math.max(1, Math.ceil(commentTotal / commentPageSize));
    let pagDiv = document.getElementById('commentPagination');
    if (!pagDiv) {
        pagDiv = document.createElement('div');
        pagDiv.id = 'commentPagination';
        pagDiv.className = 'pagination';
        pagDiv.style.marginTop = '16px';
        commentList.parentNode.insertBefore(pagDiv, commentList.nextSibling);
    }
    pagDiv.innerHTML = `
        <button id="cPrevPage" ${commentPage <= 1 ? 'disabled' : ''}>← 上一页</button>
        <span>第 ${commentPage} 页 / 共 ${totalPages} 页</span>
        <button id="cNextPage" ${commentPage >= totalPages ? 'disabled' : ''}>下一页 →</button>
    `;
    pagDiv.querySelector('#cPrevPage').addEventListener('click', () => loadComments(commentPage - 1));
    pagDiv.querySelector('#cNextPage').addEventListener('click', () => loadComments(commentPage + 1));
}

// ====== 提交评论 ======
commentBtn.addEventListener('click', () => {
    const content = commentContent.value.trim();
    if (!content) {
        showMessage('评论内容不能为空', 'error');
        return;
    }
    fetch('/api/comment/publish', {
        method: 'POST',
        credentials: 'include',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({articleId: Number(articleId), content})
    }).then(res => res.json())
      .then(data => {
          if (data.code === 200) {
              showMessage('评论成功', 'success');
              commentContent.value = '';
              loadComments(1);  // 发完评论跳到第一页
          } else {
              showMessage(data.message || '评论失败', 'error');
          }
      }).catch(() => showMessage('网络异常，请稍后再试', 'error'));
});

// ====== 删除评论（事件委托） ======
commentList.addEventListener('click', function (e) {
    const btn = e.target.closest('.btn-del-comment');
    if (!btn) return;
    if (!confirm('确定删除这条评论吗？')) return;
    const id = btn.dataset.id;
    fetch('/api/comment/delete?id=' + id, { method: 'POST', credentials: 'include' })
        .then(res => res.json())
        .then(data => {
            if (data.code === 200) {
                showMessage('已删除', 'success');
                loadComments(commentPage);
            } else {
                showMessage(data.message || '删除失败', 'error');
            }
        });
});

// ====== 初始化 ======
loadArticle();
loadComments(1);
