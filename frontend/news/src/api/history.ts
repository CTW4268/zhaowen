import apiClient from './index'

// 历史记录 DTO
export interface HistoryDTO {
  id: number
  newsId: number
  newsTitle: string
  newsType: string
  newsCategory?: string
  readAt: string
}

// 分页结果 - 匹配后端实际返回结构
export interface PageResult<T> {
  records: T[]
  total: number
  page: number
  size: number
  totalPages: number
}

/**
 * 获取用户阅读历史
 */
export const getHistory = (params: {
  page: number
  size: number
}) => {
  return apiClient.get<PageResult<HistoryDTO>>('/history', { params })
}

/**
 * 清空历史记录
 */
export const clearHistory = () => {
  return apiClient.delete('/history/clear')
}

/**
 * 删除单条历史记录
 */
export const deleteHistory = (recordId: number) => {
  return apiClient.delete(`/history/${recordId}`)
}
