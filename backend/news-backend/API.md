# 新闻中心网站 API 接口文档

## 接口说明

- **基础路径**: `/api`
- **数据格式**: JSON
- **字符编码**: UTF-8

## 响应格式

### 成功响应
```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

### 错误响应
```json
{
  "code": 400,
  "message": "错误描述",
  "data": null
}
```

---

## 一、用户认证模块

### 1.1 用户注册
**接口**: `POST /auth/register`

**请求参数**:
```json
{
  "username": "testuser",
  "password": "123456"
}
```

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "testuser",
    "avatar": "",
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```

---

### 1.2 用户登录
**接口**: `POST /auth/login`

**请求参数**:
```json
{
  "username": "testuser",
  "password": "123456"
}
```

**响应**: 同注册接口

---

## 二、新闻内容模块

### 2.1 获取轮播图新闻
**接口**: `GET /news/carousel`

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "title": "北京市发布新政策促进科技创新",
      "summary": "北京市政府出台多项措施支持科技创新发展",
      "coverImage": "/images/news/beijing-tech.jpg",
      "type": "DOMESTIC",
      "isCarousel": true,
      "carouselOrder": 1
    }
  ]
}
```

---

### 2.2 获取首页推荐新闻
**接口**: `GET /news/recommend`

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| type | String | 否 | domestic | 新闻类型：domestic/overseas/politics |
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [],
    "total": 100,
    "page": 1,
    "size": 10,
    "totalPages": 10
  }
}
```

---

### 2.3 获取国内新闻
**接口**: `GET /news/domestic`

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| province | String | 否 | 省份/直辖市 |
| category | String | 否 | 分类：shizheng/minsheng/caijing/waiwu |
| page | Integer | 否 | 页码 |
| size | Integer | 否 | 每页数量 |

---

### 2.4 获取海外新闻
**接口**: `GET /news/overseas`

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| region | String | 否 | 洲：亚洲/欧洲/美洲/非洲/大洋洲 |
| country | String | 否 | 国家 |
| category | String | 否 | 分类 |
| page | Integer | 否 | 页码 |
| size | Integer | 否 | 每页数量 |

---

### 2.5 获取政治新闻
**接口**: `GET /news/politics`

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |

---

### 2.6 获取新闻详情
**接口**: `GET /news/{id}`

**路径参数**:
- `id`: 新闻 ID

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "北京市发布新政策促进科技创新",
    "summary": "北京市政府出台多项措施支持科技创新发展",
    "content": "详细内容...",
    "coverImage": "/images/news/beijing-tech.jpg",
    "type": "DOMESTIC",
    "province": "北京",
    "category": "shizheng",
    "source": "新华网",
    "author": "记者张三",
    "viewCount": 100,
    "createdAt": "2024-01-01 10:00:00",
    "publishedAt": "2024-01-01 10:00:00"
  }
}
```

**说明**: 如果用户已登录，会自动记录阅读历史

---

## 三、收藏管理模块（需要认证）

### 3.1 获取用户收藏列表
**接口**: `GET /favorites`

**请求头**:
```
Authorization: Bearer <token>
```

**请求参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| type | String | 否 | 收藏类型：domestic/overseas |
| page | Integer | 否 | 页码 |
| size | Integer | 否 | 每页数量 |

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "id": 1,
        "newsId": 1,
        "newsType": "DOMESTIC",
        "newsTitle": "北京市发布新政策促进科技创新",
        "newsCover": "/images/news/beijing-tech.jpg",
        "createdAt": "2024-01-01 10:00:00"
      }
    ],
    "total": 10,
    "page": 1,
    "size": 10,
    "totalPages": 1
  }
}
```

---

### 3.2 添加收藏
**接口**: `POST /favorites`

**请求头**:
```
Authorization: Bearer <token>
```

**请求体**:
```json
{
  "newsId": 1,
  "type": "DOMESTIC",
  "newsTitle": "北京市发布新政策促进科技创新",
  "newsCover": "/images/news/beijing-tech.jpg"
}
```

---

### 3.3 取消收藏
**接口**: `DELETE /favorites/{newsId}`

**请求头**:
```
Authorization: Bearer <token>
```

---

### 3.4 检查收藏状态
**接口**: `GET /favorites/check/{newsId}`

**请求头**:
```
Authorization: Bearer <token>
```

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "favorited": true
  }
}
```

---

## 四、阅读历史模块（需要认证）

### 4.1 获取用户阅读历史
**接口**: `GET /history`

**请求头**:
```
Authorization: Bearer <token>
```

**请求参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "id": 1,
        "newsId": 1,
        "newsTitle": "北京市发布新政策促进科技创新",
        "newsType": "DOMESTIC",
        "newsCategory": "shizheng",
        "readAt": "2024-01-01 10:00:00"
      }
    ],
    "total": 50,
    "page": 1,
    "size": 10,
    "totalPages": 5
  }
}
```

---

### 4.2 清空历史记录
**接口**: `DELETE /history/clear`

**请求头**:
```
Authorization: Bearer <token>
```

---

### 4.3 删除单条历史记录
**接口**: `DELETE /history/{recordId}`

**请求头**:
```
Authorization: Bearer <token>
```

---

## 五、反馈与帮助模块

### 5.1 提交反馈
**接口**: `POST /feedback`

**请求体**:
```json
{
  "type": "bug",
  "subject": "登录页面显示异常",
  "description": "在移动端登录页面按钮位置偏移",
  "contact": "user@example.com"
}
```

**反馈类型**:
- `bug`: Bug 报告
- `feature`: 功能建议
- `ui`: 界面改进
- `other`: 其他

---

### 5.2 获取常见问题
**接口**: `GET /help/faq`

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "question": "如何收藏新闻？",
      "answer": "在新闻详情页面点击收藏按钮..."
    }
  ]
}
```

---

## 六、用户信息模块（需要认证）

### 6.1 获取当前用户信息
**接口**: `GET /user/info`

**请求头**:
```
Authorization: Bearer <token>
```

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "username": "testuser",
    "id": "1"
  }
}
```

---

## 认证说明

### JWT Token 使用

1. **Token 获取**: 通过登录接口获取 JWT Token
2. **Token 有效期**: 7 天
3. **Token 刷新**: 每次请求自动续期
4. **携带方式**: 在请求头中添加 `Authorization: Bearer <token>`

### 公开接口（无需认证）
- `/auth/register` - 用户注册
- `/auth/login` - 用户登录
- `/news/**` - 所有新闻查询接口
- `/help/faq` - 常见问题

### 受保护接口（需要认证）
- `/favorites/**` - 收藏管理
- `/history/**` - 历史记录
- `/user/**` - 用户信息

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权，需要登录 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 测试账户

| 用户名 | 密码 | 说明 |
|--------|------|------|
| testuser | 123456 | 普通用户 |
| admin | 123456 | 管理员 |

---

## Swagger UI

启动应用后访问：`http://localhost:8080/api/swagger-ui.html`

可以在 Swagger UI 中直接测试所有接口。
