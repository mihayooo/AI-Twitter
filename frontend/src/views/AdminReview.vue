<template>
  <div class="admin-review">
    <div class="page-header">
      <h2>内容审核</h2>
      <p class="subtitle">审核待发布的帖子内容</p>
    </div>

    <div v-loading="loading" class="review-list">
      <el-empty v-if="!loading && posts.length === 0" description="暂无待审核的帖子" />

      <div v-for="post in posts" :key="post.id" class="post-item">
        <div class="post-card">
          <div class="post-header">
            <router-link :to="`/profile/${post.userId}`">
              <el-avatar :size="40" :src="post.userAvatar || '/default-avatar.png'">
                {{ post.username?.charAt(0).toUpperCase() }}
              </el-avatar>
            </router-link>
            <div class="post-info">
              <router-link :to="`/profile/${post.userId}`" class="username">
                {{ post.username }}
              </router-link>
              <span class="time">{{ formatTime(post.createdAt) }}</span>
              <el-tag size="small" type="warning">待审核</el-tag>
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
        </div>

        <div class="review-actions">
          <el-button
            type="success"
            size="large"
            @click="handleReview(post.id, 'approved')"
            :loading="reviewing === post.id"
          >
            <el-icon><Check /></el-icon>
            通过
          </el-button>
          <el-button
            type="danger"
            size="large"
            @click="openRejectDialog(post)"
            :loading="rejecting === post.id"
          >
            <el-icon><Close /></el-icon>
            拒绝
          </el-button>
        </div>
      </div>
    </div>

    <!-- 拒绝对话框 -->
    <el-dialog
      v-model="rejectDialogVisible"
      title="拒绝审核"
      width="500px"
    >
      <el-form :model="rejectForm" label-position="top">
        <el-form-item label="拒绝原因（可选）">
          <el-input
            v-model="rejectForm.reviewNote"
            type="textarea"
            :rows="4"
            placeholder="请输入拒绝原因，将通知用户"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectDialogVisible = false">取消</el-button>
        <el-button
          type="danger"
          @click="handleReject"
          :loading="rejecting === currentPostId"
        >
          确认拒绝
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getPendingPosts, reviewPost } from '@/api/admin'
import { ElMessage } from 'element-plus'
import { Check, Close, Link } from '@element-plus/icons-vue'

const router = useRouter()

const loading = ref(false)
const posts = ref([])
const reviewing = ref(null)
const rejecting = ref(null)
const rejectDialogVisible = ref(false)
const currentPostId = ref(null)
const rejectForm = ref({
  reviewNote: ''
})

onMounted(() => {
  loadPendingPosts()
})

async function loadPendingPosts() {
  loading.value = true
  try {
    const res = await getPendingPosts({ page: 1, size: 100 })
    if (res.success) {
      posts.value = res.data
    }
  } catch (error) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

async function handleReview(postId, status) {
  reviewing.value = postId
  try {
    const res = await reviewPost(postId, { status })
    if (res.success) {
      ElMessage.success('审核通过')
      posts.value = posts.value.filter(p => p.id !== postId)
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '操作失败')
  } finally {
    reviewing.value = null
  }
}

function openRejectDialog(post) {
  currentPostId.value = post.id
  rejectForm.value.reviewNote = ''
  rejectDialogVisible.value = true
}

async function handleReject() {
  if (rejecting.value === currentPostId.value) return

  rejecting.value = currentPostId.value
  try {
    const res = await reviewPost(currentPostId.value, {
      status: 'rejected',
      reviewNote: rejectForm.value.reviewNote || '未通过审核'
    })
    if (res.success) {
      ElMessage.success('已拒绝')
      rejectDialogVisible.value = false
      posts.value = posts.value.filter(p => p.id !== currentPostId.value)
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '操作失败')
  } finally {
    rejecting.value = null
    currentPostId.value = null
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
</script>

<style scoped>
.admin-review {
  max-width: 800px;
  margin: 0 auto;
}

.page-header {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%);
  color: white;
  padding: 40px 24px;
  border-radius: 16px;
  margin-bottom: 24px;
  text-align: center;
}

.page-header h2 {
  margin: 0 0 8px 0;
  font-size: 28px;
}

.subtitle {
  margin: 0;
  opacity: 0.9;
}

.review-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.post-item {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.post-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.post-info {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.username {
  font-weight: 600;
  color: #333;
  text-decoration: none;
  font-size: 15px;
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
  margin-bottom: 12px;
  color: #333;
}

.resource-link {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 10px 14px;
  background: #f0f2f5;
  border-radius: 8px;
  color: #409eff;
  margin-bottom: 12px;
  text-decoration: none;
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

.review-actions {
  display: flex;
  gap: 16px;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.review-actions .el-button {
  flex: 1;
}
</style>
