package com.example.news.controller;

import com.example.news.dto.request.FavoriteRequest;
import com.example.news.dto.response.ApiResponse;
import com.example.news.dto.response.FavoriteCheckResponse;
import com.example.news.dto.response.FavoriteDTO;
import com.example.news.dto.response.PageResult;
import com.example.news.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * 收藏控制器
 */
@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
@Tag(name = "收藏管理", description = "收藏相关接口")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    @Operation(summary = "获取用户收藏列表")
    public ApiResponse<PageResult<FavoriteDTO>> getFavorites(
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        if (userDetails == null) {
            return ApiResponse.error(401, "请先登录");
        }

        Long userId = getUserId(userDetails);
        return ApiResponse.success(favoriteService.getUserFavorites(userId, type, page, size));
    }

    @PostMapping
    @Operation(summary = "添加收藏")
    public ApiResponse<?> addFavorite(@Valid @RequestBody FavoriteRequest request,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ApiResponse.error(401, "请先登录");
        }

        try {
            Long userId = getUserId(userDetails);
            favoriteService.addFavorite(userId, request.getNewsId(), request.getType(),
                    request.getNewsTitle(), request.getNewsCover());
            return ApiResponse.success();
        } catch (Exception e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @DeleteMapping("/{newsId}")
    @Operation(summary = "取消收藏")
    public ApiResponse<?> removeFavorite(@PathVariable Long newsId,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ApiResponse.error(401, "请先登录");
        }

        Long userId = getUserId(userDetails);
        favoriteService.removeFavorite(userId, newsId);
        return ApiResponse.success();
    }

    @GetMapping("/check/{newsId}")
    @Operation(summary = "检查收藏状态")
    public ApiResponse<FavoriteCheckResponse> checkFavorite(@PathVariable Long newsId,
                                                             @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ApiResponse.success(new FavoriteCheckResponse(false));
        }

        Long userId = getUserId(userDetails);
        boolean favorited = favoriteService.isFavorited(userId, newsId);
        return ApiResponse.success(new FavoriteCheckResponse(favorited));
    }

    /**
     * 从用户名中提取用户 ID（兼容两种情况：纯数字或包含其他信息）
     */
    private Long getUserId(UserDetails userDetails) {
        try {
            return Long.parseLong(userDetails.getUsername());
        } catch (NumberFormatException e) {
            // 如果用户名不是纯数字，可能需要从其他方式获取
            throw new RuntimeException("无法获取用户 ID");
        }
    }
}
