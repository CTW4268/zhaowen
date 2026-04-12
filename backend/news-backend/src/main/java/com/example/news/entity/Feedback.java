package com.example.news.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 反馈实体类
 */
@Data
@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private FeedbackType type;

    @Column(nullable = false, length = 255)
    private String subject;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String contact;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private FeedbackStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public enum FeedbackType {
        BUG,        // Bug 报告
        FEATURE,    // 功能建议
        UI,         // 界面改进
        OTHER       // 其他
    }

    public enum FeedbackStatus {
        PENDING,      // 待处理
        PROCESSING,   // 处理中
        RESOLVED,     // 已解决
        CLOSED        // 已关闭
    }
}
