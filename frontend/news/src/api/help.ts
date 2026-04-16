import apiClient from './index'

// FAQ 项
export interface FAQItem {
  question: string
  answer: string
}

/**
 * 获取常见问题列表
 */
export const getFAQ = () => {
  return apiClient.get<FAQItem[]>('/help/faq')
}
