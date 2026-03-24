<template>
  <div class="layout">
    <header class="header">
      <div class="header-content">
        <router-link to="/" class="logo">
          <el-icon><Connection /></el-icon>
          <span>AI学习社区</span>
        </router-link>

        <nav class="nav" v-if="authStore.isAuthenticated">
          <router-link to="/" class="nav-item" :class="{ active: $route.path === '/' }">
            <el-icon><HomeFilled /></el-icon>
            <span>首页</span>
          </router-link>
          <router-link to="/explore" class="nav-item" :class="{ active: $route.path.startsWith('/explore') }">
            <el-icon><Compass /></el-icon>
            <span>发现</span>
          </router-link>
          <router-link to="/messages" class="nav-item" :class="{ active: $route.path === '/messages' }">
            <el-icon><ChatDotRound /></el-icon>
            <span>私信</span>
          </router-link>
        </nav>

        <div class="header-actions">
          <template v-if="authStore.isAuthenticated">
            <router-link to="/create">
              <el-button type="primary" round>
                <el-icon><Edit /></el-icon>
                发帖
              </el-button>
            </router-link>
            <NotificationBell />
            <el-dropdown @command="handleCommand">
              <div class="user-avatar">
                <el-avatar :size="36" :src="authStore.user?.avatar || '/default-avatar.png'">
                  {{ authStore.user?.username?.charAt(0).toUpperCase() }}
                </el-avatar>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item :command="`/profile/${authStore.user?.id}`">
                    <el-icon><User /></el-icon>我的主页
                  </el-dropdown-item>
                  <el-dropdown-item divided command="logout">
                    <el-icon><SwitchButton /></el-icon>退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <router-link to="/login">
              <el-button round>登录</el-button>
            </router-link>
            <router-link to="/register">
              <el-button type="primary" round>注册</el-button>
            </router-link>
          </template>
        </div>
      </div>
    </header>

    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'
import NotificationBell from '@/components/NotificationBell.vue'
import { ChatDotRound, Compass, Connection, Edit, HomeFilled, User } from '@element-plus/icons-vue'

const authStore = useAuthStore()
const router = useRouter()

function handleCommand(command) {
  if (command === 'logout') {
    authStore.logout()
  } else {
    router.push(command)
  }
}
</script>

<style scoped>
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: #fff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 20px;
  font-weight: 600;
  color: #409eff;
}

.logo .el-icon {
  font-size: 28px;
}

.nav {
  display: flex;
  gap: 20px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  border-radius: 20px;
  color: #666;
  transition: all 0.3s;
}

.nav-item:hover,
.nav-item.active {
  color: #409eff;
  background: #ecf5ff;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.notification-bell {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 4px;
  border-radius: 50%;
  transition: background-color 0.2s;
}

.notification-bell:hover {
  background-color: rgba(0, 0, 0, 0.04);
}

.user-avatar {
  cursor: pointer;
  border-radius: 50%;
  padding: 2px;
  border: 2px solid transparent;
  transition: border-color 0.3s;
}

.user-avatar:hover {
  border-color: #409eff;
}

.user-avatar {
  cursor: pointer;
  border-radius: 50%;
  padding: 2px;
  border: 2px solid transparent;
  transition: border-color 0.3s;
}

.user-avatar:hover {
  border-color: #409eff;
}

.main-content {
  flex: 1;
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
  padding: 20px;
}
</style>