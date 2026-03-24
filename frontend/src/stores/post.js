import { defineStore } from 'pinia'
import { ref } from 'vue'
import {
  getPosts as apiGetPosts,
  getTimeline as apiGetTimeline,
  getUserPosts as apiGetUserPosts,
  getPost as apiGetPost,
  createPost as apiCreatePost,
  updatePost as apiUpdatePost,
  deletePost as apiDeletePost,
  likePost as apiLikePost,
  bookmarkPost as apiBookmarkPost
} from '@/api/posts'

export const usePostStore = defineStore('post', () => {
  const posts = ref([])
  const currentPost = ref(null)
  const loading = ref(false)
  const page = ref(1)
  const hasMore = ref(true)

  async function getPosts(params = {}) {
    loading.value = true;
    try {
      const res = await apiGetPosts(params)
      if (res.data.code === 200) {
        if (params.page === 1) {
          posts.value = res.data.data;
        } else {
          posts.value.push(...res.data.data)
        }
        hasMore.value = res.data.data.length >= (params.size || 20);
        page.value = params.page || 1;
        return { success: true, data: res.data.data }
      }
      return { success: false, message: res.data.message }
    } catch (error) {
      return { success: false, message: error.response?.data?.message || '获取帖子失败' }
    } finally {
      loading.value = false;
    }
  }

  async function getTimeline(params = {}) {
    loading.value = true;
    try {
      const res = await apiGetTimeline(params)
      if (res.data.code === 200) {
        if (params.page === 1) {
          posts.value = res.data.data;
        } else {
          posts.value.push(...res.data.data)
        }
        hasMore.value = res.data.data.length >= (params.size || 20);
        page.value = params.page || 1;
        return { success: true, data: res.data.data }
      }
      return { success: false, message: res.data.message }
    } catch (error) {
      return { success: false, message: error.response?.data?.message || '获取时间线失败' }
    } finally {
      loading.value = false;
    }
  }

  async function getUserPosts(userId, params = {}) {
    loading.value = true;
    try {
      const res = await apiGetUserPosts(userId, params)
      if (res.data.code === 200) {
        if (params.page === 1) {
          posts.value = res.data.data;
        } else {
          posts.value.push(...res.data.data)
        }
        hasMore.value = res.data.data.length >= (params.size || 20);
        return { success: true, data: res.data.data }
      }
      return { success: false, message: res.data.message }
    } catch (error) {
      return { success: false, message: error.response?.data?.message || '获取用户帖子失败' }
    } finally {
      loading.value = false;
    }
  }

  async function getPost(id) {
    loading.value = true;
    try {
      const res = await apiGetPost(id)
      if (res.data.code === 200) {
        currentPost.value = res.data.data;
        return { success: true, data: res.data.data }
      }
      return { success: false, message: res.data.message }
    } catch (error) {
      return { success: false, message: error.response?.data?.message || '获取帖子失败' }
    } finally {
      loading.value = false;
    }
  }

  async function createPost(postData) {
    loading.value = true;
    try {
      const res = await apiCreatePost(postData)
      if (res.data.code === 200) {
        posts.value.unshift(res.data.data)
        return { success: true, data: res.data.data }
      }
      return { success: false, message: res.data.message }
    } catch (error) {
      return { success: false, message: error.response?.data?.message || '发布帖子失败' }
    } finally {
      loading.value = false;
    }
  }

  async function updatePost(id, postData) {
    loading.value = true;
    try {
      const res = await apiUpdatePost(id, postData)
      if (res.data.code === 200) {
        const index = posts.value.findIndex(p => p.id === id)
        if (index !== -1) {
          posts.value[index] = res.data.data
        }
        return { success: true, data: res.data.data }
      }
      return { success: false, message: res.data.message }
    } catch (error) {
      return { success: false, message: error.response?.data?.message || '更新帖子失败' }
    } finally {
      loading.value = false;
    }
  }

  async function deletePost(id) {
    loading.value = true;
    try {
      const res = await apiDeletePost(id)
      if (res.data.code === 200) {
        posts.value = posts.value.filter(p => p.id !== id);
        return { success: true }
      }
      return { success: false, message: res.data.message }
    } catch (error) {
      return { success: false, message: error.response?.data?.message || '删除帖子失败' }
    } finally {
      loading.value = false;
    }
  }

  async function toggleLike(postId) {
    try {
      const res = await apiLikePost(postId)
      if (res.data.code === 200) {
        const post = posts.value.find(p => p.id === postId)
        if (post) {
          post.liked = res.data.data
          post.likeCount += res.data.data ? 1 : -1
        }
        if (currentPost.value && currentPost.value.id === postId) {
          currentPost.value.liked = res.data.data
          currentPost.value.likeCount += res.data.data ? 1 : -1
        }
        return { success: true, liked: res.data.data }
      }
      return { success: false, message: res.data.message }
    } catch (error) {
      return { success: false, message: error.response?.data?.message || '操作失败' }
    }
  }

  async function toggleBookmark(postId) {
    try {
      const res = await apiBookmarkPost(postId)
      if (res.data.code === 200) {
        const post = posts.value.find(p => p.id === postId)
        if (post) {
          post.bookmarked = res.data.data
        }
        if (currentPost.value && currentPost.value.id === postId) {
          currentPost.value.bookmarked = res.data.data
        }
        return { success: true, bookmarked: res.data.data }
      }
      return { success: false, message: res.data.message }
    } catch (error) {
      return { success: false, message: error.response?.data?.message || '操作失败' }
    }
  }

  function reset() {
    posts.value = [];
    currentPost.value = null;
    page.value = 1;
    hasMore.value = true;
  }

  return {
    posts,
    currentPost,
    loading,
    page,
    hasMore,
    getPosts,
    getTimeline,
    getUserPosts,
    getPost,
    createPost,
    updatePost,
    deletePost,
    toggleLike,
    toggleBookmark,
    reset
  }
})