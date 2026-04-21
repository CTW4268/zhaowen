import apiClient from './index'

// 收藏 DTO - 匹配后端新的数据结构
export interface FavoriteDTO {
  id: number
  newsId: number
  newsType: string
  newsTitle: string
  createdAt: string
}

// 收藏检查响应
export interface FavoriteCheckResponse {
  favorited: boolean
}

// 添加收藏请求 - 移除newsCover字段
export interface FavoriteRequest {
  newsId: number
  type: string
  newsTitle: string
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
 * 获取用户收藏列表
 */
export const getFavorites = (params: {
  type?: string
  page: number
  size: number
}) => {
  return apiClient.get<PageResult<FavoriteDTO>>('/favorites/get', { params })
}

/**
 * 添加收藏
 */
export const addFavorite = (data: FavoriteRequest) => {
  return apiClient.post('/favorites/add', data)
}

/**
 * 取消收藏
 */
export const removeFavorite = (id: number) => {
  return apiClient.delete(`/favorites/${id}`)
}

/**
 * 检查收藏状态
 */
export const checkFavorite = (newsId: number) => {
  return apiClient.get<FavoriteCheckResponse>(`/favorites/check/${newsId}`)
}
