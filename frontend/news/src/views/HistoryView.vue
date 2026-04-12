<template>
  <div class="history-page">
    <nav class="top-nav">
      <div class="nav-left">
        <router-link to="/" class="nav-link">← 返回首页</router-link>
      </div>
      <div class="nav-center">
        <h2 class="nav-title">阅读历史</h2>
      </div>
      <div class="nav-right"></div>
    </nav>

    <main class="container history-container">
      <section class="history-header">
        <p>共 {{ historyList.length }} 条已读记录</p>
        <button class="clear-btn" @click="clearHistory">清空历史</button>
      </section>

      <section class="history-list">
        <article v-if="historyList.length === 0" class="empty">暂无阅读历史，去首页阅读新闻吧。</article>

        <article v-for="item in historyList" :key="item.id" class="news-item" @click="openNews(item)">
          <img :src="item.cover" alt="已读新闻" class="news-image" />
          <div class="news-content">
            <h3 class="news-title">{{ item.title }}</h3>
            <p class="news-summary">{{ item.summary }}</p>
            <small class="news-meta">{{ item.category }} · {{ item.readAt }}</small>
          </div>
        </article>
      </section>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const historyList = ref([
  {
    id: 'h1',
    title: '已读：全球经济前瞻',
    summary: '你在 3 小时前阅读该条新闻，了解最新全球经济走势。',
    category: '海外',
    readAt: '2026-03-27 09:20',
    cover: 'https://via.placeholder.com/280x160/8CC152/FFFFFF?text=阅读历史'
  },
  {
    id: 'h2',
    title: '已读：国内政策解读',
    summary: '你在 1 天前阅读，关注政策变化带来的机会。',
    category: '国内',
    readAt: '2026-03-26 16:40',
    cover: 'https://via.placeholder.com/280x160/FF6B6B/FFFFFF?text=阅读历史2'
  }
])

const clearHistory = () => {
  if (confirm('确认清空所有阅读历史？')) {
    historyList.value = []
  }
}

const openNews = (item: { id: string }) => {
  router.push({ path: '/', query: { history: item.id } })
}
</script>

<style scoped>
.history-page {
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

.history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
}

.clear-btn {
  border: 1px solid #c41e3a;
  border-radius: 10px;
  background: #fff;
  color: #c41e3a;
  font-weight: 600;
  padding: 10px 14px;
  cursor: pointer;
}

.history-list .news-item {
  display: flex;
  gap: 14px;
  margin-bottom: 14px;
  padding: 12px;
  background: #fff;
  border-radius: 10px;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.news-image {
  width: 140px;
  height: 88px;
  border-radius: 8px;
  object-fit: cover;
  flex-shrink: 0;
}

.news-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.news-title {
  margin: 0 0 8px;
  font-size: 16px;
  font-weight: 700;
}

.news-summary {
  margin: 0;
  color: #666;
  font-size: 14px;
  line-height: 1.4;
}

.news-meta {
  margin-top: 8px;
  color: #999;
  font-size: 12px;
}

.empty {
  padding: 24px;
  background: #fff;
  border-radius: 10px;
  color: #999;
  text-align: center;
}
</style>
