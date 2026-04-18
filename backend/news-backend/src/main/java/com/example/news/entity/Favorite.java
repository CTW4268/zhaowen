package com.example.news.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 收藏实体类
 */
@Data
@Entity
@Table(name = "favorites", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "news_id"}))
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long newsId;

    @Column(nullable = false, length = 20)
    private String newsType;

    @Column(nullable = false, length = 255)
    private String newsTitle;

    @Column(length = 500)
    private String newsCover;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
