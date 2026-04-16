package com.example.news.repository;

import com.example.news.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 新闻数据访问接口 - 匹配news-system数据库结构
 */
@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {

    /**
     * 查询国内新闻（is_domestic = 1）
     */
    @Query("SELECT n FROM News n WHERE n.isDomestic = true ORDER BY n.publishTime DESC")
    Page<News> findDomesticNews(Pageable pageable);

    /**
     * 查询海外新闻（is_domestic = 0）
     */
    @Query("SELECT n FROM News n WHERE n.isDomestic = false ORDER BY n.publishTime DESC")
    Page<News> findOverseasNews(Pageable pageable);

    /**
     * 根据发布时间排序查询所有新闻
     */
    @Query("SELECT n FROM News n ORDER BY n.publishTime DESC")
    Page<News> findAllOrderByPublishTimeDesc(Pageable pageable);

    /**
     * 根据最终权重排序查询新闻
     */
    @Query("SELECT n FROM News n ORDER BY n.finalWeight DESC, n.publishTime DESC")
    Page<News> findByFinalWeightOrderByPublishTimeDesc(Pageable pageable);

    /**
     * 根据国家ID查询新闻
     */
    @Query("SELECT n FROM News n WHERE n.countryId = :countryId ORDER BY n.publishTime DESC")
    Page<News> findByCountryId(@Param("countryId") Integer countryId, Pageable pageable);

    /**
     * 根据作者ID查询新闻
     */
    @Query("SELECT n FROM News n WHERE n.authorId = :authorId ORDER BY n.publishTime DESC")
    Page<News> findByAuthorId(@Param("authorId") Integer authorId, Pageable pageable);

    /**
     * 根据来源ID查询新闻
     */
    @Query("SELECT n FROM News n WHERE n.sourceId = :sourceId ORDER BY n.publishTime DESC")
    Page<News> findBySourceId(@Param("sourceId") Integer sourceId, Pageable pageable);
}
