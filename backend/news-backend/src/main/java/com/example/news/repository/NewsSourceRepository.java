package com.example.news.repository;

import com.example.news.entity.NewsSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 新闻来源数据访问接口
 */
@Repository
public interface NewsSourceRepository extends JpaRepository<NewsSource, Integer> {

    /**
     * 根据来源名称查找
     */
    Optional<NewsSource> findBySourceName(String sourceName);
}
