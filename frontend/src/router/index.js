import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const routes = [
  {
    path: '/',
    component: () => import('@/views/Layout.vue'),
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('@/views/Home.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'explore',
        name: 'Explore',
        component: () => import('@/views/Explore.vue')
      },
      {
        path: 'explore/tags',
        name: 'ExploreTags',
        component: () => import('@/views/ExploreTags.vue')
      },
      {
        path: 'profile/:id',
        name: 'Profile',
        component: () => import('@/views/Profile.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'post/:id',
        name: 'PostDetail',
        component: () => import('@/views/PostDetail.vue')
      },
      {
        path: 'create',
        name: 'CreatePost',
        component: () => import('@/views/CreatePost.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'messages',
        name: 'Messages',
        component: () => import('@/views/Messages.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'admin/review',
        name: 'AdminReview',
        component: () => import('@/views/AdminReview.vue'),
        meta: { requiresAuth: true }
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()

  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
  } else if ((to.name === 'Login' || to.name === 'Register') && authStore.isAuthenticated) {
    next({ name: 'Home' })
  } else {
    next()
  }
})

export default router