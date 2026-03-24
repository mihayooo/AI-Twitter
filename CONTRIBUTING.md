# 贡献指南

感谢你对 AI-Twitter 的关注！我们欢迎各种形式的贡献。

## 🤝 贡献方式

### 1. 报告 Bug

如果你发现了 Bug，请：

1. 检查 [Issues](https://github.com/mihayooo/AI-Twitter/issues) 中是否已有相同报告
2. 创建新 Issue，包含以下信息：
   - Bug 描述
   - 复现步骤
   - 预期行为
   - 实际行为
   - 环境信息（OS、浏览器、Java 版本等）

### 2. 提出功能建议

有好的想法？请：

1. 在 [Discussions](https://github.com/mihayooo/AI-Twitter/discussions) 中讨论
2. 或创建 Issue，标记为 `enhancement`

### 3. 提交代码

#### 前置要求

- Java 17+
- Node.js 18+
- MySQL 8.0+
- Git

#### 开发流程

1. **Fork 仓库**
   ```bash
   git clone https://github.com/YOUR_USERNAME/AI-Twitter.git
   cd AI-Twitter
   ```

2. **创建特性分支**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **本地开发**
   
   **后端开发：**
   ```bash
   cd backend
   mvn clean install
   mvn spring-boot:run
   ```
   
   **前端开发：**
   ```bash
   cd frontend
   npm install
   npm run dev
   ```

4. **提交更改**
   ```bash
   git add .
   git commit -m "feat: add your feature description"
   ```

5. **推送到你的 Fork**
   ```bash
   git push origin feature/your-feature-name
   ```

6. **创建 Pull Request**
   - 在 GitHub 上创建 PR
   - 描述你的更改
   - 链接相关的 Issue

#### 代码规范

**Java 代码规范：**
- 遵循 [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- 使用 4 个空格缩进
- 类名使用 PascalCase
- 方法名和变量名使用 camelCase
- 常量使用 UPPER_SNAKE_CASE

**Vue 代码规范：**
- 遵循 [Vue 3 官方风格指南](https://vuejs.org/guide/scaling-up/sfc.html)
- 使用 2 个空格缩进
- 组件名使用 PascalCase
- 文件名使用 kebab-case

**提交信息规范：**
```
<type>(<scope>): <subject>

<body>

<footer>
```

类型：
- `feat`: 新功能
- `fix`: Bug 修复
- `docs`: 文档更新
- `style`: 代码风格（不影响功能）
- `refactor`: 代码重构
- `perf`: 性能优化
- `test`: 测试相关
- `chore`: 构建、依赖等

示例：
```
feat(post): add post editing functionality

- Allow users to edit their posts within 1 hour of creation
- Add edit history tracking
- Update post timestamp to show "edited"

Closes #123
```

### 4. 改进文档

文档改进总是受欢迎的！

- 修复拼写错误
- 改进说明清晰度
- 添加示例
- 翻译文档

## 📋 Pull Request 检查清单

提交 PR 前，请确保：

- [ ] 代码遵循项目的代码规范
- [ ] 已添加必要的测试
- [ ] 所有测试通过
- [ ] 已更新相关文档
- [ ] 提交信息清晰有意义
- [ ] 没有引入新的 linting 错误

## 🎯 优先级

我们优先考虑以下类型的贡献：

1. **Bug 修复** — 特别是安全相关的 Bug
2. **性能优化** — 改进应用速度和效率
3. **文档改进** — 帮助其他开发者理解代码
4. **新功能** — 符合项目愿景的功能
5. **代码重构** — 改进代码质量和可维护性

## 💬 讨论和问题

- 使用 [Discussions](https://github.com/mihayooo/AI-Twitter/discussions) 进行一般讨论
- 使用 [Issues](https://github.com/mihayooo/AI-Twitter/issues) 报告 Bug 或提出功能请求

## 📚 资源

- [项目 README](../README.md)
- [API 文档](../README.md#-api-文档)
- [开发指南](./DEVELOPMENT.md)
- [架构设计](./ARCHITECTURE.md)

## 🙏 致谢

感谢所有贡献者的支持！每一个贡献都帮助我们构建更好的 AI 学习社区。

---

**Happy coding! 🚀**
