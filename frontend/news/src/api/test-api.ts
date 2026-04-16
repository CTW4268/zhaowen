/**
 * API 接口测试脚本
 * 用于验证前后端API接口的连通性和数据正确性
 */

import { login, register } from './auth'
import { getCarouselNews, getRecommendNews, getDomesticNews, getOverseasNews, getPoliticsNews, getNewsDetail } from './news'
import { getFavorites, addFavorite, removeFavorite, checkFavorite } from './favorite'
import { getHistory, clearHistory } from './history'
import { submitFeedback } from './feedback'

// 测试配置
const TEST_USER = {
  username: 'testuser',
  password: '123456'
}

// 测试结果
const results = {
  passed: 0,
  failed: 0,
  tests: [] as Array<{ name: string; status: 'PASS' | 'FAIL'; error?: string }>
}

// 测试函数
async function runTest(name: string, testFn: () => Promise<void>) {
  try {
    await testFn()
    results.passed++
    results.tests.push({ name, status: 'PASS' })
    console.log(`✅ ${name}`)
  } catch (error: any) {
    results.failed++
    results.tests.push({ name, status: 'FAIL', error: error.message })
    console.error(`❌ ${name}: ${error.message}`)
  }
}

// 主测试函数
async function runAllTests() {
  console.log('🚀 开始API接口测试...\n')

  // 1. 测试用户注册
  await runTest('用户注册', async () => {
    const testUsername = `test_${Date.now()}`
    await register({ username: testUsername, password: '123456' })
  })

  // 2. 测试用户登录
  let token = ''
  await runTest('用户登录', async () => {
    const response = await login(TEST_USER) as any
    token = response.token
    if (!token) throw new Error('未获取到Token')
    console.log('   Token:', token.substring(0, 20) + '...')
  })

  // 3. 测试获取轮播图新闻
  await runTest('获取轮播图新闻', async () => {
    const data = await getCarouselNews()
    console.log('   轮播图数量:', Array.isArray(data) ? data.length : 0)
  })

  // 4. 测试获取推荐新闻
  await runTest('获取国内推荐新闻', async () => {
    const data: any = await getRecommendNews({ type: 'DOMESTIC', page: 1, size: 2 })
    console.log('   新闻数量:', data.records?.length || 0)
  })

  await runTest('获取海外推荐新闻', async () => {
    const data: any = await getRecommendNews({ type: 'OVERSEAS', page: 1, size: 2 })
    console.log('   新闻数量:', data.records?.length || 0)
  })

  // 5. 测试获取国内新闻
  await runTest('获取国内新闻', async () => {
    const data: any = await getDomesticNews({ page: 1, size: 5 })
    console.log('   新闻数量:', data.records?.length || 0)
  })

  // 6. 测试获取海外新闻
  await runTest('获取海外新闻', async () => {
    const data: any = await getOverseasNews({ page: 1, size: 5 })
    console.log('   新闻数量:', data.records?.length || 0)
  })

  // 7. 测试获取政治新闻
  await runTest('获取政治新闻', async () => {
    const data: any = await getPoliticsNews({ page: 1, size: 3 })
    console.log('   新闻数量:', data.records?.length || 0)
  })

  // 8. 测试获取新闻详情
  await runTest('获取新闻详情', async () => {
    // 先获取一条新闻的ID
    const newsList: any = await getRecommendNews({ type: 'DOMESTIC', page: 1, size: 1 })
    if (newsList.records && newsList.records.length > 0) {
      const newsId = newsList.records[0].id
      const detail: any = await getNewsDetail(newsId)
      console.log('   新闻标题:', detail.title)
    } else {
      throw new Error('没有可用的新闻ID')
    }
  })

  // 9. 测试收藏功能
  await runTest('检查收藏状态', async () => {
    const newsList: any = await getRecommendNews({ type: 'DOMESTIC', page: 1, size: 1 })
    if (newsList.records && newsList.records.length > 0) {
      const newsId = newsList.records[0].id
      const status: any = await checkFavorite(newsId)
      console.log('   收藏状态:', status.favorited)
    }
  })

  // 10. 测试获取收藏列表
  await runTest('获取收藏列表', async () => {
    const data: any = await getFavorites({ page: 1, size: 5 })
    console.log('   收藏数量:', data.records?.length || 0)
  })

  // 11. 测试获取阅读历史
  await runTest('获取阅读历史', async () => {
    const data: any = await getHistory({ page: 1, size: 5 })
    console.log('   历史记录数量:', data.records?.length || 0)
  })

  // 12. 测试提交反馈
  await runTest('提交反馈', async () => {
    await submitFeedback({
      type: 'bug',
      subject: '测试反馈',
      description: '这是一个测试反馈',
      contact: 'test@example.com'
    })
  })

  // 输出测试结果
  console.log('\n' + '='.repeat(50))
  console.log('📊 测试结果汇总')
  console.log('='.repeat(50))
  console.log(`✅ 通过: ${results.passed}`)
  console.log(`❌ 失败: ${results.failed}`)
  console.log(`📝 总计: ${results.passed + results.failed}`)

  if (results.failed > 0) {
    console.log('\n失败的测试:')
    results.tests
      .filter(t => t.status === 'FAIL')
      .forEach(t => {
        console.log(`  - ${t.name}: ${t.error}`)
      })
  }

  console.log('='.repeat(50))
}

// 运行测试
runAllTests().catch(console.error)
