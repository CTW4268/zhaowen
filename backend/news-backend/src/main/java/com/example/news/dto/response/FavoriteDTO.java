package com.example.news.dto.response;

import lombok.Data;

/**
 * 收藏信息响应 DTO
 */
@Data
public class FavoriteDTO {

    private Long id;
    private Long newsId;
    private String newsType;
    private String newsTitle;
    private String newsCover;
    private String createdAt;
}
