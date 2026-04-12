package com.example.news.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 收藏请求 DTO
 */
@Data
public class FavoriteRequest {

    @NotNull(message = "新闻 ID 不能为空")
    private Long newsId;

    @NotBlank(message = "新闻类型不能为空")
    private String type;

    @NotBlank(message = "新闻标题不能为空")
    private String newsTitle;

    private String newsCover;
}
