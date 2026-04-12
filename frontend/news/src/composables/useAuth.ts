import { ref, computed, watch } from 'vue'

const STORAGE_KEY = 'news_app_user'

const user = ref<string | null>(null)

const loadUserFromStorage = () => {
  try {
    const raw = localStorage.getItem(STORAGE_KEY)
    if (raw) {
      user.value = JSON.parse(raw) as string
    }
  } catch {
    user.value = null
  }
}

const saveUserToStorage = () => {
  if (user.value) {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(user.value))
  } else {
    localStorage.removeItem(STORAGE_KEY)
  }
}

watch(user, saveUserToStorage, { immediate: true })

loadUserFromStorage()

export function useAuth() {
  const isLoggedIn = computed(() => !!user.value)

  const login = (username: string) => {
    user.value = username
  }

  const logout = () => {
    user.value = null
  }

  return {
    user,
    isLoggedIn,
    login,
    logout,
  }
}
