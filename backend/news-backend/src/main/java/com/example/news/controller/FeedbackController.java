package com.example.news.controller;

import com.example.news.dto.request.FeedbackRequest;
import com.example.news.dto.response.ApiResponse;
import com.example.news.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 反馈控制器
 */
@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
@Tag(name = "反馈管理", description = "用户反馈相关接口")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    @Operation(summary = "提交反馈")
    public ApiResponse<?> submitFeedback(@Valid @RequestBody FeedbackRequest request,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Long userId = null;
            if (userDetails != null) {
                try {
                    userId = Long.parseLong(userDetails.getUsername());
                } catch (NumberFormatException e) {
                    // 匿名用户
                }
            }

            feedbackService.submitFeedback(userId, request.getType(), request.getSubject(),
                    request.getDescription(), request.getContact());
            return ApiResponse.success();
        } catch (Exception e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }
}
