package com.example.news.dto.response;

import lombok.Data;

/**
 * 历史记录响应 DTO
 */
@Data
public class HistoryDTO {

    private Long id;
    private Long newsId;
    private String newsTitle;
    private String newsType;
    private String newsCategory;
    private String readAt;
}
