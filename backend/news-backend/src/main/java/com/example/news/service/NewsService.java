package com.example.news.service;

import com.example.news.dto.response.NewsDTO;
import com.example.news.dto.response.PageResult;
import com.example.news.entity.Author;
import com.example.news.entity.Country;
import com.example.news.entity.News;
import com.example.news.entity.NewsSource;
import com.example.news.repository.AuthorRepository;
import com.example.news.repository.CountryRepository;
import com.example.news.repository.NewsRepository;
import com.example.news.repository.NewsSourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 新闻服务 - 匹配news-system数据库结构（规范化）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final AuthorRepository authorRepository;
    private final CountryRepository countryRepository;
    private final NewsSourceRepository newsSourceRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 缓存关联数据，减少查询次数
    private final Map<Integer, String> authorNameCache = new HashMap<>();
    private final Map<Integer, String> sourceNameCache = new HashMap<>();
    private final Map<Integer, Country> countryCache = new HashMap<>();

    /**
     * 获取国内新闻
     */
    @Transactional(readOnly = true)
    public PageResult<NewsDTO> getDomesticNews(int page, int size) {
        log.info("查询国内新闻: page={}, size={}", page, size);
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<News> newsPage = newsRepository.findDomesticNews(pageable);
            log.info("查询结果: total={}", newsPage.getTotalElements());

            List<NewsDTO> dtoList = newsPage.getContent().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            log.info("转换 DTO 完成: count={}", dtoList.size());
            return PageResult.of(dtoList, newsPage.getTotalElements(), page, size);
        } catch (Exception e) {
            log.error("查询国内新闻失败", e);
            throw new RuntimeException("查询国内新闻失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取海外新闻
     */
    @Transactional(readOnly = true)
    public PageResult<NewsDTO> getOverseasNews(int page, int size) {
        log.info("查询海外新闻: page={}, size={}", page, size);
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<News> newsPage = newsRepository.findOverseasNews(pageable);
            log.info("查询结果: total={}", newsPage.getTotalElements());

            List<NewsDTO> dtoList = newsPage.getContent().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            log.info("转换 DTO 完成: count={}", dtoList.size());
            return PageResult.of(dtoList, newsPage.getTotalElements(), page, size);
        } catch (Exception e) {
            log.error("查询海外新闻失败", e);
            throw new RuntimeException("查询海外新闻失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取所有新闻（按发布时间排序）
     */
    @Transactional(readOnly = true)
    public PageResult<NewsDTO> getAllNews(int page, int size) {
        log.info("查询所有新闻: page={}, size={}", page, size);
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<News> newsPage = newsRepository.findAllOrderByPublishTimeDesc(pageable);
            log.info("查询结果: total={}", newsPage.getTotalElements());

            List<NewsDTO> dtoList = newsPage.getContent().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            log.info("转换 DTO 完成: count={}", dtoList.size());
            return PageResult.of(dtoList, newsPage.getTotalElements(), page, size);
        } catch (Exception e) {
            log.error("查询所有新闻失败", e);
            throw new RuntimeException("查询所有新闻失败: " + e.getMessage(), e);
        }
    }

    /**
     * 根据国家ID获取新闻
     */
    @Transactional(readOnly = true)
    public PageResult<NewsDTO> getNewsByCountry(Integer countryId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<News> newsPage = newsRepository.findByCountryId(countryId, pageable);

        List<NewsDTO> dtoList = newsPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return PageResult.of(dtoList, newsPage.getTotalElements(), page, size);
    }

    /**
     * 获取新闻详情
     */
    @Transactional(readOnly = true)
    public NewsDTO getNewsById(Integer id) {
        log.info("查询新闻详情: id={}", id);
        try {
            News news = newsRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("新闻不存在：" + id));

            // 增加浏览次数 - 添加空值保护
            Integer currentViews = news.getViewNum() != null ? news.getViewNum() : 0;
            news.setViewNum(currentViews + 1);
            newsRepository.save(news);
            
            log.info("新闻详情查询成功: id={}, title={}", id, news.getTitle());
            return convertToDTO(news);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("查询新闻详情失败: id={}", id, e);
            throw new RuntimeException("查询新闻详情失败: " + e.getMessage(), e);
        }
    }

    /**
     * 实体转 DTO（包含关联信息）
     */
    private NewsDTO convertToDTO(News news) {
        if (news == null) {
            return null;
        }

        try {
            NewsDTO dto = new NewsDTO();
            dto.setId(news.getId());
            dto.setTitle(news.getTitle());
            dto.setSummary(news.getSummary());
            dto.setContent(news.getContent());

            // 设置类型
            dto.setIsDomestic(news.getIsDomestic() != null ? news.getIsDomestic() : false);

            // 关联作者信息 - 不使用 computeIfAbsent，直接查询
            if (news.getAuthorId() != null) {
                dto.setAuthorId(news.getAuthorId());
                String authorName = authorNameCache.get(news.getAuthorId());
                if (authorName == null) {
                    authorName = authorRepository.findById(news.getAuthorId())
                            .map(Author::getAuthorName)
                            .orElse("未知");
                    authorNameCache.put(news.getAuthorId(), authorName);
                }
                dto.setAuthorName(authorName);
            }

            // 关联来源信息 - 不使用 computeIfAbsent，直接查询
            if (news.getSourceId() != null) {
                dto.setSourceId(news.getSourceId());
                String sourceName = sourceNameCache.get(news.getSourceId());
                if (sourceName == null) {
                    sourceName = newsSourceRepository.findById(news.getSourceId())
                            .map(NewsSource::getSourceName)
                            .orElse("未知");
                    sourceNameCache.put(news.getSourceId(), sourceName);
                }
                dto.setSourceName(sourceName);
            }

            // 关联国家信息 - 不使用 computeIfAbsent，直接查询
            if (news.getCountryId() != null) {
                dto.setCountryId(news.getCountryId());
                Country country = countryCache.get(news.getCountryId());
                if (country == null) {
                    country = countryRepository.findById(news.getCountryId()).orElse(null);
                    if (country != null) {
                        countryCache.put(news.getCountryId(), country);
                    }
                }
                if (country != null) {
                    dto.setCountryName(country.getCountryName());
                    dto.setContinent(country.getContinent());
                }
            }

            // 统计字段 - 添加空值保护
            dto.setViewNum(news.getViewNum() != null ? news.getViewNum() : 0);
            dto.setLikeNum(news.getLikeNum() != null ? news.getLikeNum() : 0);
            dto.setCollectNum(news.getCollectNum() != null ? news.getCollectNum() : 0);
            dto.setFinalWeight(news.getFinalWeight() != null ? news.getFinalWeight() : java.math.BigDecimal.ZERO);

            // 日期格式
            if (news.getPublishTime() != null) {
                dto.setPublishTime(news.getPublishTime().format(FORMATTER));
            }
            if (news.getCreatedAt() != null) {
                dto.setCreatedAt(news.getCreatedAt().format(FORMATTER));
            }

            log.debug("成功转换新闻 DTO: id={}, title={}", news.getId(), news.getTitle());
            return dto;
        } catch (Exception e) {
            log.error("转换新闻 DTO 失败, newsId={}, newsTitle={}", news.getId(), news.getTitle(), e);
            throw new RuntimeException("转换新闻数据失败: " + e.getMessage(), e);
        }
    }
}
