# 🚀 AI-Twitter

> **The Social Network for AI Learners** | AI学习者的社交网络

一个专为 AI 和大模型学习者打造的**推特式社交平台**。在这里，你可以快速分享学习心得、发现前沿资源、与志同道合的学习者交流，构建属于 AI 学习者的社区。

![License](https://img.shields.io/badge/license-MIT-green)
![Java](https://img.shields.io/badge/Java-17-blue)
![Vue](https://img.shields.io/badge/Vue-3-green)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-green)

---

## 🎯 项目理念与愿景

### 为什么需要 AI-Twitter？

在 AI 爆炸式发展的时代，学习者面临的问题：

- 📚 **信息分散** — AI 学习资源散落在各个平台，难以汇聚
- 🔍 **质量参差** — 通用社交平台上 AI 内容被淹没，信噪比低
- 🤝 **社区缺失** — 没有专业的 AI 学习者社区，难以交流
- ⏱️ **效率低下** — 学习经验分享形式单一，不够便捷

### 我们的愿景

**打造全球最活跃的 AI 学习者社区**

- ✨ **快速分享** — 推文式设计，降低发布门槛，鼓励快速思考分享
- 🏷️ **智能发现** — 标签系统 + 搜索，让优质内容易被发现
- 👥 **社交连接** — 关注、评论、私信，构建学习者网络
- 📈 **知识沉淀** — 热门榜单、收藏夹，沉淀优质学习资源
- 🌍 **开放包容** — 欢迎所有 AI 学习者，从初学者到研究者

### 核心价值

| 价值 | 描述 |
|------|------|
| **快速迭代** | 推文式设计，快速分享想法，不需要长篇大论 |
| **垂直专业** | 专注 AI/大模型领域，信息质量高 |
| **社交驱动** | 关注、评论、私信，构建真实的学习者网络 |
| **资源聚合** | 集中汇聚 AI 学习资源、工具、论文、课程 |
| **社区文化** | 鼓励分享、讨论、协作，打造友好的学习氛围 |

---

## 🛠️ 技术栈

### 前端
- **框架**: Vue 3 + Vite（极速开发体验）
- **UI 组件**: Element Plus（企业级组件库）
- **状态管理**: Pinia（轻量级状态管理）
- **路由**: Vue Router（客户端路由）
- **HTTP**: Axios（API 请求）

### 后端
- **语言**: Java 17（最新 LTS 版本）
- **框架**: Spring Boot 3（现代化开发框架）
- **ORM**: MyBatis（灵活的数据访问）
- **数据库**: MySQL 8.0（可靠的关系型数据库）
- **认证**: JWT（无状态身份验证）
- **API 文档**: Swagger/Springfox（自动生成 API 文档）

### 部署
- **容器化**: Docker + Docker Compose（一键部署）
- **反向代理**: Nginx（高性能 Web 服务器）
- **编排**: Docker Compose（多容器编排）

---

## ✨ 核心功能

### 已实现功能 ✅

| 功能 | 描述 | 优先级 |
|------|------|--------|
| **用户认证** | 注册、登录、JWT 令牌管理 | 🔴 核心 |
| **发布帖子** | 支持文本、资源链接、标签 | 🔴 核心 |
| **动态流** | 关注用户时间线 + 全局发现 | 🔴 核心 |
| **互动功能** | 点赞、收藏、关注 | 🔴 核心 |
| **个人主页** | 用户资料、发布历史、关注列表 | 🔴 核心 |
| **帖子详情** | 完整帖子内容、评论区 | 🟡 重要 |
| **标签搜索** | 按标签发现内容 | 🟡 重要 |

### 开发中功能 🚧

- 📝 **评论系统** — 帖子评论、评论回复
- 🔔 **通知系统** — 点赞、评论、关注通知
- 💬 **私信功能** — 用户间私密沟通
- 🏆 **热门榜单** — 热门标签、热门用户、热门帖子
- 🤖 **AI 推荐** — 个性化内容推荐算法
- 📊 **数据分析** — 用户行为分析、内容热度分析

---

## 📁 项目结构

```
AI-Twitter/
├── 📂 backend/                          # Spring Boot 后端
│   ├── src/main/java/com/example/ailearningbbs/
│   │   ├── config/                      # 配置类（JWT、Web、Security）
│   │   ├── controller/                  # REST 控制器
│   │   │   ├── AuthController.java      # 认证接口
│   │   │   ├── PostController.java      # 帖子接口
│   │   │   ├── UserController.java      # 用户接口
│   │   │   ├── CommentController.java   # 评论接口
│   │   │   ├── NotificationController.java
│   │   │   └── MessageController.java   # 私信接口
│   │   ├── service/                     # 业务逻辑层
│   │   ├── repository/                  # 数据访问层（MyBatis）
│   │   ├── entity/                      # 数据库实体
│   │   ├── dto/                         # 数据传输对象
│   │   ├── security/                    # JWT 安全相关
│   │   └── common/                      # 公共工具类
│   ├── src/main/resources/
│   │   ├── mapper/                      # MyBatis SQL 映射
│   │   ├── sql/                         # 数据库初始化脚本
│   │   └── application.yml              # 配置文件
│   └── pom.xml                          # Maven 依赖配置
│
├── 📂 frontend/                         # Vue 3 前端
│   ├── src/
│   │   ├── api/                         # API 接口调用
│   │   ├── components/                  # 可复用组件
│   │   │   ├── PostCard.vue             # 帖子卡片
│   │   │   ├── UserCard.vue             # 用户卡片
│   │   │   └── ...
│   │   ├── views/                       # 页面组件
│   │   │   ├── Home.vue                 # 首页
│   │   │   ├── Discover.vue             # 发现页
│   │   │   ├── Profile.vue              # 个人主页
│   │   │   └── ...
│   │   ├── stores/                      # Pinia 状态管理
│   │   ├── router/                      # 路由配置
│   │   ├── utils/                       # 工具函数
│   │   ├── App.vue                      # 根组件
│   │   └── main.js                      # 入口文件
│   ├── package.json
│   ├── vite.config.js
│   └── index.html
│
├── 📂 docker/                           # Docker 配置
│   ├── docker-compose.yml               # 容器编排
│   ├── backend/Dockerfile               # 后端镜像
│   ├── frontend/Dockerfile              # 前端镜像
│   └── nginx/nginx.conf                 # Nginx 配置
│
├── 📄 README.md                         # 项目说明
├── 📄 LICENSE                           # MIT 许可证
└── 📄 .gitignore                        # Git 忽略文件
```

---

## 🚀 快速开始

### 前置要求

- Docker & Docker Compose（推荐）
- 或者：Java 17 + Node.js 18+ + MySQL 8.0

### 方式一：Docker Compose（推荐 ⭐）

最简单的方式，一条命令启动整个应用：

```bash
# 1. 克隆项目
git clone https://github.com/mihayooo/AI-Twitter.git
cd AI-Twitter

# 2. 启动所有服务
cd docker
docker-compose up -d

# 3. 等待服务启动（约 30 秒）
docker-compose logs -f

# 4. 访问应用
# 前端: http://localhost
# 后端 API: http://localhost:8080/api
# API 文档: http://localhost:8080/swagger-ui.html
```

### 方式二：本地开发

#### 后端启动

```bash
# 1. 创建数据库
mysql -u root -p < backend/src/main/resources/sql/schema.sql

# 2. 配置数据库连接
# 编辑 backend/src/main/resources/application.yml
# 修改 spring.datasource.url 和密码

# 3. 安装依赖并运行
cd backend
mvn clean install
mvn spring-boot:run

# 后端运行在 http://localhost:8080
```

#### 前端启动

```bash
# 1. 安装依赖
cd frontend
npm install

# 2. 启动开发服务器
npm run dev

# 前端运行在 http://localhost:5173
```

---

## 📚 API 文档

启动后端后，访问 **Swagger UI**：

```
http://localhost:8080/swagger-ui.html
```

### 核心 API 端点

#### 认证相关
```
POST   /api/auth/register          # 用户注册
POST   /api/auth/login             # 用户登录
GET    /api/auth/profile           # 获取当前用户信息
POST   /api/auth/logout            # 登出
```

#### 帖子相关
```
GET    /api/posts                  # 获取帖子列表（分页）
GET    /api/posts/timeline         # 获取关注用户时间线
GET    /api/posts/{id}             # 获取帖子详情
POST   /api/posts                  # 创建帖子
DELETE /api/posts/{id}             # 删除帖子
PUT    /api/posts/{id}             # 编辑帖子
```

#### 互动相关
```
POST   /api/posts/{id}/like        # 点赞帖子
DELETE /api/posts/{id}/like        # 取消点赞
POST   /api/posts/{id}/bookmark    # 收藏帖子
DELETE /api/posts/{id}/bookmark    # 取消收藏
```

#### 用户相关
```
GET    /api/users/{id}             # 获取用户信息
PUT    /api/users/{id}             # 更新用户信息
POST   /api/users/{id}/follow      # 关注用户
DELETE /api/users/{id}/follow      # 取消关注
GET    /api/users/{id}/followers   # 获取粉丝列表
GET    /api/users/{id}/following   # 获取关注列表
```

#### 标签相关
```
GET    /api/tags                   # 获取热门标签
GET    /api/tags/{name}/posts      # 按标签搜索帖子
```

---

## 🔧 环境配置

### 后端配置（application.yml）

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ai_learning_bbs
    username: ailearning
    password: ailearning123
  jpa:
    hibernate:
      ddl-auto: update

jwt:
  secret: your-secret-key-here-change-in-production
  expiration: 86400000  # 24 小时

server:
  port: 8080
```

### 前端配置（.env）

```env
VITE_API_BASE_URL=http://localhost:8080/api
VITE_APP_TITLE=AI-Twitter
```

---

## 📊 数据库设计

### 核心表结构

```sql
-- 用户表
CREATE TABLE users (
  id BIGINT PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  avatar_url VARCHAR(255),
  bio TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 帖子表
CREATE TABLE posts (
  id BIGINT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  content TEXT NOT NULL,
  tags VARCHAR(255),
  resource_links TEXT,
  like_count INT DEFAULT 0,
  comment_count INT DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 关注关系表
CREATE TABLE follows (
  follower_id BIGINT NOT NULL,
  following_id BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (follower_id, following_id),
  FOREIGN KEY (follower_id) REFERENCES users(id),
  FOREIGN KEY (following_id) REFERENCES users(id)
);

-- 点赞表
CREATE TABLE likes (
  id BIGINT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  post_id BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY (user_id, post_id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (post_id) REFERENCES posts(id)
);

-- 收藏表
CREATE TABLE bookmarks (
  id BIGINT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  post_id BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY (user_id, post_id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (post_id) REFERENCES posts(id)
);
```

---

## 🎨 UI/UX 设计特点

- 🎯 **简洁直观** — 推特式设计，用户上手快
- 📱 **响应式布局** — 完美适配桌面、平板、手机
- 🌙 **深色模式** — 护眼设计，支持主题切换
- ⚡ **快速加载** — Vite 构建，极速开发体验
- ♿ **无障碍支持** — 符合 WCAG 标准

---

## 🤝 贡献指南

欢迎贡献代码、报告 Bug、提出功能建议！

### 贡献流程

1. **Fork** 本仓库
2. **创建特性分支** (`git checkout -b feature/AmazingFeature`)
3. **提交更改** (`git commit -m 'Add some AmazingFeature'`)
4. **推送到分支** (`git push origin feature/AmazingFeature`)
5. **开启 Pull Request**

### 代码规范

- 遵循 Google Java 代码规范（后端）
- 遵循 Vue 3 官方风格指南（前端）
- 提交前运行 linter 检查
- 编写清晰的提交信息

---

## 📝 开发路线图

### Phase 1（已完成 ✅）
- [x] 用户认证系统
- [x] 帖子发布与浏览
- [x] 基础互动功能（点赞、收藏）
- [x] 用户关注系统
- [x] 标签搜索

### Phase 2（进行中 🚧）
- [ ] 评论系统
- [ ] 通知系统
- [ ] 私信功能
- [ ] 热门榜单

### Phase 3（计划中 📅）
- [ ] AI 推荐算法
- [ ] 内容审核系统
- [ ] 用户等级系统
- [ ] 社区徽章
- [ ] 移动端 App

### Phase 4（远期规划 🔮）
- [ ] 实时通知（WebSocket）
- [ ] 内容审核 AI
- [ ] 多语言支持
- [ ] 国际化部署

---

## 🐛 已知问题

- 暂无已知重大问题
- 欢迎提交 Issue 报告 Bug

---

## 📄 许可证

本项目采用 **MIT License** 开源许可证。详见 [LICENSE](LICENSE) 文件。

---

## 👨‍💻 作者

**mihayoo** — AI 学习者 & 开发者

- GitHub: [@mihayooo](https://github.com/mihayooo)
- 项目主页: [AI-Twitter](https://github.com/mihayooo/AI-Twitter)

---

## 🙏 致谢

感谢所有贡献者和使用者的支持！

特别感谢：
- Vue 3 & Spring Boot 社区
- Element Plus UI 组件库
- 所有开源项目的贡献者

---

## 📞 联系方式

- 📧 提交 Issue 进行反馈
- 💬 讨论区进行讨论
- 🌟 如果喜欢，请给个 Star！

---

<div align="center">

**⭐ 如果这个项目对你有帮助，请给个 Star 支持一下！**

Made with ❤️ by [mihayoo](https://github.com/mihayooo)

</div>
