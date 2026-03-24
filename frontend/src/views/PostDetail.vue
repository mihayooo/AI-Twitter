<template>
  <div class="post-detail" v-loading="loading">
    <div class="post-card" v-if="post">
      <div class="post-header">
        <router-link :to="`/profile/${post.userId}`">
          <el-avatar :size="48" :src="post.userAvatar || '/default-avatar.png'">
            {{ post.username?.charAt(0).toUpperCase() }}
          </el-avatar>
        </router-link>
        <div class="post-info">
          <router-link :to="`/profile/${post.userId}`" class="username">
            {{ post.username }}
          </router-link>
          <span class="time">{{ formatTime(post.createdAt) }}</span>
        </div>
      </div>

      <div class="post-content">
        <p>{{ post.content }}</p>

        <a v-if="post.resourceUrl" :href="post.resourceUrl" target="_blank" class="resource-link">
          <el-icon><Link /></el-icon>
          <span>{{ formatUrl(post.resourceUrl) }}</span>
        </a>

        <div v-if="post.tags" class="tags">
          <el-tag v-for="tag in parseTags(post.tags)" :key="tag" size="small">
            #{{ tag }}
          </el-tag>
        </div>
      </div>

      <div class="post-actions">
        <div class="action" :class="{ active: post.liked }" @click="handleLike">
          <el-icon><Star /></el-icon>
          <span>{{ post.likeCount || 0 }}</span>
        </div>
        <div class="action" :class="{ active: post.bookmarked }" @click="handleBookmark">
          <el-icon v-if="post.bookmarked"><StarFilled /></el-icon>
          <el-icon v-else><Star /></el-icon>
          <span>{{ post.bookmarked ? '已收藏' : '收藏' }}</span>
        </div>
      </div>
    </div>

    <div class="comments-section">
      <h3>评论 ({{ post?.commentCount || 0 }})</h3>

      <div class="comment-form" v-if="authStore.isAuthenticated">
        <el-input
          v-model="commentContent"
          type="textarea"
          :rows="3"
          placeholder="写下你的评论..."
        />
        <el-button type="primary" @click="submitComment" :loading="commentLoading">
          发表评论
        </el-button>
      </div>
      <div class="comment-form" v-else>
        <p class="login-prompt">
          <router-link to="/login">登录</router-link>后发表评论
        </p>
      </div>

      <div class="comments-list" v-loading="loadingComments">
        <el-empty v-if="comments.length === 0 && !loadingComments" description="还没有评论，快来抢沙发吧！" />

        <template v-for="comment in comments" :key="comment.id">
          <div class="comment-item">
            <div class="comment-header">
              <router-link :to="`/profile/${comment.userId}`">
                <el-avatar :size="40" :src="comment.userAvatar || '/default-avatar.png'">
                  {{ comment.username?.charAt(0).toUpperCase() }}
                </el-avatar>
              </router-link>
              <div class="comment-info">
                <router-link :to="`/profile/${comment.userId}`" class="username">
                  {{ comment.username }}
                </router-link>
                <span class="time">{{ formatTime(comment.createdAt) }}</span>
              </div>
              <div class="comment-actions" v-if="authStore.userId === comment.userId">
                <el-button
                  type="danger"
                  size="small"
                  link
                  @click="deleteCommentHandler(comment.id)"
                  :icon="Delete"
                >
                  删除
                </el-button>
              </div>
            </div>
            <div class="comment-content">
              <p>{{ comment.content }}</p>
            </div>
          </div>
        </template>

        <div class="load-more" v-if="hasMore && comments.length > 0">
          <el-button
            type="primary"
            text
            :loading="loadingComments"
            @click="loadComments"
          >
            加载更多
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { usePostStore } from '@/stores/post'
import { Link, Star, StarFilled, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getComments, createComment, deleteComment } from '@/api/posts'

const route = useRoute()
const authStore = useAuthStore()
const postStore = usePostStore()

const loading = ref(false)
const post = ref(null)
const comments = ref([])
const commentContent = ref('')
const commentLoading = ref(false)
const loadingComments = ref(false)
const page = ref(1)
const size = ref(20)
const hasMore = ref(true)

onMounted(() => {
  loadPost()
  loadComments()
})

async function loadPost() {
  loading.value = true
  try {
    const res = await postStore.getPost(route.params.id)
    if (res.success) {
      post.value = res.data
    }
  } finally {
    loading.value = false
  }
}

async function loadComments() {
  if (!hasMore.value) return

  loadingComments.value = true
  try {
    const res = await getComments(route.params.id, {
      page: page.value,
      size: size.value
    })
    if (res.success) {
      if (res.data.length < size.value) {
        hasMore.value = false
      }
      comments.value = [...comments.value, ...res.data]
      page.value++
    }
  } finally {
    loadingComments.value = false
  }
}

function formatTime(time) {
  if (!time) return ''
  const date = new Date(time)
  return date.toLocaleString('zh-CN')
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

function handleLike() {
  postStore.toggleLike(post.value.id).then(result => {
    if (result.success) {
      post.value.liked = result.liked
      post.value.likeCount += result.liked ? 1 : -1
    }
  })
}

function handleBookmark() {
  postStore.toggleBookmark(post.value.id).then(result => {
    if (result.success) {
      post.value.bookmarked = result.bookmarked
    }
  })
}

async function submitComment() {
  if (!commentContent.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }

  commentLoading.value = true
  try {
    const res = await createComment({
      postId: post.value.id,
      content: commentContent.value,
      parentId: null
    })
    if (res.success) {
      ElMessage.success('评论成功')
      commentContent.value = ''
      // 重新加载评论列表
      comments.value.unshift(res.data)
      post.value.commentCount = (post.value.commentCount || 0) + 1
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '评论失败')
  } finally {
    commentLoading.value = false
  }
}

async function deleteCommentHandler(commentId) {
  try {
    await ElMessageBox.confirm(
      '确定要删除这条评论吗？',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )

    const res = await deleteComment(commentId)
    if (res.success) {
      ElMessage.success('删除成功')
      comments.value = comments.value.filter(c => c.id !== commentId)
      post.value.commentCount = Math.max(0, post.value.commentCount - 1)
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.response?.data?.message || '删除失败')
    }
  }
}

function handleScroll() {
  // 可以在这里实现滚动加载更多
}
</script>

<style scoped>
.post-detail {
  max-width: 680px;
  margin: 0 auto;
}

.post-card {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  margin-bottom: 24px;
}

.post-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.post-info {
  display: flex;
  flex-direction: column;
}

.username {
  font-weight: 600;
  color: #333;
  font-size: 16px;
}

.username:hover {
  color: #409eff;
}

.time {
  font-size: 13px;
  color: #999;
}

.post-content {
  margin-bottom: 16px;
}

.post-content p {
  white-space: pre-wrap;
  word-break: break-word;
  line-height: 1.8;
  font-size: 15px;
  margin-bottom: 16px;
}

.resource-link {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 12px 16px;
  background: #f0f2f5;
  border-radius: 8px;
  color: #409eff;
  margin-bottom: 16px;
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

.post-actions {
  display: flex;
  gap: 24px;
  padding-top: 16px;
  border-top: 1px solid #eee;
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

.comments-section {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
}

.comments-section h3 {
  margin-bottom: 16px;
}

.comment-form {
  margin-bottom: 24px;
}

.comment-form .el-button {
  margin-top: 12px;
}

.login-prompt {
  color: #666;
  font-size: 14px;
}

.login-prompt a {
  color: #409eff;
  text-decoration: none;
  font-weight: 500;
}

.login-prompt a:hover {
  text-decoration: underline;
}

.comment-item {
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
  position: relative;
}

.comment-info {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.comment-content {
  margin-left: 52px;
}

.comment-content p {
  white-space: pre-wrap;
  word-break: break-word;
  line-height: 1.6;
  font-size: 14px;
  color: #333;
  margin: 0;
}

.comment-actions {
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
}

.load-more {
  text-align: center;
  padding: 16px 0;
}
</style>