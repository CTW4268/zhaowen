package com.example.news.dto.response;

import lombok.Data;

/**
 * 收藏信息响应 DTO - 匹配新的数据结构
 */
@Data
public class FavoriteDTO {

    private Long id;
    private Long newsId;
    private String newsType;
    private String newsTitle;
    private String createdAt;
}
