import apiClient from './index'

// 反馈请求
export interface FeedbackRequest {
  type: string
  subject: string
  description: string
  contact?: string
}

/**
 * 提交反馈
 */
export const submitFeedback = (data: FeedbackRequest) => {
  return apiClient.post('/feedback', data)
}
