import apiClient from './index'

// 新闻 DTO - 匹配news-system数据库结构（规范化）
export interface NewsDTO {
  id: number
  title: string
  summary: string
  content?: string

  // 关联信息
  authorId?: number
  authorName?: string

  sourceId?: number
  sourceName?: string

  countryId?: number
  countryName?: string
  continent?: string

  // 类型和统计
  isDomestic?: boolean
  viewNum?: number
  likeNum?: number
  collectNum?: number

  publishTime?: string
  createdAt?: string
}

// 分页结果
export interface PageResult<T> {
  records: T[]
  total: number
  page: number
  size: number
}

/**
 * 获取国内新闻
 */
export const getDomesticNews = (params: {
  page?: number
  size?: number
}) => {
  return apiClient.get<PageResult<NewsDTO>>('/news/domestic', { params })
}

/**
 * 获取海外新闻
 */
export const getOverseasNews = (params: {
  page?: number
  size?: number
}) => {
  return apiClient.get<PageResult<NewsDTO>>('/news/overseas', { params })
}

/**
 * 获取所有新闻
 */
export const getAllNews = (params: {
  page?: number
  size?: number
}) => {
  return apiClient.get<PageResult<NewsDTO>>('/news/all', { params })
}

/**
 * 获取新闻详情
 */
export const getNewsDetail = (id: number) => {
  return apiClient.get<NewsDTO>(`/news/${id}`)
}
