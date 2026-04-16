<template>
  <div class="help-center-page">
    <nav class="top-nav">
      <div class="nav-left">
        <router-link to="/" class="nav-link">← 返回首页</router-link>
      </div>
      <div class="nav-center">
        <h2 class="nav-title">帮助中心</h2>
      </div>
      <div class="nav-right"></div>
    </nav>

    <main class="container help-container">
      <section class="help-intro">
        <h1>欢迎使用新闻中心</h1>
        <p>这里是常见问题解答和使用指南，帮助您更好地使用我们的新闻应用。</p>
      </section>

      <section class="faq-section">
        <h2>常见问题</h2>
        <div v-if="loading" class="loading">加载中...</div>
        <div v-else class="faq-list">
          <details v-for="(faq, index) in faqList" :key="index" class="faq-item">
            <summary>{{ faq.question }}</summary>
            <p>{{ faq.answer }}</p>
          </details>
        </div>
      </section>

      <section class="contact-section">
        <h2>联系我们</h2>
        <p>如果您的问题没有得到解答，请通过以下方式联系我们：</p>
        <ul>
          <li>邮箱：support@newsapp.com</li>
          <li>电话：400-123-4567</li>
          <li><router-link to="/feedback">提交反馈</router-link></li>
        </ul>
      </section>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getFAQ } from '@/api/help'
import type { FAQItem } from '@/api/help'

const faqList = ref<FAQItem[]>([])
const loading = ref(false)

// 加载FAQ数据
const loadFAQ = async () => {
  loading.value = true
  try {
    const data = await getFAQ()
    faqList.value = data || []
  } catch (error) {
    console.error('加载FAQ失败:', error)
    // 如果API失败，使用默认数据
    faqList.value = [
      { question: '如何收藏新闻？', answer: '在新闻详情页或列表中，点击收藏按钮即可将新闻添加到个人收藏中。' },
      { question: '如何查看阅读历史？', answer: '点击左上角菜单，选择"阅读历史"即可查看您已阅读的新闻记录。' },
      { question: '如何切换国内和海外新闻？', answer: '在首页点击"国内"或"海外"按钮，或在顶部导航中选择相应页面。' },
      { question: '如何登录账户？', answer: '点击右上角用户图标，输入用户名和密码即可登录。' },
      { question: '如何注册新账户？', answer: '在登录弹窗中点击"立即注册"链接，填写用户名和密码完成注册。' }
    ]
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadFAQ()
})
</script>

<style scoped>
.help-center-page {
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

.help-intro {
  background: #fff;
  padding: 24px;
  border-radius: 10px;
  margin-bottom: 20px;
  text-align: center;
}

.help-intro h1 {
  margin: 0 0 12px;
  color: #2c3e50;
}

.help-intro p {
  margin: 0;
  color: #666;
}

.faq-section {
  background: #fff;
  padding: 24px;
  border-radius: 10px;
  margin-bottom: 20px;
}

.faq-section h2 {
  margin: 0 0 20px;
  color: #2c3e50;
}

.faq-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.faq-item {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  overflow: hidden;
}

.faq-item summary {
  padding: 16px;
  background: #f8f9fa;
  cursor: pointer;
  font-weight: 600;
  color: #2c3e50;
}

.faq-item p {
  margin: 0;
  padding: 16px;
  color: #666;
  line-height: 1.6;
}

.contact-section {
  background: #fff;
  padding: 24px;
  border-radius: 10px;
}

.contact-section h2 {
  margin: 0 0 16px;
  color: #2c3e50;
}

.contact-section p {
  margin: 0 0 16px;
  color: #666;
}

.contact-section ul {
  margin: 0;
  padding-left: 20px;
}

.contact-section li {
  margin-bottom: 8px;
  color: #666;
}

.contact-section a {
  color: #c41e3a;
  text-decoration: none;
}

.contact-section a:hover {
  text-decoration: underline;
}
</style>
