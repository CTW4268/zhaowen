<template>
  <!-- 仅保留路由视图容器，所有页面通过路由渲染 -->
  <RouterView />
</template>

<script setup lang="ts">
import { RouterView } from 'vue-router'
import { useDevice } from './composables/useDevice'

// PC/手机端检测，在 html 上设置 data-device="mobile" | "pc"
useDevice()
</script>

<style scoped>
/* 可加全局基础样式，无需其他内容 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
</style>