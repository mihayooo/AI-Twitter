<template>
  <div class="messages-page">
    <div class="messages-container">
      <!-- 左侧会话列表 -->
      <div class="conversations-panel" :class="{ collapsed: !showConversations }">
        <div class="panel-header">
          <h3>私信</h3>
          <el-button
            v-if="showConversations"
            type="primary"
            text
            @click="openChat(null)"
          >
            <el-icon><Plus /></el-icon>
            发起新对话
          </el-button>
        </div>

        <div class="conversations-list" v-loading="messageStore.loading">
          <el-empty v-if="!messageStore.loading && messageStore.conversations.length === 0" description="暂无会话" />

          <div
            v-for="conv in messageStore.conversations"
            :key="conv.id"
            class="conversation-item"
            :class="{ active: messageStore.currentChatUser === conv.id }"
            @click="openChat(conv)"
          >
            <el-avatar :size="48" :src="conv.avatar || '/default-avatar.png'">
              {{ conv.username?.charAt(0).toUpperCase() }}
            </el-avatar>
            <div class="conversation-info">
              <div class="conversation-header">
                <span class="username">{{ conv.username }}</span>
                <span class="last-time">{{ formatTime(conv.last_message_time) }}</span>
              </div>
              <div class="conversation-preview">
                <span class="last-message">{{ conv.last_message || '暂无消息' }}</span>
                <el-badge
                  v-if="conv.unread_count > 0"
                  :value="conv.unread_count"
                  :max="99"
                  class="unread-badge"
                />
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧聊天窗口 -->
      <div class="chat-panel" v-if="messageStore.currentChatUser">
        <div class="chat-header">
          <div class="chat-user-info">
            <el-avatar :size="40" :src="currentUserAvatar">
              {{ currentUsername?.charAt(0).toUpperCase() }}
            </el-avatar>
            <div class="user-details">
              <span class="username">{{ currentUsername }}</span>
              <el-button
                v-if="unreadCount > 0"
                type="primary"
                size="small"
                text
                @click="markAllRead"
              >
                标为已读
              </el-button>
            </div>
          </div>
          <el-button type="danger" text @click="closeChat">
            <el-icon><Close /></el-icon>
          </el-button>
        </div>

        <div class="messages-list" ref="messagesListRef">
          <div v-if="messageStore.currentMessages.length === 0" class="empty-messages">
            开始聊天吧
          </div>
          <div
            v-for="message in messageStore.currentMessages"
            :key="message.id"
            class="message-item"
            :class="{ sent: message.senderId === authStore.user?.id }"
          >
            <el-avatar :size="32" :src="getMessageAvatar(message)" class="message-avatar">
              {{ getMessageSenderName(message)?.charAt(0).toUpperCase() }}
            </el-avatar>
            <div class="message-content">
              <div class="message-bubble">{{ message.content }}</div>
              <div class="message-time">{{ formatTime(message.createdAt) }}</div>
            </div>
          </div>
        </div>

        <div class="message-input">
          <el-input
            v-model="newMessage"
            type="textarea"
            :rows="3"
            placeholder="输入消息..."
            @keydown.enter.exact.prevent="sendMessage"
          />
          <div class="input-actions">
            <el-button
              type="primary"
              :loading="messageStore.sending"
              @click="sendMessage"
              :disabled="!newMessage.trim()"
            >
              发送
            </el-button>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div class="empty-state" v-else>
        <el-empty description="选择一个会话开始聊天" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch, nextTick } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useMessageStore } from '@/stores/message'
import { ElMessage } from 'element-plus'
import { BellFilled, Plus, Close } from '@element-plus/icons-vue'

const authStore = useAuthStore()
const messageStore = useMessageStore()

const showConversations = ref(true)
const newMessage = ref('')
const messagesListRef = ref(null)
const currentUserAvatar = ref('')
const currentUsername = ref('')
const unreadCount = ref(0)

onMounted(() => {
  loadConversations()
})

async function loadConversations() {
  await messageStore.fetchUserList()
}

async function openChat(conversation) {
  if (!conversation) {
    // 这里可以实现选择用户进行聊天的逻辑
    ElMessage.info('请选择要聊天的用户')
    return
  }

  messageStore.setCurrentChatUser(conversation.id)
  currentUserAvatar.value = conversation.avatar || '/default-avatar.png'
  currentUsername.value = conversation.username

  await messageStore.fetchMessages(conversation.id)
  unreadCount.value = conversation.unread_count || 0

  if (unreadCount.value > 0) {
    markAllRead()
  }

  scrollToBottom()
}

function closeChat() {
  messageStore.clearCurrentChat()
  newMessage.value = ''
}

async function sendMessage() {
  if (!newMessage.value.trim()) return

  const receiverId = messageStore.currentChatUser
  const result = await messageStore.sendMessage(receiverId, newMessage.value)

  if (result.success) {
    newMessage.value = ''
    scrollToBottom()
  } else {
    ElMessage.error(result.message || '发送失败')
  }
}

async function markAllRead() {
  const senderId = messageStore.currentChatUser
  await messageStore.markAllAsRead(senderId)
  unreadCount.value = 0
}

function getMessageAvatar(message) {
  if (message.senderId === authStore.user?.id) {
    return authStore.user?.avatar || '/default-avatar.png'
  }
  return message.senderAvatar || '/default-avatar.png'
}

function getMessageSenderName(message) {
  if (message.senderId === authStore.user?.id) {
    return authStore.user?.username
  }
  return message.senderUsername
}

function scrollToBottom() {
  nextTick(() => {
    if (messagesListRef.value) {
      messagesListRef.value.scrollTop = messagesListRef.value.scrollHeight
    }
  })
}

function formatTime(time) {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date

  if (diff < 60000) {
    return '刚刚'
  }
  if (diff < 3600000) {
    return `${Math.floor(diff / 60000)} 分钟前`
  }
  if (diff < 86400000) {
    return `${Math.floor(diff / 3600000)} 小时前`
  }
  if (diff < 604800000) {
    return `${Math.floor(diff / 86400000)} 天前`
  }
  return date.toLocaleDateString('zh-CN')
}
</script>

<style scoped>
.messages-page {
  max-width: 1200px;
  margin: 0 auto;
  height: calc(100vh - 120px);
}

.messages-container {
  display: flex;
  gap: 16px;
  height: 100%;
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.conversations-panel {
  width: 320px;
  border-right: 1px solid #f0f0f0;
  display: flex;
  flex-direction: column;
  transition: all 0.3s;
}

.conversations-panel.collapsed {
  width: 0;
  border-right: none;
  overflow: hidden;
}

.panel-header {
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.conversations-list {
  flex: 1;
  overflow-y: auto;
}

.conversation-item {
  display: flex;
  gap: 12px;
  padding: 12px 16px;
  cursor: pointer;
  transition: background-color 0.2s;
  border-bottom: 1px solid #f5f5f5;
}

.conversation-item:hover {
  background: #f5f7fa;
}

.conversation-item.active {
  background: #ecf5ff;
}

.conversation-info {
  flex: 1;
  min-width: 0;
}

.conversation-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.conversation-header .username {
  font-weight: 600;
  font-size: 14px;
  color: #333;
}

.last-time {
  font-size: 12px;
  color: #999;
}

.conversation-preview {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.last-message {
  font-size: 13px;
  color: #666;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  flex: 1;
}

.unread-badge {
  flex-shrink: 0;
  margin-left: 8px;
}

.chat-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.chat-header {
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chat-user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-details {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-details .username {
  font-weight: 600;
  font-size: 16px;
}

.messages-list {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.empty-messages {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #999;
  font-size: 14px;
}

.message-item {
  display: flex;
  gap: 12px;
  max-width: 70%;
}

.message-item.sent {
  align-self: flex-end;
  flex-direction: row-reverse;
}

.message-item.sent .message-content {
  align-items: flex-end;
}

.message-avatar {
  flex-shrink: 0;
}

.message-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.message-bubble {
  background: #f0f2f5;
  padding: 10px 14px;
  border-radius: 16px;
  font-size: 14px;
  line-height: 1.5;
  word-break: break-word;
}

.message-item.sent .message-bubble {
  background: #409eff;
  color: white;
}

.message-time {
  font-size: 12px;
  color: #999;
  padding: 0 4px;
}

.message-input {
  padding: 16px;
  border-top: 1px solid #f0f0f0;
  background: #fafafa;
}

.input-actions {
  margin-top: 8px;
  display: flex;
  justify-content: flex-end;
}

.empty-state {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
