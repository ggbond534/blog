/**
 * auth-page.js — 登录/注册页面脚本
 *
 * 功能：
 *   1. 登录/注册表单切换
 *   2. 密码可见性切换
 *   3. 登录请求 → 成功后跳转 home.html
 *   4. 注册请求 → 成功后切换到登录
 *   5. 点阵地球动画
 *   6. 回车快捷提交
 *
 * 依赖：main.js（showMessage）
 */

// ====== 表单切换 ======
const loginForm = document.getElementById('loginForm');
const registerForm = document.getElementById('registerForm');

document.getElementById('switchToRegister').addEventListener('click', () => {
    loginForm.classList.remove('active');
    registerForm.classList.add('active');
});

document.getElementById('switchToLogin').addEventListener('click', () => {
    registerForm.classList.remove('active');
    loginForm.classList.add('active');
});

// ====== 密码可见性切换 ======
document.getElementById('toggleLoginPw').addEventListener('click', function () {
    const input = document.getElementById('loginPassword');
    const isPassword = input.type === 'password';
    input.type = isPassword ? 'text' : 'password';
    this.textContent = isPassword ? '🙈' : '👁️';
});

document.getElementById('toggleRegPw').addEventListener('click', function () {
    const input = document.getElementById('regPassword');
    const isPassword = input.type === 'password';
    input.type = isPassword ? 'text' : 'password';
    this.textContent = isPassword ? '🙈' : '👁️';
});

// ====== 登录 ======
function handleLogin() {
    const username = document.getElementById('loginUsername').value.trim();
    const password = document.getElementById('loginPassword').value.trim();

    if (!username || !password) {
        showMessage('用户名和密码不能为空', 'error');
        return;
    }

    fetch('/api/user/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ username, password })
    }).then(res => res.json())
      .then(data => {
          if (data.code === 200) {
              showMessage('登录成功，欢迎回来！', 'success');
              setTimeout(() => { window.location.href = '/home.html'; }, 600);
          } else {
              showMessage(data.message || '登录失败', 'error');
          }
      }).catch(() => showMessage('网络异常，请稍后再试', 'error'));
}

document.getElementById('loginBtn').addEventListener('click', handleLogin);

// ====== 注册 ======
function handleRegister() {
    const username = document.getElementById('regUsername').value.trim();
    const password = document.getElementById('regPassword').value.trim();
    const email = document.getElementById('regEmail').value.trim();
    const phone = document.getElementById('regPhone').value.trim();

    if (!username || !password) {
        showMessage('用户名和密码不能为空', 'error');
        return;
    }

    fetch('/api/user/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ username, password, email, phone })
    }).then(res => res.json())
      .then(data => {
          if (data.code === 200) {
              showMessage('注册成功，请登录', 'success');
              document.getElementById('regUsername').value = '';
              document.getElementById('regPassword').value = '';
              document.getElementById('regEmail').value = '';
              document.getElementById('regPhone').value = '';
              registerForm.classList.remove('active');
              loginForm.classList.add('active');
          } else {
              showMessage(data.message || '注册失败', 'error');
          }
      }).catch(() => showMessage('网络异常，请稍后再试', 'error'));
}

document.getElementById('registerBtn').addEventListener('click', handleRegister);

// ====== 回车快捷提交 ======
document.addEventListener('keydown', function (e) {
    if (e.key !== 'Enter') return;
    if (loginForm.classList.contains('active')) {
        handleLogin();
    } else if (registerForm.classList.contains('active')) {
        handleRegister();
    }
});

// ====== 点阵地球动画 ======
(function () {
    const canvas = document.getElementById('dotMap');
    if (!canvas) return;

    const parent = canvas.parentElement;
    let animId;

    function resize() {
        canvas.width = parent.clientWidth;
        canvas.height = parent.clientHeight;
    }

    resize();
    window.addEventListener('resize', resize);

    const ctx = canvas.getContext('2d');
    // 预生成点阵
    const dots = [];
    const gap = 10;
    const w = canvas.width || 500;
    const h = canvas.height || 600;

    for (let x = 0; x < w; x += gap) {
        for (let y = 0; y < h; y += gap) {
            const r = Math.random();
            if (r > 0.45) {
                dots.push({
                    x, y,
                    r: 0.8 + Math.random() * 0.6,
                    baseAlpha: 0.15 + Math.random() * 0.25,
                    phase: Math.random() * Math.PI * 2
                });
            }
        }
    }

    // 航线
    const routes = [
        { x1: 80, y1: 180, x2: 200, y2: 120 },
        { x1: 220, y1: 100, x2: 280, y2: 160 },
        { x1: 120, y1: 260, x2: 250, y2: 200 },
        { x1: 300, y1: 140, x2: 200, y2: 280 },
        { x1: 60, y1: 100, x2: 160, y2: 240 },
        { x1: 260, y1: 80, x2: 340, y2: 200 },
    ];

    let startTime = Date.now();

    function draw() {
        if (!canvas || !ctx) return;
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        const el = (Date.now() - startTime) / 1000;

        // 画点阵
        dots.forEach(dot => {
            const pulse = Math.sin(el * 1.5 + dot.phase) * 0.08 + 0.08;
            const alpha = dot.baseAlpha + pulse;
            ctx.beginPath();
            ctx.arc(dot.x, dot.y, dot.r, 0, Math.PI * 2);
            ctx.fillStyle = `rgba(37,99,235,${alpha.toFixed(2)})`;
            ctx.fill();
        });

        // 画航线
        routes.forEach((route, i) => {
            const offset = i * 1.2;
            const t = ((el + offset) % 6) / 6;
            const cx = route.x1 + (route.x2 - route.x1) * t;
            const cy = route.y1 + (route.y2 - route.y1) * t;

            // 线
            ctx.beginPath();
            ctx.moveTo(route.x1, route.y1);
            ctx.lineTo(cx, cy);
            ctx.strokeStyle = 'rgba(59,130,246,0.3)';
            ctx.lineWidth = 1;
            ctx.stroke();

            // 起点
            ctx.beginPath();
            ctx.arc(route.x1, route.y1, 2.5, 0, Math.PI * 2);
            ctx.fillStyle = 'rgba(37,99,235,0.5)';
            ctx.fill();

            // 动点
            ctx.beginPath();
            ctx.arc(cx, cy, 3, 0, Math.PI * 2);
            ctx.fillStyle = '#3b82f6';
            ctx.fill();

            // 光晕
            ctx.beginPath();
            ctx.arc(cx, cy, 7, 0, Math.PI * 2);
            ctx.fillStyle = 'rgba(59,130,246,0.3)';
            ctx.fill();

            // 终点
            ctx.beginPath();
            ctx.arc(route.x2, route.y2, 2.5, 0, Math.PI * 2);
            ctx.fillStyle = 'rgba(37,99,235,0.5)';
            ctx.fill();
        });

        animId = requestAnimationFrame(draw);
    }

    draw();

    window.addEventListener('resize', () => {
        cancelAnimationFrame(animId);
        resize();
        draw();
    });
})();
