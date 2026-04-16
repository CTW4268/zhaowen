import { ref, computed, watch } from 'vue'
import { login as apiLogin, register as apiRegister, type LoginResponse } from '@/api/auth'

const STORAGE_KEY = 'news_app_user'
const TOKEN_KEY = 'news_app_token'

const user = ref<string | null>(null)
const token = ref<string | null>(null)

// 从本地存储加载用户信息
const loadUserFromStorage = () => {
  try {
    const raw = localStorage.getItem(STORAGE_KEY)
    const storedToken = localStorage.getItem(TOKEN_KEY)
    if (raw) {
      user.value = JSON.parse(raw) as string
    }
    if (storedToken) {
      token.value = storedToken
    }
  } catch {
    user.value = null
    token.value = null
  }
}

// 保存用户信息到本地存储
const saveUserToStorage = () => {
  if (user.value) {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(user.value))
  } else {
    localStorage.removeItem(STORAGE_KEY)
  }
}

// 保存 Token 到本地存储
const saveTokenToStorage = () => {
  if (token.value) {
    localStorage.setItem(TOKEN_KEY, token.value)
  } else {
    localStorage.removeItem(TOKEN_KEY)
  }
}

// 监听变化并自动保存
watch(user, saveUserToStorage, { immediate: true })
watch(token, saveTokenToStorage, { immediate: true })

// 初始化时加载
loadUserFromStorage()

export function useAuth() {
  const isLoggedIn = computed(() => !!user.value && !!token.value)

  /**
   * 用户登录
   */
  const login = async (username: string, password: string) => {
    try {
      // 响应拦截器已经解包了 ApiResponse，直接返回 LoginResponse
      const response = await apiLogin({ username, password }) as unknown as LoginResponse
      user.value = response.username
      token.value = response.token
      return response
    } catch (error) {
      console.error('登录失败:', error)
      throw error
    }
  }

  /**
   * 用户注册
   */
  const register = async (username: string, password: string) => {
    try {
      const response = await apiRegister({ username, password }) as unknown as LoginResponse
      user.value = response.username
      token.value = response.token
      return response
    } catch (error) {
      console.error('注册失败:', error)
      throw error
    }
  }

  /**
   * 退出登录
   */
  const logout = () => {
    user.value = null
    token.value = null
    localStorage.removeItem(STORAGE_KEY)
    localStorage.removeItem(TOKEN_KEY)
  }

  return {
    user,
    token,
    isLoggedIn,
    login,
    register,
    logout,
  }
}
