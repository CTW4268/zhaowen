package com.example.news.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 新闻来源媒体实体类 - 匹配dbinit.py中的news_source表结构
 */
@Data
@Entity
@Table(name = "news_source")
public class NewsSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "source_name", nullable = false, length = 100)
    private String sourceName;

    private Byte weight = 3;

    private Byte status = 1;
}
