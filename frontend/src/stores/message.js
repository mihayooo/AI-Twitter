import { defineStore } from 'pinia'
import { ref } from 'vue'
import {
  getMessages,
  getUserList,
  sendMessage as apiSendMessage,
  markAsRead as apiMarkAsRead,
  markAllAsRead as apiMarkAllAsRead
} from '@/api/messages'

export const useMessageStore = defineStore('message', () => {
  const conversations = ref([])
  const currentMessages = ref([])
  const currentChatUser = ref(null)
  const loading = ref(false)
  const sending = ref(false)

  async function fetchUserList() {
    try {
      const res = await getUserList()
      if (res.data.code === 200) {
        conversations.value = res.data.data
      }
    } catch (error) {
      console.error('Failed to fetch user list:', error)
    }
  }

  async function fetchMessages(userId, page = 1, size = 50) {
    loading.value = true
    try {
      const res = await getMessages(userId, { page, size })
      if (res.data.code === 200) {
        currentMessages.value = res.data.data
        currentChatUser.value = userId
        return res.data.data
      }
    } finally {
      loading.value = false
    }
  }

  async function sendMessage(receiverId, content) {
    sending.value = true
    try {
      const res = await apiSendMessage(receiverId, content)
      if (res.data.code === 200) {
        currentMessages.value.push(res.data.data)
        return { success: true, data: res.data.data }
      }
      return { success: false, message: res.data.message }
    } finally {
      sending.value = false
    }
  }

  async function markAsRead(senderId) {
    try {
      const res = await apiMarkAsRead(senderId)
      return res.data.code === 200
    } catch (error) {
      return false
    }
  }

  async function markAllAsRead(senderId) {
    try {
      const res = await apiMarkAllAsRead(senderId)
      return res.data.code === 200
    } catch (error) {
      return false
    }
  }

  function setCurrentChatUser(user) {
    currentChatUser.value = user
  }

  function clearCurrentChat() {
    currentChatUser.value = null
    currentMessages.value = []
  }

  return {
    conversations,
    currentMessages,
    currentChatUser,
    loading,
    sending,
    fetchUserList,
    fetchMessages,
    sendMessage,
    markAsRead,
    markAllAsRead,
    setCurrentChatUser,
    clearCurrentChat
  }
})
