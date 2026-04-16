package com.example.news.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 新闻作者实体类 - 匹配dbinit.py中的author表结构
 */
@Data
@Entity
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "author_name", nullable = false, length = 50)
    private String authorName;

    @Column(name = "country_id")
    private Integer countryId;

    private Byte status = 1;
}
