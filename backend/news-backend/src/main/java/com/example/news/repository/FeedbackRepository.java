package com.example.news.repository;

import com.example.news.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 反馈数据访问接口
 */
@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    /**
     * 分页查询反馈列表
     */
    Page<Feedback> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
