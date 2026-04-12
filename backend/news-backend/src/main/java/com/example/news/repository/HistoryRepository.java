package com.example.news.repository;

import com.example.news.entity.ReadingHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 阅读历史数据访问接口
 */
@Repository
public interface HistoryRepository extends JpaRepository<ReadingHistory, Long> {

    /**
     * 分页查询用户的阅读历史
     */
    Page<ReadingHistory> findByUserIdOrderByReadAtDesc(Long userId, Pageable pageable);

    /**
     * 删除用户的所有历史记录
     */
    void deleteByUserId(Long userId);
}
