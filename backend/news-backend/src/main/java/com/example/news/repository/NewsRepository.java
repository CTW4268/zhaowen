package com.example.news.repository;

import com.example.news.entity.News;
import com.example.news.entity.News.NewsType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 新闻数据访问接口
 */
@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    /**
     * 分页查询轮播图新闻
     */
    List<News> findByIsCarouselTrueOrderByCarouselOrderAsc(Pageable pageable);

    /**
     * 根据类型分页查询新闻
     */
    Page<News> findByType(NewsType type, Pageable pageable);

    /**
     * 查询国内新闻（带省份和分类筛选）
     */
    @Query("SELECT n FROM News n WHERE n.type = 'DOMESTIC' " +
           "AND (:province IS NULL OR n.province = :province) " +
           "AND (:category IS NULL OR n.category = :category) " +
           "ORDER BY n.publishedAt DESC")
    Page<News> findDomesticNews(@Param("province") String province,
                                 @Param("category") String category,
                                 Pageable pageable);

    /**
     * 查询海外新闻（带地区和分类筛选）
     */
    @Query("SELECT n FROM News n WHERE n.type = 'OVERSEAS' " +
           "AND (:region IS NULL OR n.region = :region) " +
           "AND (:country IS NULL OR n.country = :country) " +
           "AND (:category IS NULL OR n.category = :category) " +
           "ORDER BY n.publishedAt DESC")
    Page<News> findOverseasNews(@Param("region") String region,
                                 @Param("country") String country,
                                 @Param("category") String category,
                                 Pageable pageable);

    /**
     * 查询政治新闻
     */
    @Query("SELECT n FROM News n WHERE n.type = 'POLITICS' ORDER BY n.publishedAt DESC")
    Page<News> findPoliticsNews(Pageable pageable);
}
