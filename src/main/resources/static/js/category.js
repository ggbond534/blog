/**
 * category.js — 分类管理页脚本
 *
 * 功能：
 *   1. 加载并渲染分类列表（按"日常"/"其他"分组展示）
 *   2. 创建新分类
 *   3. 删除分类
 *
 * 依赖：main.js（showMessage）
 */

// ====== DOM 元素 ======
const categoryList = document.getElementById('categoryList');
const createCategoryBtn = document.getElementById('createCategory');
const categoryNameInput = document.getElementById('categoryName');
const categoryDescriptionInput = document.getElementById('categoryDescription');
const categoryGroupTypeInput = document.getElementById('categoryGroupType');

/**
 * 渲染分类列表（按分组）
 * @param {Array} data - 分类数据
 */
function renderCategories(data) {
    categoryList.innerHTML = '';
    if (!Array.isArray(data) || data.length === 0) {
        categoryList.innerHTML = '<p>📭 当前没有分类，快创建一个吧。</p>';
        return;
    }

    // 按 groupType 分组
    const groups = { '日常': [], '其他': [] };
    data.forEach(item => {
        const type = item.groupType || '其他';
        if (!groups[type]) groups[type] = [];
        groups[type].push(item);
    });

    ['日常', '其他'].forEach(type => {
        const group = groups[type];
        if (!group || group.length === 0) return;

        // 分组标题
        const title = document.createElement('div');
        title.className = 'category-group-title';
        title.textContent = type === '日常' ? '📌 日常' : '📎 其他';
        categoryList.appendChild(title);

        // 渲染卡片
        group.forEach(item => {
            const card = document.createElement('div');
            card.className = 'category-card';
            card.innerHTML = `
                <div>
                    <div class="meta-row">
                        <h3>${item.name}</h3>
                        <span class="category-type-badge">${type}</span>
                    </div>
                    <p style="color:var(--text-secondary);">${item.description || '暂无描述'}</p>
                    <p class="article-meta">🔢 排序权重：${item.sortOrder || 0}</p>
                </div>
                <button class="danger" onclick="deleteCategory(${item.id})">🗑️ 删除</button>
            `;
            categoryList.appendChild(card);
        });
    });
}

/**
 * 加载分类列表
 */
function loadCategories() {
    fetch('/api/category/list', {credentials: 'include'})
        .then(res => res.json())
        .then(data => {
            if (data.code === 200) {
                renderCategories(data.data);
            } else {
                showMessage(data.message || '加载分类失败', 'error');
            }
        }).catch(() => showMessage('网络异常，无法加载分类', 'error'));
}

/**
 * 删除分类
 * @param {number} id - 分类 ID
 */
function deleteCategory(id) {
    if (!confirm('确定要删除这个分类吗？')) return;
    fetch('/api/category/delete?id=' + id, {
        method: 'POST',
        credentials: 'include'
    }).then(res => res.json())
      .then(data => {
          if (data.code === 200) {
              showMessage('删除成功', 'success');
              loadCategories();
          } else {
              showMessage(data.message || '删除失败', 'error');
          }
      }).catch(() => showMessage('网络异常，删除失败', 'error'));
}

// ====== 创建分类 ======
createCategoryBtn.addEventListener('click', () => {
    const name = categoryNameInput.value.trim();
    const description = categoryDescriptionInput.value.trim();
    if (!name) {
        showMessage('分类名称不能为空', 'error');
        return;
    }
    const groupType = categoryGroupTypeInput.value;
    fetch('/api/category/create', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        credentials: 'include',
        body: JSON.stringify({name, description, groupType})
    }).then(res => res.json())
      .then(data => {
          if (data.code === 200) {
              showMessage('创建成功', 'success');
              categoryNameInput.value = '';
              categoryDescriptionInput.value = '';
              loadCategories();
          } else {
              showMessage(data.message || '创建失败', 'error');
          }
      }).catch(() => showMessage('网络异常，创建失败', 'error'));
});

// ====== 初始化 ======
loadCategories();
