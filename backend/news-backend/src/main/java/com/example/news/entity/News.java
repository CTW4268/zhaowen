package com.example.news.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 新闻实体类
 */
@Data
@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(length = 500)
    private String coverImage;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private NewsType type;

    @Column(length = 50)
    private String province;

    @Column(length = 50)
    private String region;

    @Column(length = 50)
    private String country;

    @Column(length = 50)
    private String category;

    @Column(length = 100)
    private String source;

    @Column(length = 100)
    private String author;

    @Column(nullable = false)
    private Integer viewCount = 0;

    @Column(nullable = false)
    private Boolean isCarousel = false;

    @Column
    private Integer carouselOrder;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime publishedAt;

    public enum NewsType {
        DOMESTIC,    // 国内
        OVERSEAS,    // 海外
        POLITICS     // 政治
    }
}
