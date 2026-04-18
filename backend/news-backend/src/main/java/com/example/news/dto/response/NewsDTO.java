package com.example.news.dto.response;

import lombok.Data;

/**
 * 新闻详情响应 DTO
 */
@Data
public class NewsDTO {

    private Long id;
    private String title;
    private String summary;
    private String content;
    private String coverImage;
    private String type;
    private String province;
    private String region;
    private String country;
    private String category;
    private String source;
    private String author;
    private Integer viewCount;
    private Boolean isCarousel;
    private String createdAt;
    private String publishedAt;
}
