<template>
    <!-- 消息提示 -->
    <Message
      v-if="errorMessage"
      :type="messageType"
      :message="errorMessage"
      :duration="3000"
      @close="errorMessage = ''"
    />

     <!-- ==================== 新增顶部导航栏 ==================== -->
    <nav class="top-nav">
        <!-- 左侧汉堡菜单 -->
        <div class="nav-left">
            <div class="menu-toggle" id="menuToggle">
                <div class="menu-line"></div>
                <div class="menu-line"></div>
                <div class="menu-line"></div>
            </div>
            <div class="dropdown-menu" id="dropdownMenu">
                <a href="#">首页推荐</a>
                <router-link to="/favorites">个人收藏</router-link>
                <router-link to="/history">阅读历史</router-link>
                <router-link to="/help">帮助中心</router-link>
                <router-link to="/feedback">帮助反馈</router-link>
            </div>

            <!-- 国内菜单按钮靠近新闻中心左侧 -->
            <router-link to="/domestic" class="nav-link nav-domestic">国内</router-link>
        </div>

        <!-- 中间标题 -->
        <div class="nav-center">
            <h2 class="nav-title">新闻中心</h2>
        </div>

        <!-- 右侧导航链接 -->
        <div class="nav-right">
            <div class="nav-links nav-links-right">
                <router-link to="/overseas" class="nav-link">海外</router-link>
            </div>

            <div class="nav-user">
              <span class="nav-username" v-if="user">{{ user }}</span>
              <div class="user-icon" id="userIcon">
                <svg viewBox="0 0 24 24">
                    <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
                </svg>
              </div>
            </div>
        </div>
    </nav>

    <!-- 原有头部标题：艺术字体效果 -->
    <header class="header">
        <h1 class="main-title">朝闻天下事</h1>
    </header>

    <!-- 主内容容器 -->
    <main class="container">
        <!-- 轮播图模块（已因顶部导航栏而下移） -->
        <section class="carousel-section">
            <div class="carousel-wrapper">
                <div v-if="loading" class="carousel-placeholder">加载中...</div>
                <div v-else-if="allNews.length > 0" class="carousel-container">
                    <div v-for="(item, index) in allNews" :key="item.id" class="carousel-item">
                        <div class="carousel-caption">
                            <h3>{{ item.title }}</h3>
                            <p>{{ item.summary }}</p>
                        </div>
                    </div>
                </div>
                <div v-else class="carousel-placeholder">暂无轮播新闻</div>
            </div>
        </section>

        <!-- 双栏布局：国内精选 + 海外热点 -->
        <section class="dual-column-section">
            <!-- 国内精选 -->
            <div class="column-block">
                <h2 class="column-title">国内精选</h2>
                <div v-if="loading" class="news-list">加载中...</div>
                <div v-else-if="domesticNews.length > 0" class="news-list">
                    <NewsCard
                      v-for="item in domesticNews"
                      :key="item.id"
                      :news="item"
                      :show-actions="true"
                    />
                </div>
                <div v-else class="news-list">暂无国内新闻</div>
            </div>

            <!-- 海外热点 -->
            <div class="column-block">
                <h2 class="column-title">海外热点</h2>
                <div v-if="loading" class="news-list">加载中...</div>
                <div v-else-if="overseasNews.length > 0" class="news-list">
                    <NewsCard
                      v-for="item in overseasNews"
                      :key="item.id"
                      :news="item"
                      :show-actions="true"
                    />
                </div>
                <div v-else class="news-list">暂无海外新闻</div>
            </div>
        </section>

        <!-- 政治大事件展示区域 -->
        <section class="politics-section">
            <h2 class="column-title">近期国内外政治大事件展示</h2>
            <div v-if="loading" class="politics-grid">加载中...</div>
            <div v-else-if="politicsNews.length > 0" class="politics-grid">
                <NewsCard
                  v-for="item in politicsNews"
                  :key="item.id"
                  :news="item"
                  :show-actions="true"
                />
            </div>
            <div v-else class="politics-grid">暂无政治新闻</div>
        </section>
    </main>

    <!-- 登录弹窗（全局可用） -->
    <div class="login-modal" id="loginModal" aria-hidden="true">
      <div class="login-modal-backdrop" id="loginModalBackdrop"></div>
      <div class="login-modal-content" role="dialog" aria-modal="true" aria-labelledby="loginModalTitle">
        <button class="login-close" id="loginModalClose" aria-label="关闭登录窗口">×</button>
        <h2 id="loginModalTitle">用户登录</h2>
        <form class="login-form" id="loginForm">
          <label>
            账号
            <input type="text" name="username" placeholder="请输入用户名" required />
          </label>
          <label>
            密码
            <input type="password" name="password" placeholder="请输入密码" required />
          </label>
          <button type="submit" class="login-submit">登录</button>
        </form>
        <div class="login-footer">
          <small>还没有账号？<a href="#" id="registerLink">立即注册</a></small>
        </div>
      </div>
    </div>

    <!-- 底部信息区域 -->
    <footer class="footer">
        <div class="footer-content">
            <div class="footer-info">
                新闻来源：<a href="https://www.example.com" target="_blank">XXXXXXXXXXXX</a>
            </div>
            <div class="footer-info">
                <a href="mailto:contact@example.com">联系我们</a> | <a href="#">关于我们</a> | <a href="#">版权声明</a>
            </div>
        </div>
    </footer>
</template>
<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuth } from '@/composables/useAuth'
import { getDomesticNews, getOverseasNews, getPoliticsNews } from '@/api/news'
import type { NewsDTO } from '@/api/news'
import NewsCard from '@/components/NewsCard.vue'
import Message from '@/components/Message.vue'

const router = useRouter()
// 统一登录状态
const { user, login, logout } = useAuth()

// 响应式数据
const domesticNews = ref<NewsDTO[]>([])
const overseasNews = ref<NewsDTO[]>([])
const politicsNews = ref<NewsDTO[]>([])
const loading = ref(false)
const errorMessage = ref('')
const messageType = ref<'success' | 'error' | 'warning' | 'info'>('info')

// 用于取消未完成的请求（防止竞态条件）
let abortController: AbortController | null = null

// 加载数据（带详细调试信息）
const loadData = async () => {
  // 如果已有请求在进行中，先取消
  if (abortController) {
    abortController.abort()
  }
  abortController = new AbortController()

  loading.value = true
  errorMessage.value = ''

  console.log('[MainView] 开始加载新闻数据...')

  try {
    const [domesticRes, overseasRes, politicsRes] = await Promise.all([
      getDomesticNews({ page: 1, size: 6 }),
      getOverseasNews({ page: 1, size: 6 }),
      getPoliticsNews({ page: 1, size: 6 })
    ])

    // 检查是否已被取消
    if (abortController?.signal.aborted) {
      return
    }

    console.log('[MainView] 国内新闻响应:', domesticRes)
    console.log('[MainView] 海外新闻响应:', overseasRes)
    console.log('[MainView] 政治新闻响应:', politicsRes)

    domesticNews.value = domesticRes.records || []
    overseasNews.value = overseasRes.records || []
    politicsNews.value = politicsRes.records || []

    console.log('[MainView] 数据加载成功:', {
      domesticCount: domesticNews.value.length,
      overseasCount: overseasNews.value.length,
      politicsCount: politicsNews.value.length
    })
  } catch (error: any) {
    // 如果是被取消的请求，不显示错误
    if (error.name === 'AbortError') {
      return
    }

    console.error('[MainView] 加载数据失败:', error)
    console.error('[MainView] 错误详情:', {
      message: error.message,
      code: error.code,
      originalError: error.originalError,
      stack: error.stack
    })
    errorMessage.value = error.message || '加载数据失败，请稍后重试'
    messageType.value = 'error'
  } finally {
    loading.value = false
  }
}

// 所有DOM操作必须放在onMounted中，确保元素已渲染
onMounted(() => {
  // 加载新闻数据
  loadData()

  const menuToggle = document.getElementById('menuToggle')
  const dropdownMenu = document.getElementById('dropdownMenu')
  let menuOpen = false

  // 用可选链?.确保元素存在时才执行事件绑定
  menuToggle?.addEventListener('click', function(e) {
    e.stopPropagation()
    menuOpen = !menuOpen
    dropdownMenu?.classList.toggle('active', menuOpen)
  })

  // 点击页面其他地方关闭下拉菜单
  document.addEventListener('click', function() {
    menuOpen = false
    dropdownMenu?.classList.remove('active')
  })

  // 登录模态相关元素
  const userIcon = document.getElementById('userIcon')
  const loginModal = document.getElementById('loginModal')
  const loginModalBackdrop = document.getElementById('loginModalBackdrop')
  const loginModalClose = document.getElementById('loginModalClose')
  const loginForm = document.getElementById('loginForm') as HTMLFormElement | null
  const registerLink = document.getElementById('registerLink')

  const openLoginModal = () => {
    loginModal?.classList.add('active')
    loginModal?.setAttribute('aria-hidden', 'false')
  }

  const closeLoginModal = () => {
    loginModal?.classList.remove('active')
    loginModal?.setAttribute('aria-hidden', 'true')
  }

  userIcon?.addEventListener('click', () => {
    if (user.value) {
      // 已登录，显示退出选项
      if (confirm('是否退出登录？')) {
        logout()
      }
    } else {
      openLoginModal()
    }
  })
  loginModalBackdrop?.addEventListener('click', closeLoginModal)
  loginModalClose?.addEventListener('click', closeLoginModal)

  loginForm?.addEventListener('submit', async function (e) {
    e.preventDefault()
    const formData = new FormData(this)
    const username = formData.get('username') as string
    const password = formData.get('password') as string

    try {
      await login(username, password)
      messageType.value = 'success'
      errorMessage.value = `登录成功：${username}`
      closeLoginModal()
      // 登录成功后无需重新加载新闻数据
    } catch (error: any) {
      console.error('登录失败:', error)
      messageType.value = 'error'
      errorMessage.value = error.message || '登录失败，请检查用户名和密码'
    }
  })

  registerLink?.addEventListener('click', function (e) {
    e.preventDefault()
    // 跳转到内部注册页面
    router.push('/register')
  })
})

// 组件卸载时清理
onUnmounted(() => {
  if (abortController) {
    abortController.abort()
  }
})
</script>

<style scoped>
  /* 登录模态 */
  .login-modal {
    position: fixed;
    inset: 0;
    display: none;
    align-items: center;
    justify-content: center;
    z-index: 1100;
  }

  .login-modal.active {
    display: flex;
  }

  .login-modal-backdrop {
    position: absolute;
    inset: 0;
    background: rgba(0, 0, 0, 0.5);
    backdrop-filter: blur(4px);
  }

  .login-modal-content {
    position: relative;
    width: min(420px, calc(100% - 40px));
    padding: 28px 26px;
    border-radius: 18px;
    background: #ffffff;
    box-shadow: 0 16px 40px rgba(0, 0, 0, 0.3);
    z-index: 1;
    display: flex;
    flex-direction: column;
    gap: 18px;
  }

  .login-close {
    position: absolute;
    top: 14px;
    right: 14px;
    width: 34px;
    height: 34px;
    border: none;
    background: rgba(0, 0, 0, 0.06);
    border-radius: 50%;
    font-size: 22px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: background 0.2s;
  }

  .login-close:hover {
    background: rgba(0, 0, 0, 0.15);
  }

  .login-modal h2 {
    margin: 0;
    font-size: 24px;
    text-align: center;
    color: #2c3e50;
  }

  .login-form {
    display: grid;
    gap: 14px;
  }

  .login-form label {
    display: flex;
    flex-direction: column;
    gap: 8px;
    font-weight: 600;
    color: #2c3e50;
  }

  .login-form input {
    padding: 12px 14px;
    border-radius: 10px;
    border: 1px solid #d9d9d9;
    font-size: 15px;
    background: #fafafa;
  }

  .login-form input:focus {
    outline: none;
    border-color: #c41e3a;
    box-shadow: 0 0 0 3px rgba(196, 30, 58, 0.14);
  }

  .login-submit {
    padding: 14px 0;
    border-radius: 10px;
    border: none;
    background: linear-gradient(135deg, #c41e3a 0%, #8b0000 100%);
    color: #ffffff;
    font-size: 16px;
    font-weight: 700;
    cursor: pointer;
    transition: transform 0.2s, box-shadow 0.2s;
  }

  .login-submit:hover {
    transform: translateY(-1px);
    box-shadow: 0 10px 20px rgba(196, 30, 58, 0.4);
  }

  .login-footer {
    text-align: center;
    color: #667;
    font-size: 13px;
  }

  .login-footer a {
    color: #c41e3a;
    text-decoration: none;
  }

  .login-footer a:hover {
    text-decoration: underline;
  }
  /* ==================== 原有头部标题区域 ==================== */
  .header {
    background: linear-gradient(135deg, #c41e3a 0%, #8b0000 100%);
    padding: 28px 0;
    text-align: center;
    margin-bottom: var(--module-gap);
    margin-top: 0;
    box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  }

  .main-title {
    font-family: 'Long Cang', 'Ma Shan Zheng', 'STKaiti', 'KaiTi', 'SimKai', serif;
    font-size: 46px;
    font-weight: 900;
    color: #ffffff;
    letter-spacing: 8px;
    text-shadow: 3px 3px 6px rgba(0,0,0,0.3);
  }

  /* ==================== 轮播图模块 ==================== */
  .carousel-section {
    margin-bottom: var(--module-gap);
  }

  .carousel-wrapper {
    background: #ffffff;
    border-radius: 14px;
    overflow: hidden;
    height: 220px; /* 缩减轮播图高度 */
    box-shadow: 0 6px 16px rgba(0,0,0,0.12);
    position: relative;
  }

  .carousel-placeholder {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    background: linear-gradient(45deg, #e3f2fd 0%, #f3e5f5 100%);
    color: #7f8c8d;
    font-size: 18px;
    font-weight: 500;
  }

  /* ==================== 双栏内容区域 ==================== */
  .dual-column-section {
    display: grid;
    grid-template-columns: 1fr 1fr; /* PC 端双列 */
    gap: var(--column-gap);
    margin-bottom: var(--section-gap);
  }

  .column-block {
    background: #ffffff;
    padding: 28px;
    border-radius: 14px;
    box-shadow: 0 4px 14px rgba(0,0,0,0.08);
  }

  .column-title {
    font-size: 28px;
    font-weight: bold;
    color: #c41e3a;
    margin-bottom: var(--item-gap);
    padding-bottom: 12px;
    border-bottom: 3px solid #c41e3a;
  }

  /* 新闻条目 */
  .news-item {
    display: flex; /* PC 端水平 */
    gap: var(--inner-gap);
    margin-bottom: var(--item-gap);
    padding-bottom: var(--item-gap);
    border-bottom: 1px solid #ecf0f1;
  }

  .news-item:last-child {
    border-bottom: none;
    margin-bottom: 0;
    padding-bottom: 0;
  }

  .news-image {
    width: 160px; /* PC 端增大 */
    height: 100px; /* PC 端增大 */
    object-fit: cover;
    border-radius: 8px;
    flex-shrink: 0;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  }

  .news-content {
    flex: 1;
  }

  .news-title {
    font-size: 20px; /* PC 端增大 */
    font-weight: 600;
    margin-bottom: 8px;
    line-height: 1.4;
  }

  .news-title a {
    color: #2c3e50;
    text-decoration: none;
    transition: color 0.3s;
  }

  .news-title a:hover {
    color: #c41e3a;
  }

  .news-desc {
    font-size: 16px; /* PC 端增大 */
    color: #7f8c8d;
    line-height: 1.6;
  }

  /* ==================== 政治事件展示区域 ==================== */
  .politics-section {
    background: #ffffff;
    padding: 40px; /* PC 端增大 */
    border-radius: 16px;
    box-shadow: 0 4px 14px rgba(0,0,0,0.08);
    margin-bottom: var(--module-gap);
  }

  .politics-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr); /* PC 端3列 */
    gap: var(--item-gap);
  }

  .politics-card {
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 3px 10px rgba(0,0,0,0.08);
    transition: transform 0.3s, box-shadow 0.3s;
  }

  .politics-card:hover {
    transform: translateY(-6px);
    box-shadow: 0 8px 20px rgba(0,0,0,0.15);
  }

  .politics-card img {
    width: 100%;
    height: 220px; /* PC 端增大 */
    object-fit: cover;
  }

  .politics-card-content {
    padding: var(--inner-gap);
  }

  /* ==================== 底部区域 ==================== */
  /* ==================== 响应式调整 ==================== */
  @media (min-width: 769px) {
    /* PC 端明确定义，确保比例正确 */
    body {
      font-size: 16px;
    }
    .dual-column-section {
      grid-template-columns: 1fr 1fr;
    }
    .politics-grid {
      grid-template-columns: repeat(3, 1fr);
    }
  }

  @media (max-width: 1200px) {
    .politics-grid {
      grid-template-columns: repeat(2, 1fr);
    }
  }

  /* 手机端：单列、紧凑、新闻卡片式 */
  @media (max-width: 768px) {
    .header {
      padding: 16px 0;
    }
    .main-title {
      font-size: 26px;
      letter-spacing: 2px;
    }
    .carousel-section {
      margin-bottom: 20px;
    }
    .carousel-wrapper {
      height: 160px; /* 缩减移动端轮播图 */
      border-radius: 12px;
    }
    .carousel-placeholder {
      font-size: 15px;
    }
    .dual-column-section {
      grid-template-columns: 1fr;
      gap: 16px;
      margin-bottom: 20px;
    }
    .column-block {
      padding: 16px;
      border-radius: 12px;
    }
    .column-title {
      font-size: 20px;
      margin-bottom: 14px;
      padding-bottom: 10px;
    }
    .news-item {
      flex-direction: column;
      gap: 12px;
      margin-bottom: 16px;
      padding-bottom: 16px;
    }
    .news-item:last-child {
      margin-bottom: 0;
      padding-bottom: 0;
    }
    .news-image {
      width: 100%;
      height: 160px;
      border-radius: 8px;
    }
    .news-title {
      font-size: 17px;
    }
    .news-title a {
      display: block;
    }
    .news-desc {
      font-size: 14px;
    }
    .politics-section {
      padding: 16px;
      border-radius: 12px;
      margin-bottom: 20px;
    }
    .politics-section .column-title {
      font-size: 20px;
      margin-bottom: 14px;
    }
    .politics-grid {
      grid-template-columns: 1fr;
      gap: 16px;
    }
    .politics-card {
      border-radius: 10px;
    }
    .politics-card img {
      height: 180px;
    }
    .politics-card-content {
      padding: 14px;
    }
    .politics-card-content .news-title {
      font-size: 16px;
    }
    .politics-card-content .news-desc {
      font-size: 14px;
    }
  }

  @media (max-width: 480px) {
    .header {
      padding: 12px 0;
    }
    .main-title {
      font-size: 22px;
      letter-spacing: 1px;
    }
    .carousel-wrapper {
      height: 150px; /* 进一步缩减小屏轮播图 */
    }
    .column-block {
      padding: 12px;
    }
    .column-title {
      font-size: 18px;
    }
    .news-image {
      height: 140px;
    }
    .news-title {
      font-size: 16px;
    }
    .news-desc {
      font-size: 13px;
    }
    .politics-section {
      padding: 12px;
    }
    .politics-card img {
      height: 160px;
    }
    .politics-card-content {
      padding: 12px;
    }
  }

  /* PC 端放大一些间距和字体 */
  @media (min-width: 1400px) {
    :root {
      --module-gap: 80px;
      --column-gap: 50px;
      --item-gap: 35px;
      --inner-gap: 30px;
    }

    .main-title {
      font-size: 72px;
      letter-spacing: 20px;
    }

    .carousel-wrapper {
      height: 340px;
    }

    .column-title {
      font-size: 42px;
    }
  }
</style>
