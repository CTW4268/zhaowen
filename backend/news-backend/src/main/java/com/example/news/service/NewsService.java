package com.example.news.service;

import com.example.news.dto.response.NewsDTO;
import com.example.news.dto.response.PageResult;
import com.example.news.entity.News;
import com.example.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 新闻服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取轮播图新闻
     */
    @Transactional(readOnly = true)
    public List<NewsDTO> getCarouselNews() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("carouselOrder").ascending());
        List<News> carouselList = newsRepository.findByIsCarouselTrueOrderByCarouselOrderAsc(pageable);
        log.info("获取轮播图新闻：{}", carouselList);
        return carouselList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    /**
     * 获取首页推荐新闻
     */
    @Transactional(readOnly = true)
    public PageResult<NewsDTO> getRecommendNews(News.NewsType type, int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("publishedAt").descending());

        Page<News> newsPage = newsRepository.findByType(type, pageable);

        // 3. 转换 DTO
        List<NewsDTO> dtoList = newsPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        // 4. 返回分页结果
        return PageResult.of(dtoList, newsPage.getTotalElements(), page, size);
    }

    /**
     * 获取国内新闻
     */
    @Transactional(readOnly = true)
    public PageResult<NewsDTO> getDomesticNews(String province, String category, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("publishedAt").descending());
        Page<News> newsPage = newsRepository.findDomesticNews(province, category, pageable);
        
        List<NewsDTO> dtoList = newsPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return PageResult.of(dtoList, newsPage.getTotalElements(), page, size);
    }

    /**
     * 获取海外新闻
     */
    @Transactional(readOnly = true)
    public PageResult<NewsDTO> getOverseasNews(String region, String country, String category, 
                                                int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("publishedAt").descending());
        Page<News> newsPage = newsRepository.findOverseasNews(region, country, category, pageable);
        
        List<NewsDTO> dtoList = newsPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return PageResult.of(dtoList, newsPage.getTotalElements(), page, size);
    }

    /**
     * 获取政治新闻
     */
    @Transactional(readOnly = true)
    public PageResult<NewsDTO> getPoliticsNews(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("publishedAt").descending());
        Page<News> newsPage = newsRepository.findPoliticsNews(pageable);
        
        List<NewsDTO> dtoList = newsPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return PageResult.of(dtoList, newsPage.getTotalElements(), page, size);
    }

    /**
     * 获取新闻详情
     */
    @Transactional(readOnly = true)
    public NewsDTO getNewsById(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("新闻不存在：" + id));
        
        // 增加浏览次数
        news.setViewCount(news.getViewCount() + 1);
        newsRepository.save(news);
        
        return convertToDTO(news);
    }

    /**
     * 实体转 DTO
     */
    private NewsDTO convertToDTO(News news) {
        if (news == null) {
            return null;
        }

        NewsDTO dto = new NewsDTO();
        dto.setId(news.getId());
        dto.setTitle(news.getTitle());
        dto.setSummary(news.getSummary());
        dto.setContent(news.getContent());
        dto.setCoverImage(news.getCoverImage());

        // 枚举转字符串
        if (news.getType() != null) {
            dto.setType(news.getType().name());
        }

        dto.setProvince(news.getProvince());
        dto.setRegion(news.getRegion());
        dto.setCountry(news.getCountry());
        dto.setCategory(news.getCategory());
        dto.setSource(news.getSource());
        dto.setAuthor(news.getAuthor());
        dto.setViewCount(news.getViewCount());
        dto.setIsCarousel(news.getIsCarousel());

        // 优化日期格式：使用 formatter 替代 toString()
        if (news.getCreatedAt() != null) {
            dto.setCreatedAt(news.getCreatedAt().format(FORMATTER));
        }

        if (news.getPublishedAt() != null) {
            dto.setPublishedAt(news.getPublishedAt().format(FORMATTER));
        }

        return dto;
    }
}
