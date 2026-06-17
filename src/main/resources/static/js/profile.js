/**
 * profile.js — 个人中心页脚本
 *
 * 功能：
 *   1. 显示当前头像（大图）
 *   2. 选择本地图片上传头像（自动替换旧头像）
 *   3. 编辑昵称和个人简介
 *   4. 显示账号基本信息
 *
 * 依赖：main.js（showMessage, window.currentUser）
 */

// ====== DOM 元素 ======
const profileAvatarArea = document.getElementById('profileAvatarArea');
const avatarFileInput = document.getElementById('avatarFileInput');
const avatarStatus = document.getElementById('avatarStatus');
const profileNickname = document.getElementById('profileNickname');
const profileBio = document.getElementById('profileBio');
const btnSaveProfile = document.getElementById('btnSaveProfile');

/** 当前用户数据在 window.currentUser 中，由 initUser() 填充 */

/**
 * 初始化个人中心页面
 * 从 window.currentUser 加载数据并渲染
 */
function initProfilePage() {
    const user = window.currentUser;
    if (!user) {
        // initUser 还没完成，等一会儿再试
        setTimeout(initProfilePage, 200);
        return;
    }

    // 渲染头像
    renderProfileAvatar(user);

    // 填充表单
    profileNickname.value = user.nickname || '';
    profileBio.value = user.bio || '';

    // 账号信息
    document.getElementById('infoUsername').textContent = user.username || '-';
    document.getElementById('infoEmail').textContent = user.email || '未设置';
    document.getElementById('infoPhone').textContent = user.phone || '未设置';
    document.getElementById('infoCreateTime').textContent = formatDate(user.createTime);
}

/**
 * 渲染头像（大号，用于个人中心）
 */
function renderProfileAvatar(user) {
    const avatarUrl = user.avatar || '';
    const nickname = user.nickname || user.username;
    const fallbackChar = nickname.charAt(0).toUpperCase();

    if (avatarUrl) {
        profileAvatarArea.innerHTML = `
            <img src="${avatarUrl}?t=${Date.now()}" class="avatar-img-lg" id="profileAvatarImg"
                 alt="头像" onerror="this.style.display='none';this.nextElementSibling.style.display='inline-flex';">
            <span class="avatar-placeholder" style="width:96px;height:96px;font-size:2rem;display:none;">${fallbackChar}</span>
        `;
    } else {
        profileAvatarArea.innerHTML = `
            <span class="avatar-placeholder" style="width:96px;height:96px;font-size:2rem;">${fallbackChar}</span>
        `;
    }
}

/**
 * 格式化日期
 */
function formatDate(value) {
    if (!value) return '-';
    return new Date(value).toLocaleString('zh-CN', {
        year: 'numeric', month: '2-digit', day: '2-digit',
        hour: '2-digit', minute: '2-digit'
    });
}

// ====== 头像上传 ======
avatarFileInput.addEventListener('change', function () {
    const file = this.files[0];
    if (!file) return;

    // 检查文件类型
    const allowedTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];
    if (!allowedTypes.includes(file.type)) {
        showMessage('仅支持 JPEG、PNG、GIF、WebP 格式', 'error');
        return;
    }

    // 检查文件大小（5MB）
    if (file.size > 5 * 1024 * 1024) {
        showMessage('文件大小不能超过 5MB', 'error');
        return;
    }

    // 客户端预览
    const reader = new FileReader();
    reader.onload = function (e) {
        const img = profileAvatarArea.querySelector('#profileAvatarImg');
        if (img) {
            img.src = e.target.result;
        } else {
            profileAvatarArea.innerHTML = `<img src="${e.target.result}" class="avatar-img-lg" id="profileAvatarImg" alt="预览">`;
        }
    };
    reader.readAsDataURL(file);

    // 上传到服务器
    avatarStatus.textContent = '⏳ 正在上传...';
    avatarStatus.style.color = 'var(--text-muted)';

    const formData = new FormData();
    formData.append('file', file);

    fetch('/api/user/avatar/upload', {
        method: 'POST',
        credentials: 'include',
        body: formData
    })
        .then(res => res.json())
        .then(data => {
            if (data.code === 200) {
                avatarStatus.textContent = '✅ 头像更新成功！';
                avatarStatus.style.color = 'var(--success)';
                // 更新全局用户数据和 header 头像
                window.currentUser.avatar = data.data.avatar;
                if (typeof renderUserAvatar === 'function') {
                    renderUserAvatar();
                }
            } else {
                avatarStatus.textContent = '❌ ' + (data.message || '上传失败');
                avatarStatus.style.color = 'var(--danger)';
            }
        })
        .catch(() => {
            avatarStatus.textContent = '❌ 网络异常，上传失败';
            avatarStatus.style.color = 'var(--danger)';
        });
});

// ====== 保存个人资料 ======
btnSaveProfile.addEventListener('click', function () {
    const nickname = profileNickname.value.trim();
    const bio = profileBio.value.trim();

    if (!nickname) {
        showMessage('昵称不能为空', 'error');
        return;
    }

    btnSaveProfile.disabled = true;
    btnSaveProfile.textContent = '⏳ 保存中...';

    fetch('/api/user/profile/update', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ nickname, bio })
    })
        .then(res => res.json())
        .then(data => {
            btnSaveProfile.disabled = false;
            btnSaveProfile.textContent = '💾 保存资料';
            if (data.code === 200) {
                showMessage('资料已保存', 'success');
                // 更新全局用户数据
                window.currentUser = data.data;
                // 刷新 header 中的头像/昵称
                if (typeof renderUserAvatar === 'function') {
                    renderUserAvatar();
                }
            } else {
                showMessage(data.message || '保存失败', 'error');
            }
        })
        .catch(() => {
            btnSaveProfile.disabled = false;
            btnSaveProfile.textContent = '💾 保存资料';
            showMessage('网络异常', 'error');
        });
});

// ====== 页面初始化 ======
// 等待 initUser() 完成后再初始化
let checkCount = 0;
const checkInterval = setInterval(function () {
    checkCount++;
    if (window.currentUser) {
        clearInterval(checkInterval);
        initProfilePage();
    } else if (checkCount > 25) {
        // 超过5秒还没加载，可能未登录
        clearInterval(checkInterval);
        profileAvatarArea.innerHTML = '<p style="color:var(--text-muted);">请先 <a href="/index.html">登录</a></p>';
    }
}, 200);
