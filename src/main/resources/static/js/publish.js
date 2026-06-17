/**
 * publish.js — 文章发布页脚本
 *
 * 功能：
 *   1. 加载分类数据填充下拉框
 *   2. 提交文章发布请求
 *   3. 发布成功后自动跳转到文章列表页
 *
 * 依赖：main.js（showMessage）
 */

// ====== DOM 元素 ======
const categorySelect = document.getElementById('categoryId');
const publishBtn2 = document.getElementById('publishBtn');

/**
 * 加载分类列表填充下拉框
 */
function loadCategoriesForPublish() {
    fetch('/api/category/list', {credentials: 'include'})
        .then(res => res.json())
        .then(data => {
            if (data.code === 200) {
                categorySelect.innerHTML = '<option value="">请选择分类</option>';
                data.data.forEach(item => {
                    const option = document.createElement('option');
                    option.value = item.id;
                    option.innerText = item.name;
                    categorySelect.appendChild(option);
                });
            } else {
                showMessage(data.message || '无法加载分类', 'error');
            }
        }).catch(() => showMessage('网络异常，无法加载分类', 'error'));
}

// ====== 发布文章 ======
publishBtn2.addEventListener('click', () => {
    const title = document.getElementById('title').value.trim();
    const content = document.getElementById('content').value.trim();
    const summary = document.getElementById('summary').value.trim();
    const categoryId = categorySelect.value;

    if (!title || !content || !categoryId) {
        showMessage('标题、内容和分类不能为空', 'error');
        return;
    }

    fetch('/api/article/publish', {
        method: 'POST',
        credentials: 'include',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({title, content, summary, categoryId})
    }).then(res => res.json())
      .then(data => {
          if (data.code === 200) {
              showMessage('发布成功，正在跳转...', 'success');
              setTimeout(() => window.location.href = '/article.html', 900);
          } else {
              showMessage(data.message || '发布失败', 'error');
          }
      }).catch(() => showMessage('网络异常，请稍后再试', 'error'));
});

// ====== 初始化 ======
loadCategoriesForPublish();
