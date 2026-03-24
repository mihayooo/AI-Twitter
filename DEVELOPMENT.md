# 开发指南

本文档提供了 AI-Twitter 项目的开发指南。

## 📋 目录

- [环境设置](#环境设置)
- [项目结构](#项目结构)
- [后端开发](#后端开发)
- [前端开发](#前端开发)
- [数据库](#数据库)
- [API 开发](#api-开发)
- [测试](#测试)
- [调试](#调试)
- [常见问题](#常见问题)

## 🔧 环境设置

### 前置要求

- **Java**: 17 或更高版本
- **Node.js**: 18 或更高版本
- **MySQL**: 8.0 或更高版本
- **Git**: 最新版本
- **IDE**: IntelliJ IDEA（推荐）或 VS Code

### 安装步骤

1. **克隆仓库**
   ```bash
   git clone https://github.com/mihayooo/AI-Twitter.git
   cd AI-Twitter
   ```

2. **配置后端**
   ```bash
   cd backend
   
   # 编辑 application.yml 配置数据库
   # 修改 spring.datasource.url 和密码
   
   # 安装依赖
   mvn clean install
   ```

3. **配置前端**
   ```bash
   cd frontend
   npm install
   ```

4. **创建数据库**
   ```bash
   mysql -u root -p < backend/src/main/resources/sql/schema.sql
   ```

## 📁 项目结构

```
AI-Twitter/
├── backend/
│   ├── src/main/java/com/example/ailearningbbs/
│   │   ├── config/              # 配置类
│   │   ├── controller/          # REST 控制器
│   │   ├── service/             # 业务逻辑
│   │   ├── repository/          # 数据访问
│   │   ├── entity/              # 数据库实体
│   │   ├── dto/                 # 数据传输对象
│   │   ├── security/            # JWT 安全
│   │   └── common/              # 公共工具
│   ├── src/main/resources/
│   │   ├── mapper/              # MyBatis 映射
│   │   ├── sql/                 # SQL 脚本
│   │   └── application.yml      # 配置文件
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   │   ├── api/                 # API 调用
│   │   ├── components/          # Vue 组件
│   │   ├── views/               # 页面
│   │   ├── stores/              # Pinia 状态
│   │   ├── router/              # 路由
│   │   └── utils/               # 工具函数
│   ├── package.json
│   └── vite.config.js
│
└── docker/
    ├── docker-compose.yml
    ├── backend/Dockerfile
    └── frontend/Dockerfile
```

## 🔙 后端开发

### 启动后端

```bash
cd backend
mvn spring-boot:run
```

后端运行在 `http://localhost:8080`

### 项目配置

编辑 `backend/src/main/resources/application.yml`：

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
  secret: your-secret-key-here
  expiration: 86400000  # 24 小时

server:
  port: 8080
```

### 添加新的 API 端点

1. **创建 Entity**（如果需要）
   ```java
   @Entity
   @Table(name = "your_table")
   public class YourEntity {
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long id;
       
       // 字段定义
   }
   ```

2. **创建 Repository**
   ```java
   @Repository
   public interface YourRepository extends JpaRepository<YourEntity, Long> {
       // 自定义查询方法
   }
   ```

3. **创建 Service**
   ```java
   @Service
   public class YourService {
       @Autowired
       private YourRepository repository;
       
       public YourEntity create(YourEntity entity) {
           return repository.save(entity);
       }
   }
   ```

4. **创建 Controller**
   ```java
   @RestController
   @RequestMapping("/api/your-endpoint")
   public class YourController {
       @Autowired
       private YourService service;
       
       @PostMapping
       public ResponseEntity<?> create(@RequestBody YourEntity entity) {
           return ResponseEntity.ok(service.create(entity));
       }
   }
   ```

### 数据库迁移

使用 Flyway 或 Liquibase 进行数据库版本管理（可选）。

## 🎨 前端开发

### 启动前端

```bash
cd frontend
npm run dev
```

前端运行在 `http://localhost:5173`

### 项目配置

编辑 `frontend/.env`：

```env
VITE_API_BASE_URL=http://localhost:8080/api
VITE_APP_TITLE=AI-Twitter
```

### 创建新的页面

1. **创建 Vue 组件**
   ```vue
   <template>
     <div class="your-page">
       <!-- 页面内容 -->
     </div>
   </template>

   <script setup>
   import { ref } from 'vue'
   
   const data = ref([])
   
   // 逻辑代码
   </script>

   <style scoped>
   .your-page {
     /* 样式 */
   }
   </style>
   ```

2. **添加路由**
   编辑 `frontend/src/router/index.js`：
   ```javascript
   {
     path: '/your-page',
     component: () => import('../views/YourPage.vue'),
     meta: { requiresAuth: true }
   }
   ```

3. **调用 API**
   创建 `frontend/src/api/your-api.js`：
   ```javascript
   import request from '../utils/request'
   
   export function getYourData() {
     return request.get('/your-endpoint')
   }
   ```

### 状态管理

使用 Pinia 进行状态管理：

```javascript
// stores/your-store.js
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useYourStore = defineStore('your-store', () => {
  const data = ref([])
  
  function fetchData() {
    // 获取数据
  }
  
  return { data, fetchData }
})
```

## 🗄️ 数据库

### 数据库初始化

```bash
mysql -u root -p < backend/src/main/resources/sql/schema.sql
```

### 添加新表

1. 在 `backend/src/main/resources/sql/schema.sql` 中添加 SQL
2. 创建对应的 Entity 类
3. 创建 Repository 接口

### 查询优化

- 使用索引加速查询
- 避免 N+1 查询问题
- 使用分页处理大数据集

## 🔌 API 开发

### API 响应格式

统一的 API 响应格式：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    // 响应数据
  }
}
```

### 错误处理

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.status(500).body(
            new ApiResponse(500, e.getMessage(), null)
        );
    }
}
```

### API 文档

访问 Swagger UI：`http://localhost:8080/swagger-ui.html`

## 🧪 测试

### 后端测试

```bash
cd backend
mvn test
```

### 前端测试

```bash
cd frontend
npm run test
```

### 集成测试

```bash
cd backend
mvn verify
```

## 🐛 调试

### 后端调试

在 IntelliJ IDEA 中：
1. 设置断点
2. 点击 Debug 按钮
3. 使用调试工具栏

### 前端调试

在浏览器开发者工具中：
1. 打开 DevTools（F12）
2. 使用 Vue DevTools 扩展
3. 查看 Network 标签监控 API 调用

### 日志

**后端日志**：
```
backend/logs/application.log
```

**前端日志**：
浏览器控制台

## ❓ 常见问题

### Q: 如何重置数据库？
A: 
```bash
mysql -u root -p -e "DROP DATABASE ai_learning_bbs; CREATE DATABASE ai_learning_bbs;"
mysql -u root -p ai_learning_bbs < backend/src/main/resources/sql/schema.sql
```

### Q: 如何修改 JWT 密钥？
A: 编辑 `application.yml` 中的 `jwt.secret`

### Q: 如何添加新的依赖？
A: 
- 后端：编辑 `backend/pom.xml`
- 前端：运行 `npm install package-name`

### Q: 如何部署到生产环境？
A: 参考 `docker/docker-compose.yml` 使用 Docker 部署

---

**需要帮助？** 在 [Issues](https://github.com/mihayooo/AI-Twitter/issues) 中提问！
