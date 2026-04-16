import apiClient from './index'

// 用户信息响应
export interface UserInfo {
  username: string
  id: string
}

/**
 * 获取当前用户信息
 */
export const getUserInfo = () => {
  return apiClient.get<UserInfo>('/user/info')
}
