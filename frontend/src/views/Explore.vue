<template>
  <div class="explore">
    <div class="header-actions">
      <router-link to="/explore/tags">
        <el-button type="primary" :icon="TrendCharts" round>
          热门标签
        </el-button>
      </router-link>
    </div>

    <div class="search-bar">
      <el-input
        v-model="searchTag"
        placeholder="搜索标签..."
        :prefix-icon="Search"
        @keyup.enter="handleSearch"
        clearable
      />
    </div>

    <div class="post-list" v-loading="loading">
      <template v-if="posts.length > 0">
        <PostCard
          v-for="post in posts"
          :key="post.id"
          :post="post"
          @like="handleLike"
          @bookmark="handleBookmark"
        />
        <div class="load-more" v-if="hasMore">
          <el-button @click="loadMore" :loading="loading">加载更多</el-button>
        </div>
      </template>
      <el-empty v-else description="暂无相关内容" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getPosts } from '@/api/posts'
import { useAuthStore } from '@/stores/auth'
import { usePostStore } from '@/stores/post'
import PostCard from '@/components/PostCard.vue'
import { Search } from '@element-plus/icons-vue'

const route = useRoute()
const authStore = useAuthStore()
const postStore = usePostStore()

const searchTag = ref('')
const posts = ref([])
const loading = ref(false)
const page = ref(1)
const hasMore = ref(true)

onMounted(() => {
  if (route.query.tag) {
    searchTag.value = route.query.tag
  }
  loadPosts()
})

watch(() => route.query.tag, (newTag) => {
  if (newTag) {
    searchTag.value = newTag
    resetAndLoad()
  }
})

function handleSearch() {
  resetAndLoad()
}

function resetAndLoad() {
  page.value = 1
  posts.value = []
  loadPosts()
}

async function loadPosts() {
  loading.value = true
  try {
    const params = {
      page: page.value,
      size: 20
    }
    if (searchTag.value) {
      params.tag = searchTag.value
    }

    const res = await getPosts(params)
    if (res.data.code === 200) {
      if (page.value === 1) {
        posts.value = res.data.data
      } else {
        posts.value.push(...res.data.data)
      }
      hasMore.value = res.data.data.length >= 20
    }
  } finally {
    loading.value = false
  }
}

function loadMore() {
  page.value++
  loadPosts()
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
</script>

<style scoped>
.explore {
  max-width: 680px;
  margin: 0 auto;
}

.search-bar {
  margin-bottom: 16px;
}

.post-list {
  background: #fff;
  border-radius: 8px;
  min-height: 400px;
}

.load-more {
  padding: 20px;
  text-align: center;
}
</style>