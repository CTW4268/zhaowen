<template>
  <div class="favorites-page">
    <nav class="top-nav">
      <div class="nav-left">
        <router-link to="/" class="nav-link">← 返回首页</router-link>
      </div>
      <div class="nav-center">
        <h2 class="nav-title">我的收藏</h2>
      </div>
      <div class="nav-right"></div>
    </nav>

    <main class="container favorites-container">
      <section class="fav-tabs">
        <button
          class="fav-tab"
          :class="{ active: activeCategory === 'domestic' }"
          @click="activeCategory = 'domestic'"
        >
          国内收藏
        </button>
        <button
          class="fav-tab"
          :class="{ active: activeCategory === 'overseas' }"
          @click="activeCategory = 'overseas'"
        >
          海外收藏
        </button>
      </section>

      <section class="fav-list">
        <article v-if="loading" class="empty">加载中...</article>
        <article v-else-if="activeCategory === 'domestic' && domesticList.length === 0" class="empty">
          当前暂无国内收藏，去首页添加吧。
        </article>
        <article v-else-if="activeCategory === 'overseas' && overseasList.length === 0" class="empty">
          当前暂无海外收藏，去首页添加吧。
        </article>

        <article
          v-for="item in activeList"
          :key="item.id"
          class="news-item"
          @click="jumpToDetail(item)"
        >
          <div class="news-content">
            <h3 class="news-title">{{ item.newsTitle }}</h3>
            <p class="news-summary">{{ item.newsType === 'DOMESTIC' ? '国内' : '海外' }}</p>
          </div>
        </article>
      </section>

      <button class="show-more" @click="showMore">展示更多</button>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getFavorites } from '@/api/favorite'
import type { FavoriteDTO } from '@/api/favorite'

const router = useRouter()
const activeCategory = ref<'domestic' | 'overseas'>('domestic')
const domesticList = ref<FavoriteDTO[]>([])
const overseasList = ref<FavoriteDTO[]>([])
const loading = ref(false)

// 加载收藏数据
const loadFavorites = async () => {
  loading.value = true
  try {
    const [domesticRes, overseasRes] = await Promise.all([
      getFavorites({ type: 'DOMESTIC', page: 1, size: 20 }),
      getFavorites({ type: 'OVERSEAS', page: 1, size: 20 })
    ])
    domesticList.value = domesticRes.records || []
    overseasList.value = overseasRes.records || []
  } catch (error) {
    console.error('加载收藏失败:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadFavorites()
})

const activeList = computed(() => (activeCategory.value === 'domestic' ? domesticList.value : overseasList.value))

const showMore = () => {
  alert('正在展示更多收藏内容...')
}

const jumpToDetail = (item: FavoriteDTO) => {
  router.push({ path: '/', query: { fav: item.newsId.toString() } })
}
</script>

<style scoped>
.favorites-page {
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

.fav-tabs {
  display: flex;
  gap: 12px;
  margin-bottom: 18px;
}

.fav-tab {
  flex: 1;
  min-height: 44px;
  border: 1px solid #ddd;
  border-radius: 10px;
  background: #fff;
  color: #2c3e50;
  font-weight: 600;
  cursor: pointer;
}

.fav-tab.active {
  background: #c41e3a;
  color: #fff;
  border-color: #c41e3a;
}

.fav-list .news-item {
  display: flex;
  gap: 14px;
  margin-bottom: 14px;
  padding: 12px;
  background: #fff;
  border-radius: 10px;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
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

.empty {
  padding: 24px;
  background: #fff;
  border-radius: 10px;
  color: #999;
  text-align: center;
}

.show-more {
  margin-top: 22px;
  width: 100%;
  height: 44px;
  border: 1px solid #c41e3a;
  border-radius: 10px;
  background: #fff;
  color: #c41e3a;
  font-weight: 600;
  cursor: pointer;
}
</style>
