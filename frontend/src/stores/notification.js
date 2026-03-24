import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import {
  getNotifications,
  getUnreadCount,
  markAsRead as apiMarkAsRead,
  markAllAsRead as apiMarkAllAsRead
} from '@/api/notifications'

export const useNotificationStore = defineStore('notification', () => {
  const notifications = ref([])
  const unreadCount = ref(0)
  const loading = ref(false)

  async function fetchNotifications(page = 1, size = 20) {
    loading.value = true
    try {
      const res = await getNotifications({ page, size })
      if (res.data.code === 200) {
        notifications.value = res.data.data
      }
    } finally {
      loading.value = false
    }
  }

  async function fetchUnreadCount() {
    try {
      const res = await getUnreadCount()
      if (res.data.code === 200) {
        unreadCount.value = res.data.data
      }
    } catch (error) {
      console.error('Failed to fetch unread count:', error)
    }
  }

  async function markAsRead(notificationId) {
    try {
      const res = await apiMarkAsRead(notificationId)
      if (res.data.code === 200) {
        // 更新本地状态
        const notification = notifications.value.find(n => n.id === notificationId)
        if (notification) {
          notification.isRead = true
        }
        unreadCount.value = Math.max(0, unreadCount.value - 1)
        return { success: true }
      }
      return { success: false, message: res.data.message }
    } catch (error) {
      return { success: false, message: error.response?.data?.message || '操作失败' }
    }
  }

  async function markAllAsRead() {
    try {
      const res = await apiMarkAllAsRead()
      if (res.data.code === 200) {
        notifications.value.forEach(n => n.isRead = true)
        unreadCount.value = 0
        return { success: true }
      }
      return { success: false, message: res.data.message }
    } catch (error) {
      return { success: false, message: error.response?.data?.message || '操作失败' }
    }
  }

  function getNotificationText(notification) {
    const typeMap = {
      like: '赞了你的帖子',
      comment: '评论了你的帖子',
      follow: '关注了你'
    }
    return `${notification.actorUsername} ${typeMap[notification.type] || ''}`
  }

  return {
    notifications,
    unreadCount,
    loading,
    fetchNotifications,
    fetchUnreadCount,
    markAsRead,
    markAllAsRead,
    getNotificationText
  }
})
