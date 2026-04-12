package com.example.news.repository;

import com.example.news.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 收藏数据访问接口
 */
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    /**
     * 查询用户的收藏列表
     */
    @Query("SELECT f FROM Favorite f WHERE f.userId = :userId " +
           "AND (:type IS NULL OR f.newsType = :type) " +
           "ORDER BY f.createdAt DESC")
    Page<Favorite> findByUserId(@Param("userId") Long userId,
                                 @Param("type") String type,
                                 Pageable pageable);

    /**
     * 检查用户是否已收藏某新闻
     */
    Optional<Favorite> findByUserIdAndNewsId(Long userId, Long newsId);

    /**
     * 删除用户的收藏
     */
    void deleteByUserIdAndNewsId(Long userId, Long newsId);

    /**
     * 统计用户收藏数量
     */
    long countByUserId(Long userId);
}
