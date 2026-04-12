package com.example.news.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 阅读历史实体类
 */
@Data
@Entity
@Table(name = "reading_history")
public class ReadingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long newsId;

    @Column(nullable = false, length = 255)
    private String newsTitle;

    @Column(nullable = false, length = 20)
    private String newsType;

    @Column(length = 50)
    private String newsCategory;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime readAt;
}
