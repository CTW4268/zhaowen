import apiClient from './index'

// FAQ 项 - 匹配 api.md 文档结构
export interface FAQItem {
  key: string
}

/**
 * 获取常见问题列表
 */
export const getFAQ = () => {
  return apiClient.get<FAQItem[]>('/api/help/faq')
}
