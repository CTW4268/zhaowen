package com.example.news.controller;

import com.example.news.dto.response.ApiResponse;
import com.example.news.dto.response.HistoryDTO;
import com.example.news.dto.response.PageResult;
import com.example.news.security.UserDetailsImpl;
import com.example.news.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * 阅读历史控制器
 */
@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
@Tag(name = "阅读历史", description = "历史记录相关接口")
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping
    @Operation(summary = "获取用户阅读历史")
    public ApiResponse<PageResult<HistoryDTO>> getHistory(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        if (userDetails == null) {
            return ApiResponse.error(401, "请先登录");
        }

        Long userId = getUserId(userDetails);
        return ApiResponse.success(historyService.getUserHistory(userId, page, size));
    }

    @DeleteMapping("/clear")
    @Operation(summary = "清空历史记录")
    public ApiResponse<?> clearHistory(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ApiResponse.error(401, "请先登录");
        }

        Long userId = getUserId(userDetails);
        historyService.clearHistory(userId);
        return ApiResponse.success();
    }

    @DeleteMapping("/{recordId}")
    @Operation(summary = "删除单条历史记录")
    public ApiResponse<?> deleteHistory(@PathVariable Long recordId,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ApiResponse.error(401, "请先登录");
        }

        Long userId = getUserId(userDetails);
        historyService.deleteHistory(userId, recordId);
        return ApiResponse.success();
    }

    /**
     * 从用户名中提取用户 ID
     */

    private Long getUserId(UserDetails userDetails) {
        if (userDetails instanceof UserDetailsImpl impl) {
            return impl.getId();
        }
        try {
            return Long.parseLong(userDetails.getUsername());
        } catch (NumberFormatException e) {
            throw new RuntimeException("无法获取用户 ID");
        }
    }
}

