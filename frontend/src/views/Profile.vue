<template>
  <div class="profile" v-loading="loading">
    <div class="profile-header" v-if="user">
      <div class="avatar-section">
        <el-avatar :size="100" :src="user.avatar || '/default-avatar.png'">
          {{ user.username?.charAt(0).toUpperCase() }}
        </el-avatar>
      </div>

      <div class="info-section">
        <h1>{{ user.username }}</h1>
        <p class="bio" v-if="user.bio">{{ user.bio }}</p>
        <p class="bio" v-else>这个人很懒，什么都没留下~</p>

        <div class="stats">
          <div class="stat-item" @click="showFollowers">
            <span class="stat-value">{{ user.followerCount || 0 }}</span>
            <span class="stat-label">粉丝</span>
          </div>
          <div class="stat-item" @click="showFollowing">
            <span class="stat-value">{{ user.followingCount || 0 }}</span>
            <span class="stat-label">关注</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ user.postCount || 0 }}</span>
            <span class="stat-label">帖子</span>
          </div>
        </div>

        <div class="actions" v-if="!isSelf">
          <el-button
            :type="user.isFollowing ? 'default' : 'primary'"
            @click="handleFollow"
            :loading="followLoading"
          >
            {{ user.isFollowing ? '取消关注' : '关注' }}
          </el-button>
        </div>
        <div class="actions" v-else>
          <el-button @click="showEditDialog = true">编辑资料</el-button>
        </div>
      </div>
    </div>

    <div class="profile-posts">
      <h3>TA的帖子</h3>
      <div class="post-list">
        <PostCard
          v-for="post in posts"
          :key="post.id"
          :post="post"
          @like="handleLike"
          @bookmark="handleBookmark"
        />
        <el-empty v-if="posts.length === 0 && !loading" description="还没有发布任何帖子" />
      </div>
    </div>

    <!-- 编辑资料对话框 -->
    <el-dialog v-model="showEditDialog" title="编辑资料" width="500px">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="editForm.username" />
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="editForm.bio" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="handleUpdateProfile" :loading="updateLoading">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { usePostStore } from '@/stores/post'
import { getUser, updateProfile, followUser } from '@/api/users'
import { getUserPosts } from '@/api/posts'
import PostCard from '@/components/PostCard.vue'
import { ElMessage } from 'element-plus'

const route = useRoute()
const authStore = useAuthStore()
const postStore = usePostStore()

const loading = ref(false)
const user = ref(null)
const posts = ref([])
const showEditDialog = ref(false)
const followLoading = ref(false)
const updateLoading = ref(false)

const editForm = reactive({
  username: '',
  bio: ''
})

const isSelf = computed(() => {
  return authStore.user && authStore.user.id === user.value?.id
})

const userId = computed(() => route.params.id)

watch(userId, () => {
  loadData()
}, { immediate: true })

function loadData() {
  loadUser()
  loadPosts()
}

async function loadUser() {
  loading.value = true
  try {
    const res = await getUser(userId.value)
    if (res.data.code === 200) {
      user.value = res.data.data
      editForm.username = user.value.username
      editForm.bio = user.value.bio
    }
  } finally {
    loading.value = false
  }
}

async function loadPosts() {
  try {
    const res = await getUserPosts(userId.value, { page: 1 })
    if (res.data.code === 200) {
      posts.value = res.data.data
    }
  } catch (e) {
    console.error(e)
  }
}

async function handleFollow() {
  followLoading.value = true
  try {
    const res = await followUser(userId.value)
    if (res.data.code === 200) {
      user.value.isFollowing = res.data.data
      if (res.data.data) {
        user.value.followerCount++
      } else {
        user.value.followerCount--
      }
      ElMessage.success(res.data.data ? '关注成功' : '取消关注')
    }
  } finally {
    followLoading.value = false
  }
}

async function handleUpdateProfile() {
  updateLoading.value = true
  try {
    const res = await updateProfile(editForm)
    if (res.data.code === 200) {
      user.value = res.data.data
      authStore.user = res.data.data
      showEditDialog.value = false
      ElMessage.success('保存成功')
    }
  } finally {
    updateLoading.value = false
  }
}

function handleLike(postId) {
  postStore.toggleLike(postId).then(result => {
    if (result.success) {
      const post = posts.value.find(p => p.id === postId)
      if (post) {
        post.liked = result.liked
        post.likeCount += result.liked ? 1 : -1
      }
    }
  })
}

function handleBookmark(postId) {
  postStore.toggleBookmark(postId).then(result => {
    if (result.success) {
      const post = posts.value.find(p => p.id === postId)
      if (post) {
        post.bookmarked = result.bookmarked
      }
    }
  })
}

function showFollowers() {
  // TODO: 显示粉丝列表
}

function showFollowing() {
  // TODO: 显示关注列表
}
</script>

<style scoped>
.profile {
  max-width: 800px;
  margin: 0 auto;
}

.profile-header {
  background: #fff;
  border-radius: 8px;
  padding: 32px;
  display: flex;
  gap: 32px;
  margin-bottom: 24px;
}

.avatar-section {
  flex-shrink: 0;
}

.info-section h1 {
  margin: 0 0 8px;
  font-size: 24px;
}

.bio {
  color: #666;
  margin-bottom: 16px;
}

.stats {
  display: flex;
  gap: 24px;
  margin-bottom: 16px;
}

.stat-item {
  cursor: pointer;
}

.stat-value {
  font-weight: 600;
  font-size: 18px;
  margin-right: 4px;
}

.stat-label {
  color: #999;
}

.actions {
  margin-top: 16px;
}

.profile-posts h3 {
  margin-bottom: 16px;
  color: #333;
}

.post-list {
  background: #fff;
  border-radius: 8px;
}
</style>