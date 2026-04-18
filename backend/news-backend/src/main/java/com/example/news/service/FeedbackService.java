package com.example.news.service;

import com.example.news.entity.Feedback;
import com.example.news.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 反馈服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    /**
     * 提交反馈
     */
    @Transactional
    public Feedback submitFeedback(Long userId, String type, String subject, 
                                    String description, String contact) {
        Feedback feedback = new Feedback();
        feedback.setUserId(userId);
        feedback.setType(Feedback.FeedbackType.valueOf(type.toUpperCase()));
        feedback.setSubject(subject);
        feedback.setDescription(description);
        feedback.setContact(contact);
        feedback.setStatus(Feedback.FeedbackStatus.PENDING);

        feedback = feedbackRepository.save(feedback);
        log.info("用户 {} 提交了反馈：{}", userId != null ? userId : "匿名", subject);
        return feedback;
    }
}
