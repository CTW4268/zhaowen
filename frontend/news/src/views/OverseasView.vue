<template>
   <!-- ==================== 顶部导航栏 ==================== -->
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
    </div>

    <div class="nav-center">
      <h2 class="nav-title">海外热点</h2>
    </div>

    <div class="nav-right">
      <a class="nav-back" @click="$router.push('/')" role="button">← 首页</a>
      <div class="nav-user">
        <span class="nav-username" v-if="user">{{ user }}</span>
        <div class="user-icon" id="userIcon">
          <!-- SVG 或图标 -->
        </div>
      </div>
    </div>
  </nav>

    <!-- ==================== 主标题区域 ==================== -->
    <header class="main-header">
        <h1 class="main-title">海外热点</h1>
    </header>

    <!-- ==================== 主容器 ==================== -->
    <main class="container region-page">
        <!-- 轮播图模块 -->
        <section class="carousel-section">
            <div class="carousel-wrapper">
                <div class="carousel-container">
                    <div class="carousel-text">
                        <div>近期国外时政动向新闻图片</div>
                        <div class="subtitle">底部配小字概括</div>
                    </div>
                    <div class="carousel-nav carousel-prev">&lt;</div>
                    <div class="carousel-nav carousel-next">&gt;</div>
                </div>
            </div>
        </section>

        <!-- 地区/国家选择区域（先选地区，再选对应国家） -->
        <section class="selection-section">
            <div class="selection-box">
                <label for="regionSelect">地区：</label>
                <select id="regionSelect">
                    <option value="">请选择地区</option>
                    <option value="asia">亚洲</option>
                    <option value="europe">欧洲</option>
                    <option value="america">美洲</option>
                    <option value="africa">非洲</option>
                    <option value="oceania">大洋洲</option>
                </select>
            </div>
            <div class="selection-box">
                <label for="countrySelect">国家：</label>
                <select id="countrySelect" disabled>
                    <option value="">请先选择地区</option>
                </select>
            </div>
        </section>

        <!-- 新闻分类标签区域 -->
        <section class="category-section">
            <div class="category-tags">
                <div class="category-tag active" data-category="shizheng">时政</div>
                <div class="category-tag" data-category="minsheng">民生</div>
                <div class="category-tag" data-category="caijing">财经</div>
                <div class="category-tag" data-category="waiwu">外务</div>
            </div>
        </section>

        <!-- 新闻内容区域 -->
        <section class="news-content-section">
            <h2 class="section-title">该地区新闻</h2>
            <div class="news-list" id="newsList">
                <article class="news-item" data-id="1">
                    <img src="https://via.placeholder.com/200x130/FF6B6B/FFFFFF?text=国际时政1" alt="国际时政" class="news-image">
                    <div class="news-content">
                        <h3 class="news-title">国际时政：多国领导人举行紧急峰会</h3>
                        <p class="news-summary">近日，多国领导人在国际会议中心举行紧急峰会，就当前国际热点问题展开深入讨论。会议达成多项共识，为维护地区和平稳定奠定基础...</p>
                    </div>
                </article>
                <article class="news-item" data-id="2">
                    <img src="https://via.placeholder.com/200x130/4ECDC4/FFFFFF?text=民生动态" alt="民生动态" class="news-image">
                    <div class="news-content">
                        <h3 class="news-title">海外民生：多国推出惠民新政策</h3>
                        <p class="news-summary">为应对经济下行压力，多个发达国家相继推出惠民新政策，涵盖医疗、教育、住房等多个领域，受到当地民众广泛欢迎...</p>
                    </div>
                </article>
                <article class="news-item" data-id="3">
                    <img src="https://via.placeholder.com/200x130/45B7D1/FFFFFF?text=财经资讯" alt="财经资讯" class="news-image">
                    <div class="news-content">
                        <h3 class="news-title">国际财经：全球股市震荡调整</h3>
                        <p class="news-summary">受多重因素影响，全球主要股指近期出现震荡调整。分析师指出，市场正在消化前期涨幅，长期来看仍具备上涨基础...</p>
                    </div>
                </article>
                <article class="news-item" data-id="4">
                    <img src="https://via.placeholder.com/200x130/F7DC6F/FFFFFF?text=外交动态" alt="外交动态" class="news-image">
                    <div class="news-content">
                        <h3 class="news-title">外交动态：双边关系取得新进展</h3>
                        <p class="news-summary">两国高级别代表团近日互访，就深化各领域合作达成重要共识。此次访问标志着双边关系迈上新台阶，为未来发展开辟广阔空间...</p>
                    </div>
                </article>
            </div>
        </section>

        <!-- 查看更多按钮 -->
        <section class="load-more-section">
            <a href="#" class="load-more-btn" id="loadMoreBtn">查看更多新闻详情</a>
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

    <!-- ==================== 底部区域 ==================== -->
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
import { onMounted } from 'vue'
import { useAuth } from '@/composables/useAuth'

// 全局登录状态
const { user, login } = useAuth()

// 所有DOM操作必须放在onMounted中，确保元素已渲染
onMounted(() => {

  const menuToggle = document.getElementById('menuToggle')
  const dropdownMenu = document.getElementById('dropdownMenu')
  let menuOpen = false

  menuToggle?.addEventListener('click', (e) => {
    e.stopPropagation()
    menuOpen = !menuOpen
    dropdownMenu?.classList.toggle('active', menuOpen)
  })

  document.addEventListener('click', () => {
    menuOpen = false
    dropdownMenu?.classList.remove('active')
  })

  // ==================== 登录弹窗 ====================
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
    openLoginModal()
  })

  loginModalBackdrop?.addEventListener('click', closeLoginModal)
  loginModalClose?.addEventListener('click', closeLoginModal)

  loginForm?.addEventListener('submit', function (e) {
    e.preventDefault()
    const formData = new FormData(this)
    const username = (formData.get('username') as string) || '用户'
    login(username)
    alert(`登录完成：${username}`)
    closeLoginModal()
  })

  registerLink?.addEventListener('click', function (e) {
    e.preventDefault()
    window.location.href = '/register'
  })

  // ==================== 地区/国家选择功能 ====================
  const regionSelect = document.getElementById('regionSelect') as HTMLSelectElement | null
  const countrySelect = document.getElementById('countrySelect') as HTMLSelectElement | null

  // 地区 -> 国家映射
  const regionCountryMap: Record<string, { value: string; label: string }[]> = {
    asia: [
      { value: 'china', label: '中国' },
      { value: 'japan', label: '日本' },
      { value: 'korea', label: '韩国' },
      { value: 'india', label: '印度' },
      { value: 'singapore', label: '新加坡' }
    ],
    europe: [
      { value: 'uk', label: '英国' },
      { value: 'germany', label: '德国' },
      { value: 'france', label: '法国' },
      { value: 'italy', label: '意大利' },
      { value: 'spain', label: '西班牙' }
    ],
    america: [
      { value: 'usa', label: '美国' },
      { value: 'canada', label: '加拿大' },
      { value: 'brazil', label: '巴西' },
      { value: 'mexico', label: '墨西哥' }
    ],
    africa: [
      { value: 'egypt', label: '埃及' },
      { value: 'south_africa', label: '南非' },
      { value: 'nigeria', label: '尼日利亚' }
    ],
    oceania: [
      { value: 'australia', label: '澳大利亚' },
      { value: 'new_zealand', label: '新西兰' }
    ]
  }

  const resetCountrySelect = (placeholder: string) => {
    if (!countrySelect) return
    countrySelect.innerHTML = ''
    const option = document.createElement('option')
    option.value = ''
    option.textContent = placeholder
    countrySelect.appendChild(option)
  }

  regionSelect?.addEventListener('change', function () {
    const selectedRegion = this.value
    console.log('选择了地区:', selectedRegion)

    if (!countrySelect) return

    if (!selectedRegion || !regionCountryMap[selectedRegion]) {
      countrySelect.disabled = true
      resetCountrySelect('请先选择地区')
      return
    }

    // 根据地区填充国家选项
    countrySelect.disabled = false
    countrySelect.innerHTML = ''
    const placeholder = document.createElement('option')
    placeholder.value = ''
    placeholder.textContent = '请选择国家'
    countrySelect.appendChild(placeholder)

    regionCountryMap[selectedRegion].forEach(item => {
      const opt = document.createElement('option')
      opt.value = item.value
      opt.textContent = item.label
      countrySelect.appendChild(opt)
    })
  })

  countrySelect?.addEventListener('change', function () {
    const selectedCountry = this.value
    console.log('选择了国家:', selectedCountry)
    // 实际项目中这里会发送AJAX请求获取对应国家的新闻
  })

  // ==================== 分类标签切换功能 ====================
  const categoryTags = document.querySelectorAll('.category-tag')
  categoryTags.forEach(tag => {
    tag.addEventListener('click', function(this: HTMLElement) {
      // 移除所有active类
      categoryTags.forEach(t => t.classList.remove('active'))
      // 添加active类到当前点击的标签
      this.classList.add('active')
      
      // 这里可以添加刷新新闻内容的逻辑
      const category = this.dataset.category
      console.log('切换到分类:', category)
      // 实际项目中这里会发送AJAX请求获取对应分类的新闻
    })
  })

  // ==================== 查看更多按钮 ====================
  const loadMoreBtn = document.getElementById('loadMoreBtn')
  loadMoreBtn?.addEventListener('click', function(e) {
    e.preventDefault()
    alert('正在加载更多新闻...')
    // 实际项目中这里会发送AJAX请求获取更多新闻
    // 并将新内容追加到新闻列表中
  })

  // ==================== 新闻项点击事件 ====================
  const newsItems = document.querySelectorAll('.news-item')
  newsItems.forEach(item => {
    item.addEventListener('click', function(this: HTMLElement) {
      const newsId = this.dataset.id
      console.log('点击了新闻:', newsId)
      // 实际项目中这里会跳转到新闻详情页或打开详情模态框
      // window.open(`/news/${newsId}`, '_blank');
    })
  })

  // ==================== 轮播图导航功能 ====================
  const carouselPrev = document.querySelector('.carousel-prev')
  const carouselNext = document.querySelector('.carousel-next')

  carouselPrev?.addEventListener('click', function(e) {
    e.stopPropagation()
    console.log('轮播图上一张')
    // 实际项目中这里会切换到上一张图片
  })

  carouselNext?.addEventListener('click', function(e) {
    e.stopPropagation()
    console.log('轮播图下一张')
    // 实际项目中这里会切换到下一张图片
  })
})
</script>
<style scoped>
</style>