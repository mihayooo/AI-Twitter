<template>
  <el-dropdown
    ref="dropdownRef"
    trigger="click"
    @visible-change="handleVisibleChange"
  >
    <div class="notification-bell" :class="{ 'has-unread': unreadCount > 0 }">
      <el-badge :value="unreadCount" :hidden="unreadCount === 0" :max="99">
        <el-icon :size="20"><Bell /></el-icon>
      </el-badge>
    </div>

    <template #dropdown>
      <div class="notification-dropdown">
        <div class="dropdown-header">
          <span>通知</span>
          <div class="dropdown-actions" v-if="unreadCount > 0">
            <el-button
              type="primary"
              size="small"
              text
              @click="markAllAsRead"
              :loading="markingAllRead"
            >
              全部标为已读
            </el-button>
          </div>
        </div>

        <div class="notification-list" v-loading="loading">
          <el-empty v-if="!loading && notifications.length === 0" description="暂无通知" />

          <div
            v-for="notification in notifications"
            :key="notification.id"
            class="notification-item"
            :class="{ unread: !notification.isRead }"
            @click="handleNotificationClick(notification)"
          >
            <el-avatar :size="40" :src="notification.actorAvatar || '/default-avatar.png'">
              {{ notification.actorUsername?.charAt(0).toUpperCase() }}
            </el-avatar>
            <div class="notification-content">
              <div class="notification-text">
                <router-link :to="`/profile/${notification.actorId}`" class="username">
                  {{ notification.actorUsername }}
                </router-link>
                {{ getNotificationText(notification) }}
              </div>
              <div class="notification-time">{{ formatTime(notification.createdAt) }}</div>
            </div>
            <div class="notification-actions" v-if="!notification.isRead" @click.stop>
              <el-button
                type="primary"
                size="small"
                text
                @click="handleMarkAsRead(notification.id)"
                :loading="markingRead[notification.id]"
              >
                标为已读
              </el-button>
            </div>
          </div>
        </div>

        <div class="dropdown-footer" v-if="notifications.length > 0">
          <el-button text @click="viewAll">查看全部通知</el-button>
        </div>
      </div>
    </template>
  </el-dropdown>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Bell } from '@element-plus/icons-vue'
import { useNotificationStore } from '@/stores/notification'
import { ElMessage } from 'element-plus'

const router = useRouter()
const notificationStore = useNotificationStore()

const dropdownRef = ref(null)
const loading = ref(false)
const markingAllRead = ref(false)
const markingRead = ref({})

const {
  notifications,
  unreadCount,
  fetchNotifications,
  markAsRead,
  markAllAsRead: storeMarkAllAsRead,
  getNotificationText
} = notificationStore

onMounted(() => {
  if (notificationStore.isAuthenticated) {
    loadNotifications()
    fetchUnreadCount()
  }
})

async function loadNotifications() {
  loading.value = true
  try {
    await fetchNotifications(1, 10)
  } finally {
    loading.value = false
  }
}

async function handleVisibleChange(visible) {
  if (visible) {
    await loadNotifications()
  }
}

async function handleMarkAsRead(notificationId) {
  if (markingRead.value[notificationId]) return

  markingRead.value[notificationId] = true
  try {
    await markAsRead(notificationId)
    await notificationStore.fetchUnreadCount()
  } finally {
    markingRead.value[notificationId] = false
  }
}

async function markAllAsRead() {
  if (markingAllRead.value) return

  markingAllRead.value = true
  try {
    await storeMarkAllAsRead()
    ElMessage.success('已全部标为已读')
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '操作失败')
  } finally {
    markingAllRead.value = false
  }
}

function handleNotificationClick(notification) {
  // 如果有目标，跳转到目标页面
  if (notification.targetType && notification.targetId) {
    let route = null
    if (notification.targetType === 'post') {
      route = { name: 'PostDetail', params: { id: notification.targetId } }
    } else if (notification.targetType === 'user') {
      route = { name: 'Profile', params: { id: notification.targetId } }
    }

    if (route) {
      router.push(route)
      dropdownRef.value?.hide()
    }
  }

  // 如果未读，标为已读
  if (!notification.isRead) {
    handleMarkAsRead(notification.id)
  }
}

function viewAll() {
  router.push({ name: 'Profile', params: { id: notificationStore.user?.id } })
  dropdownRef.value?.hide()
}

function formatTime(time) {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date

  // 小于1分钟
  if (diff < 60000) {
    return '刚刚'
  }
  // 小于1小时
  if (diff < 3600000) {
    return `${Math.floor(diff / 60000)} 分钟前`
  }
  // 小于1天
  if (diff < 86400000) {
    return `${Math.floor(diff / 3600000)} 小时前`
  }
  // 小于7天
  if (diff < 604800000) {
    return `${Math.floor(diff / 86400000)} 天前`
  }
  // 否则显示日期
  return date.toLocaleDateString('zh-CN')
}
</script>

<style scoped>
.notification-bell {
  cursor: pointer;
  padding: 8px;
  border-radius: 50%;
  transition: background-color 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.notification-bell:hover {
  background-color: rgba(0, 0, 0, 0.04);
}

.notification-bell.has-unread {
  color: #409eff;
}

.notification-dropdown {
  width: 400px;
  max-height: 500px;
  display: flex;
  flex-direction: column;
}

.dropdown-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #f0f0f0;
  font-weight: 600;
  font-size: 16px;
}

.dropdown-actions {
  display: flex;
  gap: 8px;
}

.notification-list {
  overflow-y: auto;
  flex: 1;
  padding: 8px 0;
}

.notification-item {
  display: flex;
  gap: 12px;
  padding: 12px 20px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.notification-item:hover {
  background-color: #f5f7fa;
}

.notification-item.unread {
  background-color: #ecf5ff;
}

.notification-content {
  flex: 1;
  min-width: 0;
}

.notification-text {
  font-size: 14px;
  color: #333;
  line-height: 1.5;
  margin-bottom: 4px;
}

.notification-text .username {
  font-weight: 600;
  color: #333;
  text-decoration: none;
  margin-right: 4px;
}

.notification-text .username:hover {
  color: #409eff;
}

.notification-time {
  font-size: 12px;
  color: #999;
}

.notification-actions {
  display: flex;
  align-items: flex-start;
  flex-shrink: 0;
}

.dropdown-footer {
  padding: 12px;
  text-align: center;
  border-top: 1px solid #f0f0f0;
  background: #fafafa;
}
</style>
