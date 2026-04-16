import { createRouter, createWebHistory } from 'vue-router'
// 确认导入路径正确（../views/ 对应你的文件位置）
import MainView from '../views/MainView.vue'
import DomesticView from '../views/DomesticView.vue'
import OverseasView from '../views/OverseasView.vue'
import RegisterView from '../views/zhuce.vue'
import FavoritesView from '../views/FavoritesView.vue'
import HistoryView from '../views/HistoryView.vue'
import HelpCenterView from '../views/HelpCenterView.vue'
import FeedbackView from '../views/FeedbackView.vue'
import NewsDetailView from '../views/NewsDetailView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/', // 默认访问路径
      name: 'main',
      component: MainView // 必须指向你的首页组件，而非默认组件
    },
    {
      path: '/domestic',
      name: 'domestic',
      component: DomesticView
    },
    {
      path: '/overseas',
      name: 'overseas',
      component: OverseasView
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView
    },
    {
      path: '/favorites',
      name: 'favorites',
      component: FavoritesView
    },
    {
      path: '/history',
      name: 'history',
      component: HistoryView
    },
    {
      path: '/help',
      name: 'help',
      component: HelpCenterView
    },
    {
      path: '/feedback',
      name: 'feedback',
      component: FeedbackView
    },
    {
      path: '/news/:id',
      name: 'newsDetail',
      component: NewsDetailView,
      props: true
    }
  ]
})

export default router
