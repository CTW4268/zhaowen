package com.example.news.dto.response;

import lombok.Data;

@Data
public class LoginResponse {

    private Long id;
    private String username;
    private String avatar;
    private String token;
    private String refreshToken;

    public LoginResponse() {
    }

    public LoginResponse(Long id, String username, String avatar, String token, String refreshToken) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
        this.token = token;
        this.refreshToken = refreshToken;
    }
}