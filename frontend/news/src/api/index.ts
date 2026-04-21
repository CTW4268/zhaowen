import axios from 'axios'

// 创建 Axios 实例
const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器：自动附加 JWT Token
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('news_app_token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器：统一错误处理和数据解包（带详细调试信息）
apiClient.interceptors.response.use(
  (response) => {
    // 后端返回的是 ApiResponse { code, message, data }
    // 这里直接返回 data 字段，方便调用方使用
    const apiResponse = response.data as any

    console.log('[API 响应]', {
      url: response.config.url,
      method: response.config.method?.toUpperCase(),
      status: response.status,
      responseData: apiResponse
    })

    if (apiResponse.code === 200) {
      return apiResponse.data
    } else {
      // 如果后端返回错误码，抛出异常
      const error = new Error(apiResponse.message || '请求失败')
      ;(error as any).code = apiResponse.code
      return Promise.reject(error)
    }
  },
  (error) => {
    // 详细的错误日志
    console.error('[API 错误详情]', {
      url: error.config?.url,
      method: error.config?.method?.toUpperCase(),
      status: error.response?.status,
      statusText: error.response?.statusText,
      responseData: error.response?.data,
      errorMessage: error.message,
      errorStack: error.stack
    })

    // 处理 HTTP 错误
    let errorMessage = '网络错误，请检查网络连接'

    if (error.response) {
      const status = error.response.status
      const data = error.response.data

      // 优先使用后端返回的错误信息
      if (data && data.message) {
        errorMessage = `${data.message} (状态码: ${status})`
      } else {
        switch (status) {
          case 400:
            errorMessage = '请求参数错误'
            break
          case 401:
            // Token 过期或无效，清除本地存储
            errorMessage = '认证失败，请重新登录'
            localStorage.removeItem('news_app_token')
            localStorage.removeItem('news_app_user')
            // 可选：跳转到登录页
            // window.location.href = '/login'
            break
          case 403:
            errorMessage = '没有权限访问该资源'
            break
          case 404:
            errorMessage = '请求的资源不存在'
            break
          case 500:
            errorMessage = `服务器内部错误 (${status}) - 请检查后端日志`
            break
          default:
            errorMessage = `请求失败 (${status})`
        }
      }
    } else if (error.request) {
      errorMessage = '网络错误，请检查网络连接（后端服务可能未启动）'
    } else {
      errorMessage = error.message || '请求配置错误'
    }

    // 创建带有用户友好消息的错误对象
    const userFriendlyError = new Error(errorMessage)
    ;(userFriendlyError as any).originalError = error

    return Promise.reject(userFriendlyError)
  }
)

// 导出一个辅助函数用于类型断言
export type ApiResponse<T> = T

export default apiClient
