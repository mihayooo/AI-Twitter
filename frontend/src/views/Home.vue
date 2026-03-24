<template>
  <div class="home">
    <div class="feed-tabs">
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="关注" name="timeline" v-if="authStore.isAuthenticated" />
        <el-tab-pane label="最新" name="latest" />
      </el-tabs>
    </div>

    <div class="post-list" v-loading="postStore.loading">
      <template v-if="postStore.posts.length > 0">
        <PostCard
          v-for="post in postStore.posts"
          :key="post.id"
          :post="post"
          @like="handleLike"
          @bookmark="handleBookmark"
          @delete="handleDelete"
        />
        <div class="load-more" v-if="postStore.hasMore">
          <el-button @click="loadMore" :loading="postStore.loading">加载更多</el-button>
        </div>
      </template>
      <el-empty v-else description="还没有帖子，快来发布第一篇吧！" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { usePostStore } from '@/stores/post'
import { useAuthStore } from '@/stores/auth'
import PostCard from '@/components/PostCard.vue'

const postStore = usePostStore()
const authStore = useAuthStore()
const activeTab = ref('latest')

onMounted(() => {
  loadPosts()
})

function handleTabChange(tab) {
  postStore.reset()
  loadPosts()
}

function loadPosts() {
  if (activeTab.value === 'timeline') {
    postStore.getTimeline({ page: 1 })
  } else {
    postStore.getPosts({ page: 1 })
  }
}

function loadMore() {
  const nextPage = postStore.page + 1
  if (activeTab.value === 'timeline') {
    postStore.getTimeline({ page: nextPage })
  } else {
    postStore.getPosts({ page: nextPage })
  }
}

function handleLike(postId) {
  postStore.toggleLike(postId)
}

function handleBookmark(postId) {
  postStore.toggleBookmark(postId)
}

function handleDelete(postId) {
  postStore.deletePost(postId)
}
</script>

<style scoped>
.home {
  max-width: 680px;
  margin: 0 auto;
}

.feed-tabs {
  background: #fff;
  padding: 0 16px;
  border-radius: 8px 8px 0 0;
  margin-bottom: 2px;
}

.feed-tabs :deep(.el-tabs__header) {
  margin: 0;
}

.post-list {
  background: #fff;
  border-radius: 0 0 8px 8px;
  min-height: 400px;
}

.load-more {
  padding: 20px;
  text-align: center;
}
</style>