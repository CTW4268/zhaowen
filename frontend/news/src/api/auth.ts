import apiClient from './index'

// 登录请求参数
export interface LoginRequest {
  username: string
  password: string
}

// 注册请求参数
export interface RegisterRequest {
  username: string
  password: string
}

// 登录/注册响应数据
export interface LoginResponse {
  id: number
  username: string
  avatar: string
  token: string
  refreshToken: string
}

/**
 * 用户登录
 */
export const login = (data: LoginRequest) => {
  return apiClient.post<LoginResponse>('/auth/login', data)
}

/**
 * 用户注册
 */
export const register = (data: RegisterRequest) => {
  return apiClient.post<LoginResponse>('/auth/register', data)
}

/**
 * 刷新 Token
 */
export const refreshToken = (refreshToken: string) => {
  return apiClient.post<LoginResponse>('/auth/refresh', { refreshToken })
}
