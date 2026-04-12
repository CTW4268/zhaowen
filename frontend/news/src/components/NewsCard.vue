<template>
  <nav class="navbar">
    <!-- 导航栏内容，例如菜单项 -->
    <ul>
      <li><a href="#">首页</a></li>
      <li><a href="#">关于</a></li>
    </ul>
  </nav>
</template>

<script setup lang="ts">
// 如果需要逻辑，可在这里添加
</script>

<style scoped>
.navbar {
  background-color: #333;
  color: white;
  padding: 10px;
}

.navbar ul {
  list-style: none;
  display: flex;
  gap: 20px;
}

.navbar a {
  color: white;
  text-decoration: none;
}
</style>