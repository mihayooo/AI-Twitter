<template>
  <div class="post-card" @click="goToPost">
    <div class="post-header">
      <router-link :to="`/profile/${post.userId}`" @click.stop>
        <el-avatar :size="44" :src="post.userAvatar || '/default-avatar.png'">
          {{ post.username?.charAt(0).toUpperCase() }}
        </el-avatar>
      </router-link>
      <div class="post-info">
        <router-link :to="`/profile/${post.userId}`" @click.stop class="username">
          {{ post.username }}
        </router-link>
        <span class="time">{{ formatTime(post.createdAt) }}</span>
      </div>
      <el-dropdown v-if="isAuthor" @command="handleCommand" @click.stop>
        <el-button text>
          <el-icon><MoreFilled /></el-icon>
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="edit">编辑</el-dropdown-item>
            <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>

    <div class="post-content">
      <p>{{ post.content }}</p>

      <a v-if="post.resourceUrl" :href="post.resourceUrl" target="_blank" class="resource-link" @click.stop>
        <el-icon><Link /></el-icon>
        <span>{{ formatUrl(post.resourceUrl) }}</span>
      </a>

      <div v-if="post.tags" class="tags">
        <el-tag v-for="tag in parseTags(post.tags)" :key="tag" size="small" @click.stop="searchByTag(tag)">
          #{{ tag }}
        </el-tag>
      </div>
    </div>

    <div class="post-actions">
      <div class="action" :class="{ active: post.liked }" @click.stop="handleLike">
        <el-icon><Star /></el-icon>
        <span>{{ post.likeCount || 0 }}</span>
      </div>
      <div class="action" :class="{ active: post.bookmarked }" @click.stop="handleBookmark">
        <el-icon v-if="post.bookmarked"><StarFilled /></el-icon>
        <el-icon v-else><Star /></el-icon>
        <span>{{ post.bookmarked ? '已收藏' : '收藏' }}</span>
      </div>
      <div class="action" @click.stop="goToPost">
        <el-icon><ChatDotRound /></el-icon>
        <span>{{ post.commentCount || 0 }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { ElMessageBox, ElMessage } from 'element-plus'

const props = defineProps({
  post: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['like', 'bookmark', 'delete'])

const router = useRouter()
const authStore = useAuthStore()

const isAuthor = computed(() => {
  return authStore.user && authStore.user.id === props.post.userId
})

function formatTime(time) {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date

  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return `${Math.floor(diff / 60000)}分钟前`
  if (diff < 86400000) return `${Math.floor(diff / 3600000)}小时前`
  if (diff < 604800000) return `${Math.floor(diff / 86400000)}天前`

  return date.toLocaleDateString('zh-CN')
}

function formatUrl(url) {
  try {
    const urlObj = new URL(url)
    return urlObj.hostname
  } catch {
    return url
  }
}

function parseTags(tagsStr) {
  if (!tagsStr) return []
  return tagsStr.split(',').map(t => t.trim()).filter(t => t)
}

function searchByTag(tag) {
  router.push({ path: '/explore', query: { tag } })
}

function goToPost() {
  router.push(`/post/${props.post.id}`)
}

function handleLike() {
  emit('like', props.post.id)
}

function handleBookmark() {
  emit('bookmark', props.post.id)
}

function handleCommand(command) {
  if (command === 'edit') {
    router.push({ path: '/create', query: { id: props.post.id } })
  } else if (command === 'delete') {
    ElMessageBox.confirm('确定要删除这条帖子吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      emit('delete', props.post.id)
      ElMessage.success('删除成功')
    }).catch(() => {})
  }
}
</script>

<style scoped>
.post-card {
  background: #fff;
  padding: 16px;
  border-bottom: 1px solid #eee;
  cursor: pointer;
  transition: background 0.2s;
}

.post-card:hover {
  background: #fafafa;
}

.post-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.post-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.username {
  font-weight: 600;
  color: #333;
}

.username:hover {
  color: #409eff;
}

.time {
  font-size: 13px;
  color: #999;
}

.post-content {
  padding-left: 56px;
  margin-bottom: 12px;
}

.post-content p {
  white-space: pre-wrap;
  word-break: break-word;
  line-height: 1.6;
  margin-bottom: 12px;
}

.resource-link {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 8px 12px;
  background: #f0f2f5;
  border-radius: 6px;
  color: #409eff;
  margin-bottom: 12px;
  transition: background 0.2s;
}

.resource-link:hover {
  background: #e8e8e8;
}

.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tags .el-tag {
  cursor: pointer;
}

.post-actions {
  display: flex;
  gap: 24px;
  padding-left: 56px;
}

.action {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #666;
  cursor: pointer;
  transition: color 0.2s;
}

.action:hover,
.action.active {
  color: #409eff;
}
</style>