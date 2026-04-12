package com.example.news.dto.response;

import lombok.Data;

/**
 * 登录响应 DTO
 */
@Data
public class LoginResponse {

    private Long id;
    private String username;
    private String avatar;
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(Long id, String username, String avatar, String token) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
        this.token = token;
    }
}
