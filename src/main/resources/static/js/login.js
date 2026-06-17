/**
 * login.js — 用户登录页脚本
 *
 * 功能：
 *   1. 用户名 + 密码登录
 *   2. 支持回车键快捷提交
 *   3. 登录成功后自动跳转到文章列表页
 *
 * 依赖：main.js（showMessage）
 */

// ====== DOM 元素 ======
const loginBtn = document.getElementById('loginBtn');
const loginUsername = document.getElementById('username');
const loginPassword = document.getElementById('password');

/**
 * 处理登录请求
 * 前端校验输入 → 发送 POST 请求 → 成功后跳转
 */
function handleLogin() {
    const username = loginUsername.value.trim();
    const password = loginPassword.value.trim();

    if (!username || !password) {
        showMessage('用户名和密码不能为空', 'error');
        return;
    }

    fetch('/api/user/login', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        credentials: 'include',
        body: JSON.stringify({username, password})
    }).then(res => res.json())
      .then(data => {
          if (data.code === 200) {
              showMessage('登录成功，正在跳转...', 'success');
              setTimeout(() => window.location.href = '/article.html', 800);
          } else {
              showMessage(data.message || '登录失败', 'error');
          }
      }).catch(() => showMessage('网络异常，请稍后再试', 'error'));
}

// 点击登录
loginBtn.addEventListener('click', handleLogin);

// 回车快捷提交
document.addEventListener('keydown', function (event) {
    if (event.key === 'Enter' && document.activeElement && (document.activeElement === loginUsername || document.activeElement === loginPassword)) {
        event.preventDefault();
        handleLogin();
    }
});
