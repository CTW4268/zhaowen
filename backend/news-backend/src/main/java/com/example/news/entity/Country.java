package com.example.news.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 国家地区实体类 - 匹配dbinit.py中的country表结构
 */
@Data
@Entity
@Table(name = "country")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "country_name", nullable = false, length = 50)
    private String countryName;

    @Column(length = 20)
    private String continent;

    private Byte weight = 1;

    private Byte status = 1;
}
