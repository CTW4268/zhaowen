<template>
  <div class="news-detail-page">
    <nav class="top-nav">
      <div class="nav-left">
        <router-link to="/" class="nav-link">← 返回首页</router-link>
      </div>
      <div class="nav-center">
        <h2 class="nav-title">新闻详情</h2>
      </div>
      <div class="nav-right"></div>
    </nav>

    <main class="container detail-container">
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <div v-else-if="news" class="news-detail">
        <h1 class="detail-title">{{ news.title }}</h1>

        <div class="detail-meta">
          <span class="meta-item">来源: {{ news.sourceName || '未知' }}</span>
          <span class="meta-item">作者: {{ news.authorName || '未知' }}</span>
          <span class="meta-item">发布时间: {{ formatTime(news.publishTime) }}</span>
          <span class="meta-item">阅读量: {{ news.viewNum || 0 }}</span>
        </div>

        <div class="detail-content" v-html="news.content || news.summary"></div>

        <div class="detail-actions">
          <button
            class="action-btn favorite-btn"
            :class="{ favorited: isFavorited }"
            @click="toggleFavorite"
            :disabled="actionLoading"
          >
            {{ isFavorited ? '已收藏' : '收藏' }}
          </button>

          <button class="action-btn share-btn" @click="shareNews">
            分享
          </button>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getNewsDetail } from '@/api/news'
import { addFavorite, removeFavorite, checkFavorite } from '@/api/favorite'
import type { NewsDTO } from '@/api/news'

const route = useRoute()
const router = useRouter()

const news = ref<NewsDTO | null>(null)
const loading = ref(false)
const error = ref('')
const isFavorited = ref(false)
const actionLoading = ref(false)

// 加载新闻详情
const loadNewsDetail = async () => {
  const id = route.params.id as string
  if (!id) {
    error.value = '新闻ID无效'
    return
  }

  loading.value = true
  try {
    const res = await getNewsDetail(Number(id))
    news.value = res
  } catch (err) {
    console.error('加载新闻详情失败:', err)
    error.value = '加载新闻详情失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

// 检查收藏状态
const checkFavoriteStatus = async () => {
  if (!news.value) return

  try {
    const res = await checkFavorite(news.value.id)
    isFavorited.value = res.favorited
  } catch (err) {
    console.error('检查收藏状态失败:', err)
  }
}

// 切换收藏状态
const toggleFavorite = async () => {
  if (actionLoading.value || !news.value) return

  actionLoading.value = true
  try {
    if (isFavorited.value) {
      // 取消收藏
      await removeFavorite(news.value.id)
      isFavorited.value = false
      alert('已取消收藏')
    } else {
      // 添加收藏
      await addFavorite({
        newsId: news.value.id,
        type: news.value.isDomestic ? 'DOMESTIC' : 'OVERSEAS',
        newsTitle: news.value.title
      })
      isFavorited.value = true
      alert('收藏成功')
    }
  } catch (err) {
    console.error('收藏操作失败:', err)
    alert('操作失败，请稍后重试')
  } finally {
    actionLoading.value = false
  }
}

// 分享新闻
const shareNews = () => {
  if (navigator.share && news.value) {
    navigator.share({
      title: news.value.title,
      text: news.value.summary,
      url: window.location.href
    }).catch(console.error)
  } else {
    // 复制链接到剪贴板
    navigator.clipboard.writeText(window.location.href).then(() => {
      alert('链接已复制到剪贴板')
    }).catch(() => {
      alert('分享功能暂不可用')
    })
  }
}

// 格式化时间
const formatTime = (timeStr: string) => {
  if (!timeStr) return ''
  const date = new Date(timeStr)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(() => {
  loadNewsDetail().then(() => {
    checkFavoriteStatus()
  })
})
</script>

<style scoped>
.news-detail-page {
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
  max-width: 800px;
  margin: 20px auto;
  padding: 0 16px;
}

.detail-container {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  padding: 24px;
}

.loading, .error {
  text-align: center;
  padding: 40px 20px;
  color: #666;
}

.error {
  color: #c41e3a;
}

.detail-title {
  font-size: 24px;
  font-weight: 700;
  color: #2c3e50;
  margin: 0 0 16px;
  line-height: 1.4;
}

.detail-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #eee;
}

.meta-item {
  font-size: 14px;
  color: #666;
}

.detail-cover {
  width: 100%;
  max-height: 400px;
  object-fit: cover;
  border-radius: 8px;
  margin-bottom: 20px;
}

.detail-content {
  font-size: 16px;
  line-height: 1.8;
  color: #333;
  margin-bottom: 24px;
}

.detail-content p {
  margin: 0 0 16px;
}

.detail-actions {
  display: flex;
  gap: 12px;
  justify-content: center;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.action-btn {
  padding: 10px 20px;
  border: 1px solid #c41e3a;
  border-radius: 8px;
  background: #fff;
  color: #c41e3a;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.action-btn:hover:not(:disabled) {
  background: #c41e3a;
  color: #fff;
}

.action-btn.favorited {
  background: #c41e3a;
  color: #fff;
}

.action-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.share-btn {
  border-color: #1890ff;
  color: #1890ff;
}

.share-btn:hover:not(:disabled) {
  background: #1890ff;
  color: #fff;
}

@media (max-width: 768px) {
  .detail-title {
    font-size: 20px;
  }

  .detail-meta {
    flex-direction: column;
    gap: 8px;
  }

  .detail-content {
    font-size: 15px;
  }

  .detail-actions {
    flex-direction: column;
  }
}
</style>
