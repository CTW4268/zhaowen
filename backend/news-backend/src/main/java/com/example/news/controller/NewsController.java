package com.example.news.controller;

import com.example.news.dto.response.ApiResponse;
import com.example.news.dto.response.NewsDTO;
import com.example.news.dto.response.PageResult;
import com.example.news.service.HistoryService;
import com.example.news.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Tag(name = "新闻管理", description = "新闻查询相关接口")
public class NewsController {

    private final NewsService newsService;
    private final HistoryService historyService;
    private static final Logger log = LoggerFactory.getLogger(NewsController.class);

    @GetMapping("/domestic")
    @Operation(summary = "获取国内新闻")
    public ApiResponse<PageResult<NewsDTO>> getDomesticNews(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(newsService.getDomesticNews(page, size));
    }

    @GetMapping("/overseas")
    @Operation(summary = "获取海外新闻")
    public ApiResponse<PageResult<NewsDTO>> getOverseasNews(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(newsService.getOverseasNews(page, size));
    }

    @GetMapping("/all")
    @Operation(summary = "获取所有新闻")
    public ApiResponse<PageResult<NewsDTO>> getAllNews(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(newsService.getAllNews(page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取新闻详情")
    public ApiResponse<NewsDTO> getNewsDetail(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        NewsDTO news = newsService.getNewsById(id);

        // 记录阅读历史（如果用户已登录）
        if (userDetails != null && news != null) {
            try {
                Long userId = Long.parseLong(userDetails.getUsername());
                String newsType = news.getIsDomestic() != null && news.getIsDomestic() ? "DOMESTIC" : "OVERSEAS";
                historyService.addHistory(userId, id.longValue(), news.getTitle(), newsType, null);
            } catch (Exception e) {
                log.warn("记录阅读历史失败，用户: {}, 新闻ID: {}", userDetails.getUsername(), id);
            }
        }

        return ApiResponse.success(news);
    }
}
