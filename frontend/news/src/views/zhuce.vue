<template>
  <div class="register-page">
    <div class="register-card">
      <h1>注册账号</h1>

      <form @submit.prevent="handleSubmit" class="register-form">
        <div class="form-row">
          <label for="username">用户名</label>
          <input
            id="username"
            v-model="username"
            type="text"
            autocomplete="username"
            placeholder="请输入用户名（数字或字母）"
          />
        </div>

        <div class="form-row">
          <label for="password">密码</label>
          <input
            id="password"
            v-model="password"
            type="password"
            autocomplete="new-password"
            placeholder="请输入密码（数字或字母）"
          />
        </div>

        <p v-if="error" class="register-error">{{ error }}</p>

        <button type="submit" class="register-submit">注册</button>

        <p class="register-tip">
          已有账号？
          <a @click.prevent="goLogin" href="#">去登录</a>
        </p>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
defineOptions({ name: 'RegisterView' })

import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuth } from '@/composables/useAuth'

const router = useRouter()
const { register } = useAuth()

const username = ref('')
const password = ref('')
const error = ref('')

const isAlphaNumeric = (value: string) => /^[A-Za-z0-9]+$/.test(value)

const validate = () => {
  if (!username.value || !password.value) {
    error.value = '用户名和密码不能为空'
    return false
  }
  if (!isAlphaNumeric(username.value) || !isAlphaNumeric(password.value)) {
    error.value = '用户名和密码必须由数字或字母组成'
    return false
  }
  if (username.value.length < 3 || username.value.length > 20) {
    error.value = '用户名长度需在 3-20 个字符之间'
    return false
  }
  if (password.value.length < 6 || password.value.length > 24) {
    error.value = '密码长度需在 6-24 个字符之间'
    return false
  }
  error.value = ''
  return true
}

const handleSubmit = async () => {
  if (!validate()) return

  try {
    // 调用真正的注册 API
    await register(username.value, password.value)
    alert('注册成功！')
    router.push('/auth/register')
  } catch (error) {
    console.error('注册失败:', error)
    alert('注册失败，请检查用户名和密码')
  }
}

const goLogin = () => {
  router.push('/')
}
</script>

<style scoped>
.register-page {
  min-height: calc(100vh - 80px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 48px 16px 80px;
  background: #f5f5f5;
}

.register-card {
  width: min(480px, 100%);
  background: #ffffff;
  border-radius: 18px;
  box-shadow: 0 16px 30px rgba(0, 0, 0, 0.12);
  padding: 36px 32px;
  box-sizing: border-box;
}

.register-card h1 {
  margin: 0 0 24px;
  font-size: 28px;
  text-align: center;
  color: #2c3e50;
}

.register-form {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.form-row {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-row label {
  font-weight: 600;
  color: #2c3e50;
}

.form-row input {
  height: 48px;
  padding: 0 14px;
  border-radius: 10px;
  border: 1px solid #d9d9d9;
  font-size: 15px;
  background: #fafafa;
}

.form-row input:focus {
  outline: none;
  border-color: #c41e3a;
  box-shadow: 0 0 0 3px rgba(196, 30, 58, 0.14);
}

.register-error {
  margin: 0;
  color: #c41e3a;
  font-weight: 600;
  text-align: center;
}

.register-submit {
  height: 50px;
  border: none;
  border-radius: 12px;
  font-weight: 700;
  font-size: 16px;
  background: linear-gradient(135deg, #c41e3a 0%, #8b0000 100%);
  color: #ffffff;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

.register-submit:hover {
  transform: translateY(-1px);
  box-shadow: 0 12px 24px rgba(196, 30, 58, 0.35);
}

.register-tip {
  text-align: center;
  color: #667;
  margin: 0;
}

.register-tip a {
  color: #c41e3a;
  text-decoration: none;
  font-weight: 600;
}

.register-tip a:hover {
  text-decoration: underline;
}
</style>
