---
title: 默认模块
language_tabs:
  - shell: Shell
  - http: HTTP
  - javascript: JavaScript
  - ruby: Ruby
  - python: Python
  - php: PHP
  - java: Java
  - go: Go
toc_footers: []
includes: []
search: true
code_clipboard: true
highlight_theme: darkula
headingLevel: 2
generator: "@tarslib/widdershins v4.0.30"

---

# 默认模块

Base URLs:

# Authentication

# 帮助中心

## GET 获取常见问题列表

GET /api/help/faq

获取常见问题列表

> 返回示例

> 200 Response

```json
{
  "code": 0,
  "message": "string",
  "data": [
    {
      "key": "string"
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ApiResponseListMapString](#schemaapiresponselistmapstring)|

# 新闻管理

## GET 获取轮播图新闻

GET /api/news/carousel

获取轮播图新闻

> Body 请求参数

```json
{}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|

> 返回示例

> 200 Response

```json
{
  "code": 0,
  "message": "string",
  "data": [
    {
      "id": 0,
      "title": "string",
      "summary": "string",
      "content": "string",
      "authorId": 0,
      "authorName": "string",
      "sourceId": 0,
      "sourceName": "string",
      "countryId": 0,
      "countryName": "string",
      "continent": "string",
      "isDomestic": true,
      "viewNum": 0,
      "likeNum": 0,
      "collectNum": 0,
      "finalWeight": 0,
      "publishTime": "string",
      "createdAt": "string"
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ApiResponseListNewsDTO](#schemaapiresponselistnewsdto)|

## GET 获取国内新闻

GET /news/domestic

获取国内新闻

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|page|query|integer| 是 |none|
|size|query|integer| 是 |none|

> 返回示例

> 200 Response

```json
{
  "code": 0,
  "message": "string",
  "data": {
    "list": [
      {
        "id": 0,
        "title": "string",
        "summary": "string",
        "content": "string",
        "authorId": 0,
        "authorName": "string",
        "sourceId": 0,
        "sourceName": "string",
        "countryId": 0,
        "countryName": "string",
        "continent": "string",
        "isDomestic": true,
        "viewNum": 0,
        "likeNum": 0,
        "collectNum": 0,
        "finalWeight": 0,
        "publishTime": "string",
        "createdAt": "string"
      }
    ],
    "total": 0,
    "page": 0,
    "size": 0,
    "totalPages": 0
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ApiResponsePageResultNewsDTO](#schemaapiresponsepageresultnewsdto)|

## GET 获取首页推荐新闻

GET /news/recommend

获取首页推荐新闻

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|type|query|string| 否 |none|
|page|query|integer| 是 |none|
|size|query|integer| 是 |none|

> 返回示例

> 200 Response

```json
{
  "code": 0,
  "message": "string",
  "data": {
    "list": [
      {
        "id": 0,
        "title": "string",
        "summary": "string",
        "content": "string",
        "authorId": 0,
        "authorName": "string",
        "sourceId": 0,
        "sourceName": "string",
        "countryId": 0,
        "countryName": "string",
        "continent": "string",
        "isDomestic": true,
        "viewNum": 0,
        "likeNum": 0,
        "collectNum": 0,
        "finalWeight": 0,
        "publishTime": "string",
        "createdAt": "string"
      }
    ],
    "total": 0,
    "page": 0,
    "size": 0,
    "totalPages": 0
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ApiResponsePageResultNewsDTO](#schemaapiresponsepageresultnewsdto)|

## GET 获取海外新闻

GET /news/overseas

获取海外新闻

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|page|query|integer| 是 |none|
|size|query|integer| 是 |none|

> 返回示例

> 200 Response

```json
{
  "code": 0,
  "message": "string",
  "data": {
    "list": [
      {
        "id": 0,
        "title": "string",
        "summary": "string",
        "content": "string",
        "authorId": 0,
        "authorName": "string",
        "sourceId": 0,
        "sourceName": "string",
        "countryId": 0,
        "countryName": "string",
        "continent": "string",
        "isDomestic": true,
        "viewNum": 0,
        "likeNum": 0,
        "collectNum": 0,
        "finalWeight": 0,
        "publishTime": "string",
        "createdAt": "string"
      }
    ],
    "total": 0,
    "page": 0,
    "size": 0,
    "totalPages": 0
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ApiResponsePageResultNewsDTO](#schemaapiresponsepageresultnewsdto)|

## GET 获取新闻详情

GET /news/{id}

获取新闻详情

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|id|path|integer| 是 |none|

> 返回示例

> 200 Response

```json
{
  "code": 0,
  "message": "string",
  "data": {
    "id": 0,
    "title": "string",
    "summary": "string",
    "content": "string",
    "authorId": 0,
    "authorName": "string",
    "sourceId": 0,
    "sourceName": "string",
    "countryId": 0,
    "countryName": "string",
    "continent": "string",
    "isDomestic": true,
    "viewNum": 0,
    "likeNum": 0,
    "collectNum": 0,
    "finalWeight": 0,
    "publishTime": "string",
    "createdAt": "string"
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ApiResponseNewsDTO](#schemaapiresponsenewsdto)|

## GET 获取政治新闻

GET /news/politics

获取政治新闻

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|page|query|integer| 是 |none|
|size|query|integer| 是 |none|

> 返回示例

> 200 Response

```json
{
  "code": 0,
  "message": "string",
  "data": {
    "list": [
      {
        "id": 0,
        "title": "string",
        "summary": "string",
        "content": "string",
        "authorId": 0,
        "authorName": "string",
        "sourceId": 0,
        "sourceName": "string",
        "countryId": 0,
        "countryName": "string",
        "continent": "string",
        "isDomestic": true,
        "viewNum": 0,
        "likeNum": 0,
        "collectNum": 0,
        "finalWeight": 0,
        "publishTime": "string",
        "createdAt": "string"
      }
    ],
    "total": 0,
    "page": 0,
    "size": 0,
    "totalPages": 0
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ApiResponsePageResultNewsDTO](#schemaapiresponsepageresultnewsdto)|

## GET 获取所有新闻

GET /news/all

获取所有新闻

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|page|query|integer| 是 |none|
|size|query|integer| 是 |none|

> 返回示例

> 200 Response

```json
{
  "code": 0,
  "message": "string",
  "data": {
    "list": [
      {
        "id": 0,
        "title": "string",
        "summary": "string",
        "content": "string",
        "authorId": 0,
        "authorName": "string",
        "sourceId": 0,
        "sourceName": "string",
        "countryId": 0,
        "countryName": "string",
        "continent": "string",
        "isDomestic": true,
        "viewNum": 0,
        "likeNum": 0,
        "collectNum": 0,
        "finalWeight": 0,
        "publishTime": "string",
        "createdAt": "string"
      }
    ],
    "total": 0,
    "page": 0,
    "size": 0,
    "totalPages": 0
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ApiResponsePageResultNewsDTO](#schemaapiresponsepageresultnewsdto)|

# 用户信息

## GET 获取当前用户信息

GET /user/info

获取当前用户信息

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|Authorization|header|string| 否 |none|

> 返回示例

> 200 Response

```json
{
  "code": 0,
  "message": "string",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ApiResponse?](#schemaapiresponse?)|

# 阅读历史

## GET 获取用户阅读历史

GET /history

获取用户阅读历史

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|page|query|integer| 是 |none|
|size|query|integer| 是 |none|
|Authorization|header|string| 否 |none|

> 返回示例

> 200 Response

```json
{
  "code": 0,
  "message": "string",
  "data": {
    "list": [
      {
        "id": 0,
        "newsId": 0,
        "newsTitle": "string",
        "newsType": "string",
        "newsCategory": "string",
        "readAt": "string"
      }
    ],
    "total": 0,
    "page": 0,
    "size": 0,
    "totalPages": 0
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ApiResponsePageResultHistoryDTO](#schemaapiresponsepageresulthistorydto)|

## DELETE 清空历史记录

DELETE /history/clear

清空历史记录

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|Authorization|header|string| 否 |none|

> 返回示例

> 200 Response

```json
{
  "code": 0,
  "message": "string",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ApiResponse?](#schemaapiresponse?)|

## DELETE 删除单条历史记录

DELETE /history/{recordId}

删除单条历史记录

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|recordId|path|integer| 是 |none|
|Authorization|header|string| 否 |none|

> 返回示例

> 200 Response

```json
{
  "code": 0,
  "message": "string",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ApiResponse?](#schemaapiresponse?)|

# 收藏管理

## GET 获取用户收藏列表

GET /favorites/get

获取用户收藏列表

> Body 请求参数

```json
{}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|type|query|string| 否 |none|
|page|query|integer| 是 |none|
|size|query|integer| 是 |none|
|Authorization|header|string| 否 |none|
|body|body|object| 否 |none|

> 返回示例

> 200 Response

```json
{
  "code": 0,
  "message": "string",
  "data": {
    "list": [
      {
        "id": 0,
        "newsId": 0,
        "newsType": "string",
        "newsTitle": "string",
        "createdAt": "string"
      }
    ],
    "total": 0,
    "page": 0,
    "size": 0,
    "totalPages": 0
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ApiResponsePageResultFavoriteDTO](#schemaapiresponsepageresultfavoritedto)|

## POST 添加收藏

POST /favorites/add

添加收藏

> Body 请求参数

```json
{
  "newsId": 1,
  "type": "DOMESTIC",
  "newsTitle": "测试新闻标题",
  "newsCover": "https://example.com/image.jpg"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|Authorization|header|string| 否 |none|
|body|body|[FavoriteRequest](#schemafavoriterequest)| 否 |none|

> 返回示例

> 200 Response

```json
{
  "code": 0,
  "message": "string",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ApiResponse?](#schemaapiresponse?)|

## DELETE 取消收藏

DELETE /favorites/1

取消收藏

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|Authorization|header|string| 否 |none|

> 返回示例

> 200 Response

```json
{
  "code": 0,
  "message": "string",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ApiResponse?](#schemaapiresponse?)|

## GET 检查收藏状态

GET /favorites/check/1

检查收藏状态

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|Authorization|header|string| 否 |none|

> 返回示例

> 200 Response

```json
{
  "code": 0,
  "message": "string",
  "data": {
    "favorited": true
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ApiResponseFavoriteCheckResponse](#schemaapiresponsefavoritecheckresponse)|

# 登录

## POST 用户登录

POST /auth/login

用户登录

> Body 请求参数

```json
{
  "username": "testuser",
  "password": "123456"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|[LoginRequest](#schemaloginrequest)| 否 |none|

> 返回示例

> 200 Response

```json
{
  "code": 0,
  "message": "string",
  "data": {
    "id": 0,
    "username": "string",
    "avatar": "string",
    "token": "string",
    "refreshToken": "string"
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ApiResponseLoginResponse](#schemaapiresponseloginresponse)|

## POST 用户注册

POST /auth/register

用户注册

> Body 请求参数

```json
{
  "username": "testuser",
  "password": "password123"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|[RegisterRequest](#schemaregisterrequest)| 否 |none|

> 返回示例

> 200 Response

```json
{
  "code": 0,
  "message": "string",
  "data": {
    "id": 0,
    "username": "string",
    "avatar": "string",
    "token": "string",
    "refreshToken": "string"
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ApiResponseLoginResponse](#schemaapiresponseloginresponse)|

## POST 刷新Token接口

POST /auth/refresh

> Body 请求参数

```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|Content-Type|header|string| 否 |none|
|body|body|object| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# 反馈管理

## POST 提交反馈

POST /feedback

提交反馈

> Body 请求参数

```json
{
  "type": "BUG",
  "subject": "测试反馈标题",
  "description": "这是一段测试反馈描述内容",
  "contact": "test@example.com"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|[FeedbackRequest](#schemafeedbackrequest)| 否 |none|

> 返回示例

> 200 Response

```json
{
  "code": 0,
  "message": "string",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ApiResponse?](#schemaapiresponse?)|

# 数据模型

<h2 id="tocS_ApiResponseObject">ApiResponseObject</h2>

<a id="schemaapiresponseobject"></a>
<a id="schema_ApiResponseObject"></a>
<a id="tocSapiresponseobject"></a>
<a id="tocsapiresponseobject"></a>

```json
{}

```

### 属性

*None*

<h2 id="tocS_LoginResponse">LoginResponse</h2>

<a id="schemaloginresponse"></a>
<a id="schema_LoginResponse"></a>
<a id="tocSloginresponse"></a>
<a id="tocsloginresponse"></a>

```json
{
  "id": 0,
  "username": "string",
  "avatar": "string",
  "token": "string",
  "refreshToken": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|id|integer(int64)|false|none||none|
|username|string|false|none||none|
|avatar|string|false|none||none|
|token|string|false|none||none|
|refreshToken|string|false|none||none|

<h2 id="tocS_ApiResponsePageResultNewsDTO">ApiResponsePageResultNewsDTO</h2>

<a id="schemaapiresponsepageresultnewsdto"></a>
<a id="schema_ApiResponsePageResultNewsDTO"></a>
<a id="tocSapiresponsepageresultnewsdto"></a>
<a id="tocsapiresponsepageresultnewsdto"></a>

```json
{
  "code": 0,
  "message": "string",
  "data": {
    "list": [
      {
        "id": 0,
        "title": "string",
        "summary": "string",
        "content": "string",
        "authorId": 0,
        "authorName": "string",
        "sourceId": 0,
        "sourceName": "string",
        "countryId": 0,
        "countryName": "string",
        "continent": "string",
        "isDomestic": true,
        "viewNum": 0,
        "likeNum": 0,
        "collectNum": 0,
        "finalWeight": 0,
        "publishTime": "string",
        "createdAt": "string"
      }
    ],
    "total": 0,
    "page": 0,
    "size": 0,
    "totalPages": 0
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|code|integer|false|none||none|
|message|string|false|none||none|
|data|[PageResultNewsDTO](#schemapageresultnewsdto)|false|none||none|

<h2 id="tocS_ApiResponseLoginResponse">ApiResponseLoginResponse</h2>

<a id="schemaapiresponseloginresponse"></a>
<a id="schema_ApiResponseLoginResponse"></a>
<a id="tocSapiresponseloginresponse"></a>
<a id="tocsapiresponseloginresponse"></a>

```json
{
  "code": 0,
  "message": "string",
  "data": {
    "id": 0,
    "username": "string",
    "avatar": "string",
    "token": "string",
    "refreshToken": "string"
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|code|integer|false|none||none|
|message|string|false|none||none|
|data|[LoginResponse](#schemaloginresponse)|false|none||none|

<h2 id="tocS_ApiResponse?">ApiResponse?</h2>

<a id="schemaapiresponse?"></a>
<a id="schema_ApiResponse?"></a>
<a id="tocSapiresponse?"></a>
<a id="tocsapiresponse?"></a>

```json
{
  "code": 0,
  "message": "string",
  "data": null
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|code|integer|false|none||none|
|message|string|false|none||none|
|data|null|false|none||none|

<h2 id="tocS_RegisterRequest">RegisterRequest</h2>

<a id="schemaregisterrequest"></a>
<a id="schema_RegisterRequest"></a>
<a id="tocSregisterrequest"></a>
<a id="tocsregisterrequest"></a>

```json
{
  "username": "string",
  "password": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|username|string|true|none||none|
|password|string|true|none||none|

<h2 id="tocS_LoginRequest">LoginRequest</h2>

<a id="schemaloginrequest"></a>
<a id="schema_LoginRequest"></a>
<a id="tocSloginrequest"></a>
<a id="tocsloginrequest"></a>

```json
{
  "username": "string",
  "password": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|username|string|true|none||none|
|password|string|true|none||none|

<h2 id="tocS_RefreshTokenRequest">RefreshTokenRequest</h2>

<a id="schemarefreshtokenrequest"></a>
<a id="schema_RefreshTokenRequest"></a>
<a id="tocSrefreshtokenrequest"></a>
<a id="tocsrefreshtokenrequest"></a>

```json
{
  "refreshToken": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|refreshToken|string|true|none||none|

<h2 id="tocS_MapString">MapString</h2>

<a id="schemamapstring"></a>
<a id="schema_MapString"></a>
<a id="tocSmapstring"></a>
<a id="tocsmapstring"></a>

```json
{
  "key": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|key|string|false|none||none|

<h2 id="tocS_ApiResponseListMapString">ApiResponseListMapString</h2>

<a id="schemaapiresponselistmapstring"></a>
<a id="schema_ApiResponseListMapString"></a>
<a id="tocSapiresponselistmapstring"></a>
<a id="tocsapiresponselistmapstring"></a>

```json
{
  "code": 0,
  "message": "string",
  "data": [
    {
      "key": "string"
    }
  ]
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|code|integer|false|none||none|
|message|string|false|none||none|
|data|[[MapString](#schemamapstring)]|false|none||none|

<h2 id="tocS_NewsDTO">NewsDTO</h2>

<a id="schemanewsdto"></a>
<a id="schema_NewsDTO"></a>
<a id="tocSnewsdto"></a>
<a id="tocsnewsdto"></a>

```json
{
  "id": 0,
  "title": "string",
  "summary": "string",
  "content": "string",
  "authorId": 0,
  "authorName": "string",
  "sourceId": 0,
  "sourceName": "string",
  "countryId": 0,
  "countryName": "string",
  "continent": "string",
  "isDomestic": true,
  "viewNum": 0,
  "likeNum": 0,
  "collectNum": 0,
  "finalWeight": 0,
  "publishTime": "string",
  "createdAt": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|id|integer|false|none||none|
|title|string|false|none||none|
|summary|string|false|none||none|
|content|string|false|none||none|
|authorId|integer|false|none||关联信息|
|authorName|string|false|none||none|
|sourceId|integer|false|none||none|
|sourceName|string|false|none||none|
|countryId|integer|false|none||none|
|countryName|string|false|none||none|
|continent|string|false|none||none|
|isDomestic|boolean|false|none||类型和统计|
|viewNum|integer|false|none||none|
|likeNum|integer|false|none||none|
|collectNum|integer|false|none||none|
|finalWeight|number|false|none||none|
|publishTime|string|false|none||none|
|createdAt|string|false|none||none|

<h2 id="tocS_ApiResponseListNewsDTO">ApiResponseListNewsDTO</h2>

<a id="schemaapiresponselistnewsdto"></a>
<a id="schema_ApiResponseListNewsDTO"></a>
<a id="tocSapiresponselistnewsdto"></a>
<a id="tocsapiresponselistnewsdto"></a>

```json
{
  "code": 0,
  "message": "string",
  "data": [
    {
      "id": 0,
      "title": "string",
      "summary": "string",
      "content": "string",
      "authorId": 0,
      "authorName": "string",
      "sourceId": 0,
      "sourceName": "string",
      "countryId": 0,
      "countryName": "string",
      "continent": "string",
      "isDomestic": true,
      "viewNum": 0,
      "likeNum": 0,
      "collectNum": 0,
      "finalWeight": 0,
      "publishTime": "string",
      "createdAt": "string"
    }
  ]
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|code|integer|false|none||none|
|message|string|false|none||none|
|data|[[NewsDTO](#schemanewsdto)]|false|none||none|

<h2 id="tocS_PageResultNewsDTO">PageResultNewsDTO</h2>

<a id="schemapageresultnewsdto"></a>
<a id="schema_PageResultNewsDTO"></a>
<a id="tocSpageresultnewsdto"></a>
<a id="tocspageresultnewsdto"></a>

```json
{
  "list": [
    {
      "id": 0,
      "title": "string",
      "summary": "string",
      "content": "string",
      "authorId": 0,
      "authorName": "string",
      "sourceId": 0,
      "sourceName": "string",
      "countryId": 0,
      "countryName": "string",
      "continent": "string",
      "isDomestic": true,
      "viewNum": 0,
      "likeNum": 0,
      "collectNum": 0,
      "finalWeight": 0,
      "publishTime": "string",
      "createdAt": "string"
    }
  ],
  "total": 0,
  "page": 0,
  "size": 0,
  "totalPages": 0
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|list|[[NewsDTO](#schemanewsdto)]|false|none||none|
|total|integer(int64)|false|none||none|
|page|integer|false|none||none|
|size|integer|false|none||none|
|totalPages|integer|false|none||none|

<h2 id="tocS_ApiResponseNewsDTO">ApiResponseNewsDTO</h2>

<a id="schemaapiresponsenewsdto"></a>
<a id="schema_ApiResponseNewsDTO"></a>
<a id="tocSapiresponsenewsdto"></a>
<a id="tocsapiresponsenewsdto"></a>

```json
{
  "code": 0,
  "message": "string",
  "data": {
    "id": 0,
    "title": "string",
    "summary": "string",
    "content": "string",
    "authorId": 0,
    "authorName": "string",
    "sourceId": 0,
    "sourceName": "string",
    "countryId": 0,
    "countryName": "string",
    "continent": "string",
    "isDomestic": true,
    "viewNum": 0,
    "likeNum": 0,
    "collectNum": 0,
    "finalWeight": 0,
    "publishTime": "string",
    "createdAt": "string"
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|code|integer|false|none||none|
|message|string|false|none||none|
|data|[NewsDTO](#schemanewsdto)|false|none||none|

<h2 id="tocS_HistoryDTO">HistoryDTO</h2>

<a id="schemahistorydto"></a>
<a id="schema_HistoryDTO"></a>
<a id="tocShistorydto"></a>
<a id="tocshistorydto"></a>

```json
{
  "id": 0,
  "newsId": 0,
  "newsTitle": "string",
  "newsType": "string",
  "newsCategory": "string",
  "readAt": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|id|integer(int64)|false|none||none|
|newsId|integer(int64)|false|none||none|
|newsTitle|string|false|none||none|
|newsType|string|false|none||none|
|newsCategory|string|false|none||none|
|readAt|string|false|none||none|

<h2 id="tocS_PageResultHistoryDTO">PageResultHistoryDTO</h2>

<a id="schemapageresulthistorydto"></a>
<a id="schema_PageResultHistoryDTO"></a>
<a id="tocSpageresulthistorydto"></a>
<a id="tocspageresulthistorydto"></a>

```json
{
  "list": [
    {
      "id": 0,
      "newsId": 0,
      "newsTitle": "string",
      "newsType": "string",
      "newsCategory": "string",
      "readAt": "string"
    }
  ],
  "total": 0,
  "page": 0,
  "size": 0,
  "totalPages": 0
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|list|[[HistoryDTO](#schemahistorydto)]|false|none||none|
|total|integer(int64)|false|none||none|
|page|integer|false|none||none|
|size|integer|false|none||none|
|totalPages|integer|false|none||none|

<h2 id="tocS_ApiResponsePageResultHistoryDTO">ApiResponsePageResultHistoryDTO</h2>

<a id="schemaapiresponsepageresulthistorydto"></a>
<a id="schema_ApiResponsePageResultHistoryDTO"></a>
<a id="tocSapiresponsepageresulthistorydto"></a>
<a id="tocsapiresponsepageresulthistorydto"></a>

```json
{
  "code": 0,
  "message": "string",
  "data": {
    "list": [
      {
        "id": 0,
        "newsId": 0,
        "newsTitle": "string",
        "newsType": "string",
        "newsCategory": "string",
        "readAt": "string"
      }
    ],
    "total": 0,
    "page": 0,
    "size": 0,
    "totalPages": 0
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|code|integer|false|none||none|
|message|string|false|none||none|
|data|[PageResultHistoryDTO](#schemapageresulthistorydto)|false|none||none|

<h2 id="tocS_FavoriteDTO">FavoriteDTO</h2>

<a id="schemafavoritedto"></a>
<a id="schema_FavoriteDTO"></a>
<a id="tocSfavoritedto"></a>
<a id="tocsfavoritedto"></a>

```json
{
  "id": 0,
  "newsId": 0,
  "newsType": "string",
  "newsTitle": "string",
  "createdAt": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|id|integer(int64)|false|none||none|
|newsId|integer(int64)|false|none||none|
|newsType|string|false|none||none|
|newsTitle|string|false|none||none|
|createdAt|string|false|none||none|

<h2 id="tocS_PageResultFavoriteDTO">PageResultFavoriteDTO</h2>

<a id="schemapageresultfavoritedto"></a>
<a id="schema_PageResultFavoriteDTO"></a>
<a id="tocSpageresultfavoritedto"></a>
<a id="tocspageresultfavoritedto"></a>

```json
{
  "list": [
    {
      "id": 0,
      "newsId": 0,
      "newsType": "string",
      "newsTitle": "string",
      "createdAt": "string"
    }
  ],
  "total": 0,
  "page": 0,
  "size": 0,
  "totalPages": 0
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|list|[[FavoriteDTO](#schemafavoritedto)]|false|none||none|
|total|integer(int64)|false|none||none|
|page|integer|false|none||none|
|size|integer|false|none||none|
|totalPages|integer|false|none||none|

<h2 id="tocS_ApiResponsePageResultFavoriteDTO">ApiResponsePageResultFavoriteDTO</h2>

<a id="schemaapiresponsepageresultfavoritedto"></a>
<a id="schema_ApiResponsePageResultFavoriteDTO"></a>
<a id="tocSapiresponsepageresultfavoritedto"></a>
<a id="tocsapiresponsepageresultfavoritedto"></a>

```json
{
  "code": 0,
  "message": "string",
  "data": {
    "list": [
      {
        "id": 0,
        "newsId": 0,
        "newsType": "string",
        "newsTitle": "string",
        "createdAt": "string"
      }
    ],
    "total": 0,
    "page": 0,
    "size": 0,
    "totalPages": 0
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|code|integer|false|none||none|
|message|string|false|none||none|
|data|[PageResultFavoriteDTO](#schemapageresultfavoritedto)|false|none||none|

<h2 id="tocS_FavoriteRequest">FavoriteRequest</h2>

<a id="schemafavoriterequest"></a>
<a id="schema_FavoriteRequest"></a>
<a id="tocSfavoriterequest"></a>
<a id="tocsfavoriterequest"></a>

```json
{
  "newsId": 0,
  "type": "string",
  "newsTitle": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|newsId|integer(int64)|true|none||none|
|type|string|true|none||none|
|newsTitle|string|true|none||none|

<h2 id="tocS_FavoriteCheckResponse">FavoriteCheckResponse</h2>

<a id="schemafavoritecheckresponse"></a>
<a id="schema_FavoriteCheckResponse"></a>
<a id="tocSfavoritecheckresponse"></a>
<a id="tocsfavoritecheckresponse"></a>

```json
{
  "favorited": true
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|favorited|boolean|false|none||none|

<h2 id="tocS_ApiResponseFavoriteCheckResponse">ApiResponseFavoriteCheckResponse</h2>

<a id="schemaapiresponsefavoritecheckresponse"></a>
<a id="schema_ApiResponseFavoriteCheckResponse"></a>
<a id="tocSapiresponsefavoritecheckresponse"></a>
<a id="tocsapiresponsefavoritecheckresponse"></a>

```json
{
  "code": 0,
  "message": "string",
  "data": {
    "favorited": true
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|code|integer|false|none||none|
|message|string|false|none||none|
|data|[FavoriteCheckResponse](#schemafavoritecheckresponse)|false|none||none|

<h2 id="tocS_FeedbackRequest">FeedbackRequest</h2>

<a id="schemafeedbackrequest"></a>
<a id="schema_FeedbackRequest"></a>
<a id="tocSfeedbackrequest"></a>
<a id="tocsfeedbackrequest"></a>

```json
{
  "type": "string",
  "subject": "string",
  "description": "string",
  "contact": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|type|string|true|none||none|
|subject|string|true|none||none|
|description|string|true|none||none|
|contact|string|false|none||none|

