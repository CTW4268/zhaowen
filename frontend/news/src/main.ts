import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
// 新增这行：导入全局适配样式（路径要和你创建的一致）
import './assets/css/global.css'
const app = createApp(App)

app.use(createPinia())
app.use(router)

app.mount('#app')

