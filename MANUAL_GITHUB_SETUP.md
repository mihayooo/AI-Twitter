# 🎯 GitHub 仓库创建 - 手动步骤指南

## ⚠️ 当前情况

你的 GitHub CLI 已登录，但 token 的权限不足以自动创建仓库。这很常见，因为 Personal Access Token 需要特定的权限范围。

**不用担心！** 我们可以用两种方式解决：

---

## 方案 A：手动在 GitHub 网页上创建（最简单，推荐）

### 步骤 1：打开 GitHub 新建仓库页面

在浏览器中打开：
```
https://github.com/new
```

### 步骤 2：填写仓库信息

| 字段 | 值 |
|------|-----|
| **Repository name** | `AI-Twitter` |
| **Description** | `🚀 The Social Network for AI Learners \| AI学习者的社交网络` |
| **Visibility** | `Public` |
| **Initialize this repository with** | **不勾选任何选项** |

### 步骤 3：点击 Create repository

完成！仓库已创建。

---

## 方案 B：重新生成 Token 并自动创建（如果需要）

如果你想用 CLI 自动创建，需要重新生成 token 并确保权限正确：

### 步骤 1：生成新的 Personal Access Token

1. 访问：https://github.com/settings/tokens/new
2. 填写信息：
   - **Token name**: `GitHub CLI Token`
   - **Expiration**: `90 days` 或 `No expiration`
   - **Scopes**: 勾选以下权限：
     - ✅ `repo` (完整的仓库访问)
     - ✅ `read:org` (读取组织信息)
     - ✅ `workflow` (管理 GitHub Actions)
3. 点击 **Generate token**
4. **复制 token**（只显示一次！）

### 步骤 2：重新登录 GitHub CLI

```bash
gh auth logout
gh auth login
```

选择：
- Protocol: `HTTPS`
- Authenticate with your GitHub credentials: `Y`
- How would you like to authenticate GitHub CLI: `Paste an authentication token`
- 粘贴你刚才复制的 token

### 步骤 3：创建仓库

```bash
cd D:\Projects\Claude\ai_learning_bbs

gh repo create AI-Twitter --public --description "🚀 The Social Network for AI Learners | AI学习者的社交网络" --source=. --remote=origin --push
```

---

## 推荐方案：方案 A（手动创建）+ 自动推送

这是最稳定的方式：

### 第 1 步：手动创建仓库

按照 **方案 A** 的步骤在 GitHub 网页上创建仓库。

### 第 2 步：推送代码

创建完仓库后，在 PowerShell 中运行：

```bash
cd D:\Projects\Claude\ai_learning_bbs

# 配置远程仓库
git remote set-url origin https://github.com/mihayooo/AI-Twitter.git

# 推送代码
git push -u origin main
```

**这样做的好处：**
- ✅ 简单快速
- ✅ 不需要修改 token
- ✅ 完全可控
- ✅ 推送过程中会提示输入凭证

---

## 📋 完整的推送命令

创建完仓库后，复制以下命令到 PowerShell 中执行：

```powershell
cd D:\Projects\Claude\ai_learning_bbs
git remote set-url origin https://github.com/mihayooo/AI-Twitter.git
git push -u origin main
```

**预期输出：**
```
Enumerating objects: 120, done.
Counting objects: 100% (120/120), done.
Delta compression using up to 8 threads
Compressing objects: 100% (110/110), done.
Writing objects: 100% (120/120), 2.50 MiB | 1.25 MiB/s, done.
Total 120 (delta 45), reused 0 (delta 0), received 0 (delta 0)
remote: Resolving deltas: 100% (45/45), done.
To https://github.com/mihayooo/AI-Twitter.git
 * [new branch]      main -> main
Branch 'main' set to track remote branch 'main' from 'origin'.
```

---

## 🔧 如果推送时要求输入凭证

当你运行 `git push` 时，可能会出现以下提示：

```
Username for 'https://github.com': 
```

输入你的 GitHub 用户名：
```
mihayooo
```

然后会提示输入密码：
```
Password for 'https://mihayooo@github.com':
```

**注意：** 这里输入的不是你的 GitHub 密码，而是：
- **Personal Access Token**（推荐）
- 或者你的 GitHub 密码（如果启用了 2FA，需要用 token）

由于你已经配置了 GitHub CLI，你可以直接使用 token。

---

## ✅ 验证推送成功

推送完成后，检查以下内容：

1. **访问仓库**：https://github.com/mihayooo/AI-Twitter
2. **查看文件**：应该能看到所有 110+ 个文件
3. **查看 commit**：应该能看到 7 个 commit
4. **查看分支**：应该能看到 `main` 分支

---

## 📝 后续步骤

### 第 3 步：配置 GitHub Pages

1. 进入仓库：https://github.com/mihayooo/AI-Twitter
2. 点击 **Settings**
3. 左侧菜单选择 **Pages**
4. **Source** 选择：
   - Branch: `main`
   - Folder: `/docs`
5. 点击 **Save**

等待 1-2 分钟，你的 GitHub Pages 就会在以下地址发布：
```
https://mihayooo.github.io/AI-Twitter/
```

### 第 4 步：配置仓库信息

1. 进入仓库主页：https://github.com/mihayooo/AI-Twitter
2. 点击 **⚙️ Settings**
3. 在 **General** 标签页中：
   - **Description**: `🚀 The Social Network for AI Learners`
   - **Website**: `https://mihayooo.github.io/AI-Twitter/`
   - **Topics**: `ai`, `learning`, `community`, `social-network`, `vue3`, `spring-boot`, `java`, `docker`
4. 向下滚动，启用：
   - ✅ **Discussions**
   - ✅ **Issues**
   - ✅ **Projects**
   - ✅ **Wiki**

---

## 🎉 完成！

完成以上步骤后，你的项目就会：

✅ 在 GitHub 上发布  
✅ 有美观的 GitHub Pages 网站  
✅ 支持社区讨论和贡献  
✅ 准备好接收 Stars 和 Forks！

---

## 📞 需要帮助？

如果遇到问题，请查看：
- [GITHUB_UPLOAD_GUIDE.md](./GITHUB_UPLOAD_GUIDE.md) - 详细的上传指南
- [QUICK_REFERENCE.md](./QUICK_REFERENCE.md) - 快速参考
- [GitHub 官方文档](https://docs.github.com)

---

**祝你的项目成功！** 🚀
