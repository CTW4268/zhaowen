package com.example.news.controller;

import com.example.news.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户信息控制器
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户信息", description = "用户相关信息接口")
public class UserController {

    @GetMapping("/info")
    @Operation(summary = "获取当前用户信息")
    public ApiResponse<?> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ApiResponse.error(401, "请先登录");
        }

        // 这里可以返回更详细的用户信息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", userDetails.getUsername());
        userInfo.put("id", userDetails.getUsername()); // 临时使用用户名作为 ID
        
        return ApiResponse.success(userInfo);
    }
}
