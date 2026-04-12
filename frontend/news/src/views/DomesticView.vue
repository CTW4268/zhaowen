<template>
<!-- ==================== 顶部导航栏 ==================== -->
  <nav class="top-nav">
    <div class="nav-left">
      <div class="menu-toggle" id="menuToggle">
        <!-- 确保有内容，否则事件绑定失败 -->
        <div class="menu-line"></div>
        <div class="menu-line"></div>
        <div class="menu-line"></div>
      </div>
      <div class="dropdown-menu" id="dropdownMenu">
        <!-- 添加菜单项 -->
        <a href="#">首页推荐</a>
        <router-link to="/favorites">个人收藏</router-link>
        <router-link to="/history">阅读历史</router-link>
        <router-link to="/help">帮助中心</router-link>
        <router-link to="/feedback">帮助反馈</router-link>
      </div>
    </div>

    <div class="nav-center">
      <h2 class="nav-title">国内新闻</h2>
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
        <h1 class="main-title">国内话题</h1>
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

        <!-- 地区选择区域（国内省份/直辖市） -->
        <section class="selection-section">
            <div class="selection-box">
                <label for="regionSelect">省份/直辖市：</label>
                <select id="regionSelect">
                    <option value="">请选择省份或直辖市</option>
                    <!-- 直辖市 -->
                    <option value="beijing">北京市</option>
                    <option value="tianjin">天津市</option>
                    <option value="shanghai">上海市</option>
                    <option value="chongqing">重庆市</option>
                    <!-- 华北地区 -->
                    <option value="hebei">河北省</option>
                    <option value="shanxi">山西省</option>
                    <option value="neimenggu">内蒙古自治区</option>
                    <!-- 东北地区 -->
                    <option value="liaoning">辽宁省</option>
                    <option value="jilin">吉林省</option>
                    <option value="heilongjiang">黑龙江省</option>
                    <!-- 华东地区 -->
                    <option value="jiangsu">江苏省</option>
                    <option value="zhejiang">浙江省</option>
                    <option value="anhui">安徽省</option>
                    <option value="fujian">福建省</option>
                    <option value="jiangxi">江西省</option>
                    <option value="shandong">山东省</option>
                    <!-- 华中地区 -->
                    <option value="henan">河南省</option>
                    <option value="hubei">湖北省</option>
                    <option value="hunan">湖南省</option>
                    <!-- 华南地区 -->
                    <option value="guangdong">广东省</option>
                    <option value="guangxi">广西壮族自治区</option>
                    <option value="hainan">海南省</option>
                    <!-- 西南地区 -->
                    <option value="sichuan">四川省</option>
                    <option value="guizhou">贵州省</option>
                    <option value="yunnan">云南省</option>
                    <option value="xizang">西藏自治区</option>
                    <!-- 西北地区 -->
                    <option value="shanxi_province">陕西省</option>
                    <option value="gansu">甘肃省</option>
                    <option value="qinghai">青海省</option>
                    <option value="ningxia">宁夏回族自治区</option>
                    <option value="xinjiang">新疆维吾尔自治区</option>
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

// 所有DOM操作必须放在onMounted中，确保组件挂载后元素已渲染
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

  // 登录弹窗
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
})
</script>
<style scoped>
  /* 登录弹窗已在全局样式中定义 */
</style>
