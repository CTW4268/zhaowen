import { ref, onMounted, onUnmounted } from 'vue'

/** 移动端断点（与 CSS 中 @media (max-width: 768px) 一致） */
const MOBILE_BREAKPOINT = 768

/**
 * PC/手机端检测：根据视口宽度判断是否为手机端，
 * 并在 html 上设置 data-device="mobile" | "pc"，便于 CSS/JS 按设备区分样式。
 */
export function useDevice() {
  const isMobile = ref(false)

  function update() {
    const match = window.matchMedia(`(max-width: ${MOBILE_BREAKPOINT}px)`)
    isMobile.value = match.matches
    document.documentElement.setAttribute('data-device', match.matches ? 'mobile' : 'pc')
  }

  onMounted(() => {
    update()
    const mql = window.matchMedia(`(max-width: ${MOBILE_BREAKPOINT}px)`)
    const handler = () => update()
    mql.addEventListener('change', handler)
    onUnmounted(() => mql.removeEventListener('change', handler))
  })

  return { isMobile }
}
