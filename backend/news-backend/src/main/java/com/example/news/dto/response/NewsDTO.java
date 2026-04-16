package com.example.news.dto.response;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 新闻响应 DTO - 匹配news-system数据库结构（包含关联信息）
 */
@Data
public class NewsDTO {

    private Integer id;
    private String title;
    private String summary;
    private String content;

    // 关联信息
    private Integer authorId;
    private String authorName;

    private Integer sourceId;
    private String sourceName;

    private Integer countryId;
    private String countryName;
    private String continent;

    // 类型和统计
    private Boolean isDomestic;
    private Integer viewNum;
    private Integer likeNum;
    private Integer collectNum;
    private BigDecimal finalWeight;

    private String publishTime;
    private String createdAt;
}
