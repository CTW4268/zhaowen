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
        <article v-if="activeCategory === 'domestic' && domesticList.length === 0" class="empty">
          当前暂无国内收藏，去首页添加吧。
        </article>
        <article v-if="activeCategory === 'overseas' && overseasList.length === 0" class="empty">
          当前暂无海外收藏，去首页添加吧。
        </article>

        <article
          v-for="item in activeList"
          :key="item.id"
          class="news-item"
          @click="jumpToDetail(item)"
        >
          <img :src="item.cover" alt="收藏新闻" class="news-image" />
          <div class="news-content">
            <h3 class="news-title">{{ item.title }}</h3>
            <p class="news-summary">{{ item.summary }}</p>
          </div>
        </article>
      </section>

      <button class="show-more" @click="showMore">展示更多</button>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const activeCategory = ref<'domestic' | 'overseas'>('domestic')

const domesticList = ref([
  {
    id: 'd1',
    title: '国内收藏示例：科技新闻回顾',
    summary: '您已收藏国内新闻，方便随时查看策略变动与国家政策动态。',
    cover: 'https://via.placeholder.com/280x160/FF6B6B/FFFFFF?text=国内收藏'
  },
  {
    id: 'd2',
    title: '国内收藏示例：民生热点速递',
    summary: '民生类新闻一键收藏，再也不怕错过重要信息。',
    cover: 'https://via.placeholder.com/280x160/4ECDC4/FFFFFF?text=国内收藏2'
  }
])

const overseasList = ref([
  {
    id: 'o1',
    title: '海外收藏示例：国际合作进程',
    summary: '海外要闻随身看，知晓国际趋势，洞察市场机遇。',
    cover: 'https://via.placeholder.com/280x160/45B7D1/FFFFFF?text=海外收藏'
  }
])

const activeList = computed(() => (activeCategory.value === 'domestic' ? domesticList.value : overseasList.value))

const showMore = () => {
  // 这里可以继续加载更多收藏或跳转收藏管理页面
  alert('正在展示更多收藏内容...')
}

const jumpToDetail = (item: { id: string }) => {
  // 根据需求可对接真实详情页
  router.push({ path: '/', query: { fav: item.id } })
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
