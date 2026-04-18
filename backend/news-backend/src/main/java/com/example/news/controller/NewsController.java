package com.example.news.controller;

import com.example.news.dto.response.ApiResponse;
import com.example.news.dto.response.NewsDTO;
import com.example.news.dto.response.PageResult;
import com.example.news.entity.News;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Tag(name = "新闻管理", description = "新闻查询相关接口")
public class NewsController {

    private final NewsService newsService;
    private final HistoryService historyService;
    private static final Logger log = LoggerFactory.getLogger(NewsController.class);

    @GetMapping("/carousel")
    @Operation(summary = "获取轮播图新闻")
    public ApiResponse<List<NewsDTO>> getCarouselNews() {
        List<NewsDTO> carouselList = newsService.getCarouselNews();
        return ApiResponse.success(carouselList);
    }

    @GetMapping("/recommend")
    @Operation(summary = "获取首页推荐新闻")
    public ApiResponse<PageResult<NewsDTO>> getRecommendNews(
            @RequestParam(required = false, defaultValue = "domestic") String type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        News.NewsType newsType;
        try {
            newsType = News.NewsType.valueOf(type.toUpperCase());
        } catch (Exception e) {
            newsType = News.NewsType.DOMESTIC;
        }

        PageResult<NewsDTO> result = newsService.getRecommendNews(newsType, page, size);

        return ApiResponse.success(result);
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
    public ApiResponse<NewsDTO> getNewsDetail(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        NewsDTO news = newsService.getNewsById(id);

        if (userDetails != null) {
            try {
                Long userId = Long.parseLong(userDetails.getUsername());
                historyService.addHistory(userId, id, news.getTitle(), news.getType(), news.getCategory());
            } catch (Exception e) {
                log.warn("记录阅读历史失败，用户: {}, 新闻ID: {}", userDetails.getUsername(), id);
            }
        }

        return ApiResponse.success(news);
    }
}