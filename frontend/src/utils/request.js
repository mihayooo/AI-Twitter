import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    return response
  },
  error => {
    if (error.response) {
      const { status, data } = error.response

      if (status === 401) {
        localStorage.removeItem('token')
        window.location.href = '/login'
        ElMessage.error('登录已过期，请重新登录')
      } else if (status === 403) {
        ElMessage.error(data.message || '没有权限')
      } else if (status === 404) {
        ElMessage.error(data.message || '资源不存在')
      } else if (status >= 500) {
        ElMessage.error('服务器错误')
      } else if (data?.message) {
        ElMessage.error(data.message)
      }
    } else {
      ElMessage.error('网络错误')
    }
    return Promise.reject(error)
  }
)

export default request