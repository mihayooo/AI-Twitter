import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as apiLogin, register as apiRegister, getProfile as apiGetProfile } from '@/api/auth'
import router from '@/router'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || '')
  const user = ref(null)
  const loading = ref(false)

  const isAuthenticated = computed(() => !!token.value)

  async function login(loginData) {
    loading.value = true
    try {
      const res = await apiLogin(loginData)
      if (res.data.code === 200) {
        token.value = res.data.data.token
        user.value = res.data.data.user
        localStorage.setItem('token', token.value)
        return { success: true }
      } else {
        return { success: false, message: res.data.message }
      }
    } catch (error) {
      return { success: false, message: error.response?.data?.message || '登录失败' }
    } finally {
      loading.value = false
    }
  }

  async function register(registerData) {
    loading.value = true
    try {
      const res = await apiRegister(registerData)
      if (res.data.code === 200) {
        token.value = res.data.data.token
        user.value = res.data.data.user
        localStorage.setItem('token', token.value)
        return { success: true }
      } else {
        return { success: false, message: res.data.message }
      }
    } catch (error) {
      return { success: false, message: error.response?.data?.message || '注册失败' }
    } finally {
      loading.value = false
    }
  }

  async function fetchProfile() {
    if (!token.value) return
    try {
      const res = await apiGetProfile()
      if (res.data.code === 200) {
        user.value = res.data.data
      }
    } catch (error) {
      logout()
    }
  }

  function logout() {
    token.value = ''
    user.value = null
    localStorage.removeItem('token')
    router.push({ name: 'Login' })
  }

  // 初始化时获取用户信息
  if (token.value) {
    fetchProfile()
  }

  return {
    token,
    user,
    loading,
    isAuthenticated,
    login,
    register,
    fetchProfile,
    logout
  }
})