/**
 * main.js — 全局公共脚本
 *
 * 提供所有页面共享的基础功能：
 *   1. request()  — 封装 fetch API 的统一请求函数
 *   2. showMessage() — Toast 消息提示（支持 success/error 样式）
 *   3. activatePageNavigation() — 导航栏高亮当前页面
 *
 * 每个 HTML 页面都应最先引入此脚本。
 */

/**
 * 统一的 HTTP 请求封装
 * 基于 fetch API，默认携带 credentials: 'include' 以发送 Cookie（维持 session），
 * 并自动设置 Content-Type 为 application/json。
 *
 * @param {string} url      - 请求 URL
 * @param {object} [options] - fetch 选项（与默认选项合并）
 * @returns {Promise<any>}   - 解析后的 JSON 响应数据
 */
function request(url, options = {}) {
    return fetch(url, Object.assign({
        headers: {'Content-Type': 'application/json'},
        credentials: 'include'  // 携带 Cookie 维持 session
    }, options)).then(res => res.json());
}

/**
 * 显示 Toast 消息提示
 * 支持三种类型：默认（深色）、success（绿色）、error（红色）
 * 固定在页面右下角，2.6 秒后自动消失。多次调用时会替换前一个 Toast。
 *
 * @param {string} text - 消息文本
 * @param {string} [type] - 消息类型：'success' | 'error' | 默认（深色）
 */
function showMessage(text, type) {
    const toastId = 'app-toast';
    let toast = document.getElementById(toastId);
    if (!toast) {
        toast = document.createElement('div');
        toast.id = toastId;
        toast.className = 'toast';
        document.body.appendChild(toast);
    }
    // 重置样式类并设置消息文本
    toast.className = 'toast';
    toast.textContent = text;

    // 根据类型添加对应的样式
    if (type === 'success') {
        toast.classList.add('success');
    } else if (type === 'error') {
        toast.classList.add('error');
    }

    // 触发显示
    requestAnimationFrame(() => {
        toast.classList.add('show');
    });

    // 自动隐藏
    clearTimeout(window.toastTimeout);
    window.toastTimeout = setTimeout(() => {
        toast.classList.remove('show');
    }, 2600);
}

/**
 * 激活当前页面的导航链接高亮
 * 读取 body 的 data-page 属性或当前 URL 路径，
 * 匹配导航栏中对应的链接并添加 active 样式类。
 */
window.activatePageNavigation = function () {
    const page = document.body.getAttribute('data-page') || window.location.pathname.split('/').pop();
    document.querySelectorAll('nav a').forEach(link => {
        if (link.getAttribute('data-page') === page || link.getAttribute('href').endsWith(page)) {
            link.classList.add('active');
        }
    });
};

// 自动激活导航高亮（DOM 已就绪，直接调用）
activatePageNavigation();

/* ================================================================
   用户状态管理与界面组件
   ================================================================ */

/** 全局当前登录用户数据 */
window.currentUser = null;

/**
 * 初始化用户状态
 * 从后端获取当前登录用户信息，渲染头像和下拉菜单。
 * 如果未登录（401），自动跳转到登录页。
 */
window.initUser = function () {
    fetch('/api/user/current', { credentials: 'include' })
        .then(res => res.json())
        .then(data => {
            if (data.code === 200) {
                window.currentUser = data.data;
                renderUserAvatar();
            } else {
                // 未登录，显示登录按钮
                showLoginPrompt();
            }
        })
        .catch(() => {
            showLoginPrompt();
        });
};

/** 未登录时在头像位置显示登录按钮 */
function showLoginPrompt() {
    const container = document.getElementById('userAvatarContainer');
    if (!container) return;
    container.innerHTML = '';
    container.style.cssText = 'display:flex;align-items:center;gap:10px;background:rgba(255,255,255,0.5);padding:6px 14px;border-radius:999px;border:1px solid rgba(59,130,246,0.2);';
    const loginLink = document.createElement('a');
    loginLink.href = '/index.html';
    loginLink.textContent = '🔑 请登录';
    loginLink.style.cssText = 'color:var(--blue-600);font-weight:600;font-size:0.9rem;text-decoration:none;white-space:nowrap;';
    container.appendChild(loginLink);
}

/**
 * 在 header 中渲染用户头像和昵称
 * 容器本身是 <a href="/profile.html">，点击即跳转个人中心
 */
function renderUserAvatar() {
    const container = document.getElementById('userAvatarContainer');
    if (!container) return;

    const user = window.currentUser;
    const avatarUrl = user.avatar || '';
    const nickname = user.nickname || user.username;
    const fallbackChar = (nickname || '?').charAt(0).toUpperCase();

    container.innerHTML = '';
    container.style.cssText = 'display:flex;align-items:center;gap:10px;cursor:pointer;background:rgba(255,255,255,0.5);padding:6px 14px;border-radius:999px;border:1px solid rgba(59,130,246,0.2);transition:all 0.25s ease;';
    container.title = '点击进入个人中心';

    // 鼠标悬停效果
    container.onmouseenter = function () {
        this.style.background = 'rgba(255,255,255,0.8)';
        this.style.boxShadow = 'var(--shadow-md)';
    };
    container.onmouseleave = function () {
        this.style.background = 'rgba(255,255,255,0.5)';
        this.style.boxShadow = 'none';
    };

    // 头像或占位符
    if (avatarUrl) {
        const img = document.createElement('img');
        img.src = avatarUrl;
        img.alt = nickname;
        img.className = 'avatar-img';
        img.style.pointerEvents = 'none';
        img.onerror = function () {
            this.style.display = 'none';
            const fb = document.createElement('span');
            fb.className = 'avatar-placeholder';
            fb.textContent = fallbackChar;
            this.parentNode.insertBefore(fb, this);
        };
        container.appendChild(img);
    } else {
        const placeholder = document.createElement('span');
        placeholder.className = 'avatar-placeholder';
        placeholder.textContent = fallbackChar;
        container.appendChild(placeholder);
    }

    // 昵称文字
    const nameSpan = document.createElement('span');
    nameSpan.textContent = nickname;
    nameSpan.style.cssText = 'font-weight:600;color:var(--text-primary);font-size:0.93rem;white-space:nowrap;pointer-events:none;';
    container.appendChild(nameSpan);

    // 小箭头表示可点击
    const arrow = document.createElement('span');
    arrow.textContent = '▾';
    arrow.style.cssText = 'font-size:0.7rem;color:var(--text-muted);pointer-events:none;';
    container.appendChild(arrow);
}

// 更新通知未读角标
function updateNotificationBadge() {
    fetch('/api/notification/unread', { credentials: 'include' })
        .then(res => res.json())
        .then(data => {
            const navNotif = document.getElementById('navNotification');
            if (navNotif && data.code === 200) {
                const count = data.data.count || 0;
                if (count > 0) {
                    navNotif.querySelector('span').innerHTML = '🔔 通知 <sup style="background:var(--danger);color:#fff;padding:1px 5px;border-radius:999px;font-size:0.7rem;">' + count + '</sup>';
                } else {
                    navNotif.querySelector('span').textContent = '🔔 通知';
                }
            }
        })
        .catch(() => {});
}

// 页面加载时更新通知角标
updateNotificationBadge();

// 导航栏退出登录链接（所有页面通用）
// 注意：不使用 DOMContentLoaded，因为 main.js 在 </body> 前加载，此时 DOM 已就绪
(function bindLogout() {
    const navLogout = document.getElementById('navLogout');
    if (navLogout) {
        navLogout.addEventListener('click', function (e) {
            e.preventDefault();
            fetch('/api/user/logout', { method: 'POST', credentials: 'include' })
                .then(res => res.json())
                .then(() => { window.location.href = '/index.html'; })
                .catch(() => { window.location.href = '/index.html'; });
        });
    }
})();
