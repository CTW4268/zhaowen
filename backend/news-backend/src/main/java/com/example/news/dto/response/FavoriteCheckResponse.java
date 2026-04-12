package com.example.news.dto.response;

import lombok.Data;

/**
 * 收藏状态检查响应
 */
@Data
public class FavoriteCheckResponse {

    private Boolean favorited;

    public FavoriteCheckResponse() {
    }

    public FavoriteCheckResponse(Boolean favorited) {
        this.favorited = favorited;
    }
}
