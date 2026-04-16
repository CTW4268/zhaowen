<template>
  <article class="news-card" @click="handleCardClick">
    <div class="news-content">
      <h3 class="news-title">{{ news.title }}</h3>
      <p class="news-summary">{{ news.summary }}</p>
      <div class="news-meta">
        <span class="news-type">{{ news.isDomestic ? '国内' : '海外' }}</span>
        <span class="news-time">{{ formatTime(news.publishTime) }}</span>
      </div>
      <div class="news-actions" v-if="showActions">
        <button
          class="action-btn favorite-btn"
          :class="{ favorited: isFavorited }"
          @click.stop="toggleFavorite"
          :disabled="loading"
        >
          {{ isFavorited ? '已收藏' : '收藏' }}
        </button>
      </div>
    </div>
  </article>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { addFavorite, removeFavorite, checkFavorite } from '@/api/favorite'
import type { NewsDTO } from '@/api/news'

const props = defineProps<{
  news: NewsDTO
  showActions?: boolean
}>()

const router = useRouter()
const isFavorited = ref(false)
const loading = ref(false)

// 检查收藏状态
const checkFavoriteStatus = async () => {
  if (!props.showActions) return

  try {
    const res = await checkFavorite(props.news.id)
    isFavorited.value = res.favorited
  } catch (error) {
    console.error('检查收藏状态失败:', error)
  }
}

// 切换收藏状态
const toggleFavorite = async () => {
  if (loading.value) return

  loading.value = true
  try {
    if (isFavorited.value) {
      // 取消收藏
      await removeFavorite(props.news.id)
      isFavorited.value = false
      alert('已取消收藏')
    } else {
      // 添加收藏
      await addFavorite({
        newsId: props.news.id,
        type: props.news.isDomestic ? 'DOMESTIC' : 'OVERSEAS',
        newsTitle: props.news.title
      })
      isFavorited.value = true
      alert('收藏成功')
    }
  } catch (error) {
    console.error('收藏操作失败:', error)
    alert('操作失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 格式化时间
const formatTime = (timeStr: string) => {
  if (!timeStr) return ''
  const date = new Date(timeStr)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  })
}

// 点击卡片跳转到详情页
const handleCardClick = () => {
  router.push(`/news/${props.news.id}`)
}

onMounted(() => {
  if (props.showActions) {
    checkFavoriteStatus()
  }
})
</script>

<style scoped>
.news-card {
  display: flex;
  gap: 14px;
  margin-bottom: 14px;
  padding: 12px;
  background: #fff;
  border-radius: 10px;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: transform 0.2s, box-shadow 0.2s;
}

.news-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
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
  justify-content: space-between;
}

.news-title {
  margin: 0 0 8px;
  font-size: 16px;
  font-weight: 700;
  line-height: 1.4;
}

.news-summary {
  margin: 0 0 8px;
  color: #666;
  font-size: 14px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.news-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #999;
}

.news-actions {
  margin-top: 8px;
}

.action-btn {
  padding: 6px 12px;
  border: 1px solid #c41e3a;
  border-radius: 6px;
  background: #fff;
  color: #c41e3a;
  font-size: 12px;
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
</style>
