package com.example.news.controller;

import com.example.news.dto.response.ApiResponse;
import com.example.news.dto.response.NewsDTO;
import com.example.news.dto.response.PageResult;
import com.example.news.service.HistoryService;
import com.example.news.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * 新闻控制器
 */
@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Tag(name = "新闻管理", description = "新闻查询相关接口")
public class NewsController {

    private final NewsService newsService;
    private final HistoryService historyService;

    @GetMapping("/carousel")
    @Operation(summary = "获取轮播图新闻")
    public ApiResponse<?> getCarouselNews() {
        return ApiResponse.success(newsService.getCarouselNews());
    }

    @GetMapping("/recommend")
    @Operation(summary = "获取首页推荐新闻")
    public ApiResponse<PageResult<NewsDTO>> getRecommendNews(
            @RequestParam(defaultValue = "domestic") String type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(newsService.getRecommendNews(type, page, size));
    }

    @GetMapping("/domestic")
    @Operation(summary = "获取国内新闻")
    public ApiResponse<PageResult<NewsDTO>> getDomesticNews(
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(newsService.getDomesticNews(province, category, page, size));
    }

    @GetMapping("/overseas")
    @Operation(summary = "获取海外新闻")
    public ApiResponse<PageResult<NewsDTO>> getOverseasNews(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(newsService.getOverseasNews(region, country, category, page, size));
    }

    @GetMapping("/politics")
    @Operation(summary = "获取政治新闻")
    public ApiResponse<PageResult<NewsDTO>> getPoliticsNews(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(newsService.getPoliticsNews(page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取新闻详情")
    public ApiResponse<NewsDTO> getNewsDetail(@PathVariable Long id,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        NewsDTO news = newsService.getNewsById(id);
        
        // 如果已登录，自动记录阅读历史
        if (userDetails != null) {
            try {
                Long userId = Long.parseLong(userDetails.getUsername());
                historyService.addHistory(userId, id, news.getTitle(), 
                        news.getType(), news.getCategory());
            } catch (Exception e) {
                // 记录历史失败不影响返回新闻
            }
        }
        
        return ApiResponse.success(news);
    }
}
