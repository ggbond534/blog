/**
 * register.js — 用户注册页脚本
 *
 * 功能：
 *   1. 用户名 + 密码 + 邮箱 + 电话注册
 *   2. 支持回车键快捷提交
 *   3. 注册成功后自动跳转到登录页
 *
 * 依赖：main.js（showMessage）
 */

// ====== DOM 元素 ======
const registerBtn = document.getElementById('registerBtn');
const registerUsername = document.getElementById('username');
const registerPassword = document.getElementById('password');
const registerEmail = document.getElementById('email');
const registerPhone = document.getElementById('phone');

/**
 * 处理注册请求
 * 校验输入 → 发送 POST → 成功后跳转登录页
 */
function handleRegister() {
    const username = registerUsername.value.trim();
    const password = registerPassword.value.trim();
    const email = registerEmail.value.trim();
    const phone = registerPhone.value.trim();

    if (!username || !password) {
        showMessage('用户名和密码不能为空', 'error');
        return;
    }

    fetch('/api/user/register', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        credentials: 'include',
        body: JSON.stringify({username, password, email, phone})
    }).then(res => res.json())
      .then(data => {
          if (data.code === 200) {
              showMessage('注册成功，请登录', 'success');
              setTimeout(() => window.location.href = '/index.html', 800);
          } else {
              showMessage(data.message || '注册失败', 'error');
          }
      }).catch(() => showMessage('网络异常，请稍后再试', 'error'));
}

// 点击注册
registerBtn.addEventListener('click', handleRegister);

// 回车快捷提交
document.addEventListener('keydown', function (event) {
    if (event.key === 'Enter' && document.activeElement && [registerUsername, registerPassword, registerEmail, registerPhone].includes(document.activeElement)) {
        event.preventDefault();
        handleRegister();
    }
});
