package com.example.news.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 新闻实体类 - 匹配news-system数据库结构（规范化）
 */
@Data
@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 500)
    private String summary;

    // 外键关联
    @Column(name = "author_id")
    private Integer authorId;

    @Column(name = "source_id")
    private Integer sourceId;

    @Column(name = "country_id")
    private Integer countryId;

    @Column(name = "is_domestic")
    private Boolean isDomestic = false;

    @Column(name = "publish_time")
    private LocalDateTime publishTime;

    @Column(name = "view_num")
    private Integer viewNum = 0;

    @Column(name = "like_num")
    private Integer likeNum = 0;

    @Column(name = "collect_num")
    private Integer collectNum = 0;

    @Column(name = "final_weight", precision = 10, scale = 2)
    private BigDecimal finalWeight = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
