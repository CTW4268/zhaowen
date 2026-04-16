<template>
  <div v-if="visible" class="message-container" :class="type">
    <div class="message-content">
      <span class="message-icon">{{ icon }}</span>
      <span class="message-text">{{ message }}</span>
      <button class="message-close" @click="close">×</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'

const props = defineProps<{
  type?: 'success' | 'error' | 'warning' | 'info'
  message: string
  duration?: number
}>()

const emit = defineEmits(['close'])

const visible = ref(false)

const icon = computed(() => {
  switch (props.type) {
    case 'success':
      return '✓'
    case 'error':
      return '✕'
    case 'warning':
      return '⚠'
    default:
      return 'ℹ'
  }
})

let timer: number | null = null

const show = () => {
  visible.value = true

  if (props.duration && props.duration > 0) {
    timer = window.setTimeout(() => {
      close()
    }, props.duration)
  }
}

const close = () => {
  visible.value = false
  if (timer) {
    clearTimeout(timer)
    timer = null
  }
  emit('close')
}

// 监听message变化，自动显示
watch(() => props.message, (newVal) => {
  if (newVal) {
    show()
  }
}, { immediate: true })

import { computed } from 'vue'
</script>

<style scoped>
.message-container {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 9999;
  min-width: 300px;
  max-width: 500px;
  animation: slideIn 0.3s ease-out;
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

.message-content {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  background: #fff;
}

.message-icon {
  margin-right: 12px;
  font-size: 18px;
  font-weight: bold;
}

.message-text {
  flex: 1;
  font-size: 14px;
  line-height: 1.5;
}

.message-close {
  margin-left: 12px;
  padding: 0;
  width: 24px;
  height: 24px;
  border: none;
  background: transparent;
  font-size: 18px;
  cursor: pointer;
  color: #999;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.message-close:hover {
  background: #f0f0f0;
  color: #666;
}

/* 不同类型的样式 */
.message-container.success .message-content {
  border-left: 4px solid #52c41a;
}

.message-container.success .message-icon {
  color: #52c41a;
}

.message-container.error .message-content {
  border-left: 4px solid #ff4d4f;
}

.message-container.error .message-icon {
  color: #ff4d4f;
}

.message-container.warning .message-content {
  border-left: 4px solid #faad14;
}

.message-container.warning .message-icon {
  color: #faad14;
}

.message-container.info .message-content {
  border-left: 4px solid #1890ff;
}

.message-container.info .message-icon {
  color: #1890ff;
}

@media (max-width: 768px) {
  .message-container {
    left: 20px;
    right: 20px;
    min-width: auto;
  }
}
</style>
