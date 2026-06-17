/**
 * notifications.js — 消息通知页脚本
 */
const notificationList = document.getElementById('notificationList');
const btnReadAll = document.getElementById('btnReadAll');

function loadNotifications() {
    fetch('/api/notification/list', { credentials: 'include' })
        .then(res => res.json())
        .then(data => {
            notificationList.innerHTML = '';
            if (data.code !== 200) {
                notificationList.innerHTML = '<p>加载失败</p>';
                return;
            }
            const list = data.data.list || [];
            if (list.length === 0) {
                notificationList.innerHTML = '<p style="text-align:center;color:var(--text-muted);padding:40px;">📭 暂无消息通知</p>';
                return;
            }
            list.forEach(item => {
                const card = document.createElement('div');
                card.className = 'article-card';
                card.style.cssText = item.isRead ? 'opacity:0.65;' : 'border-left:3px solid var(--blue-500);';
                card.innerHTML = `
                    <div style="display:flex;justify-content:space-between;align-items:start;gap:12px;">
                        <div style="flex:1;">
                            <p style="color:var(--text-primary);font-size:0.95rem;margin-bottom:6px;">${item.message}</p>
                            <p style="color:var(--text-muted);font-size:0.82rem;">${formatTime(item.createTime)}</p>
                        </div>
                        <div style="display:flex;flex-direction:column;align-items:flex-end;gap:8px;">
                            ${!item.isRead ? '<span style="background:var(--blue-500);color:#fff;padding:2px 8px;border-radius:999px;font-size:0.72rem;">新</span>' : ''}
                            <a href="/article-detail.html?id=${item.articleId}" style="color:var(--blue-600);font-size:0.85rem;text-decoration:none;">查看 →</a>
                        </div>
                    </div>
                `;
                // 点击卡片标记已读
                card.addEventListener('click', function () {
                    if (!item.isRead) {
                        fetch('/api/notification/read?id=' + item.id, { method: 'POST', credentials: 'include' })
                            .then(() => {
                                item.isRead = 1;
                                card.style.opacity = '0.65';
                                card.style.borderLeft = 'none';
                                const badge = card.querySelector('span[style]');
                                if (badge && badge.textContent === '新') badge.remove();
                            });
                    }
                });
                notificationList.appendChild(card);
            });
        });
}

function formatTime(value) {
    if (!value) return '';
    return new Date(value).toLocaleString('zh-CN', {
        year: 'numeric', month: '2-digit', day: '2-digit',
        hour: '2-digit', minute: '2-digit'
    });
}

btnReadAll.addEventListener('click', function () {
    fetch('/api/notification/read-all', { method: 'POST', credentials: 'include' })
        .then(res => res.json())
        .then(data => {
            if (data.code === 200) {
                showMessage('已全部标记为已读', 'success');
                loadNotifications();
            }
        });
});

loadNotifications();
