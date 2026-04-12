package com.example.news.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 反馈请求 DTO
 */
@Data
public class FeedbackRequest {

    @NotNull(message = "反馈类型不能为空")
    private String type;

    @NotBlank(message = "主题不能为空")
    private String subject;

    @NotBlank(message = "描述不能为空")
    private String description;

    private String contact;
}
