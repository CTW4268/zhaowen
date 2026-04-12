<template>
  <div class="feedback-page">
    <nav class="top-nav">
      <div class="nav-left">
        <router-link to="/" class="nav-link">← 返回首页</router-link>
      </div>
      <div class="nav-center">
        <h2 class="nav-title">帮助反馈</h2>
      </div>
      <div class="nav-right"></div>
    </nav>

    <main class="container feedback-container">
      <section class="feedback-intro">
        <h1>您的反馈对我们很重要</h1>
        <p>请告诉我们您的建议、问题或使用体验，我们会认真听取并改进。</p>
      </section>

      <section class="feedback-form-section">
        <form @submit.prevent="submitFeedback" class="feedback-form">
          <div class="form-group">
            <label for="feedback-type">反馈类型</label>
            <select id="feedback-type" v-model="feedback.type" required>
              <option value="">请选择反馈类型</option>
              <option value="bug">Bug 报告</option>
              <option value="feature">功能建议</option>
              <option value="ui">界面改进</option>
              <option value="other">其他</option>
            </select>
          </div>

          <div class="form-group">
            <label for="subject">主题</label>
            <input
              id="subject"
              v-model="feedback.subject"
              type="text"
              placeholder="简要描述您的反馈主题"
              required
            />
          </div>

          <div class="form-group">
            <label for="message">详细描述</label>
            <textarea
              id="message"
              v-model="feedback.message"
              placeholder="请详细描述您的问题、建议或使用体验..."
              rows="6"
              required
            ></textarea>
          </div>

          <div class="form-group">
            <label for="contact">联系方式（可选）</label>
            <input
              id="contact"
              v-model="feedback.contact"
              type="text"
              placeholder="邮箱或电话，便于我们联系您"
            />
          </div>

          <button type="submit" class="submit-btn" :disabled="isSubmitting">
            {{ isSubmitting ? '提交中...' : '提交反馈' }}
          </button>
        </form>

        <div v-if="submitSuccess" class="success-message">
          <p>感谢您的反馈！我们会尽快处理。</p>
        </div>
      </section>

      <section class="other-help">
        <h2>其他帮助方式</h2>
        <ul>
          <li><router-link to="/help">查看帮助中心</router-link></li>
          <li>邮箱：support@newsapp.com</li>
          <li>电话：400-123-4567</li>
        </ul>
      </section>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const feedback = ref({
  type: '',
  subject: '',
  message: '',
  contact: ''
})

const isSubmitting = ref(false)
const submitSuccess = ref(false)

const submitFeedback = async () => {
  if (!feedback.value.type || !feedback.value.subject || !feedback.value.message) {
    alert('请填写所有必填字段')
    return
  }

  isSubmitting.value = true

  // 模拟提交过程
  try {
    await new Promise(resolve => setTimeout(resolve, 2000)) // 模拟网络请求
    console.log('Feedback submitted:', feedback.value)
    submitSuccess.value = true
    feedback.value = { type: '', subject: '', message: '', contact: '' }
  } catch (error) {
    console.error('Feedback submission error:', error)
    alert('提交失败，请稍后重试')
  } finally {
    isSubmitting.value = false
  }
}
</script>

<style scoped>
.feedback-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding-top: 80px;
}

.top-nav {
  position: fixed;
  inset: 0 0 auto 0;
  height: 66px;
  background: linear-gradient(135deg, #c41e3a 0%, #8b0000 100%);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
  z-index: 1000;
}

.nav-center {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}

.nav-title {
  color: #fff;
  font-size: 18px;
  font-weight: 700;
}

.container {
  max-width: 1100px;
  margin: 20px auto;
  padding: 0 16px;
}

.feedback-intro {
  background: #fff;
  padding: 24px;
  border-radius: 10px;
  margin-bottom: 20px;
  text-align: center;
}

.feedback-intro h1 {
  margin: 0 0 12px;
  color: #2c3e50;
}

.feedback-intro p {
  margin: 0;
  color: #666;
}

.feedback-form-section {
  background: #fff;
  padding: 24px;
  border-radius: 10px;
  margin-bottom: 20px;
}

.feedback-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  font-weight: 600;
  color: #2c3e50;
}

.form-group input,
.form-group select,
.form-group textarea {
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  background: #fafafa;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #c41e3a;
  box-shadow: 0 0 0 3px rgba(196, 30, 58, 0.14);
}

.submit-btn {
  padding: 14px 0;
  border: none;
  border-radius: 10px;
  background: linear-gradient(135deg, #c41e3a 0%, #8b0000 100%);
  color: #ffffff;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 10px 20px rgba(196, 30, 58, 0.4);
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.success-message {
  margin-top: 20px;
  padding: 16px;
  background: #d4edda;
  border: 1px solid #c3e6cb;
  border-radius: 8px;
  color: #155724;
  text-align: center;
}

.other-help {
  background: #fff;
  padding: 24px;
  border-radius: 10px;
}

.other-help h2 {
  margin: 0 0 16px;
  color: #2c3e50;
}

.other-help ul {
  margin: 0;
  padding-left: 20px;
}

.other-help li {
  margin-bottom: 8px;
  color: #666;
}

.other-help a {
  color: #c41e3a;
  text-decoration: none;
}

.other-help a:hover {
  text-decoration: underline;
}
</style>