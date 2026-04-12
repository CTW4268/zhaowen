package com.example.news.service;

import com.example.news.dto.response.HistoryDTO;
import com.example.news.dto.response.PageResult;
import com.example.news.entity.ReadingHistory;
import com.example.news.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 阅读历史服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;

    /**
     * 获取用户阅读历史
     */
    @Transactional(readOnly = true)
    public PageResult<HistoryDTO> getUserHistory(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("readAt").descending());
        Page<ReadingHistory> historyPage = historyRepository.findByUserIdOrderByReadAtDesc(userId, pageable);
        
        List<HistoryDTO> dtoList = historyPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return PageResult.of(dtoList, historyPage.getTotalElements(), page, size);
    }

    /**
     * 添加阅读历史
     */
    @Transactional
    public ReadingHistory addHistory(Long userId, Long newsId, String newsTitle, 
                                      String newsType, String newsCategory) {
        ReadingHistory history = new ReadingHistory();
        history.setUserId(userId);
        history.setNewsId(newsId);
        history.setNewsTitle(newsTitle);
        history.setNewsType(newsType);
        history.setNewsCategory(newsCategory);

        history = historyRepository.save(history);
        log.debug("用户 {} 阅读了新闻 {}", userId, newsId);
        return history;
    }

    /**
     * 清空历史记录
     */
    @Transactional
    public void clearHistory(Long userId) {
        historyRepository.deleteByUserId(userId);
        log.info("清空用户 {} 的阅读历史", userId);
    }

    /**
     * 删除单条历史
     */
    @Transactional
    public void deleteHistory(Long userId, Long recordId) {
        ReadingHistory history = historyRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("历史记录不存在"));
        
        if (!history.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除此历史记录");
        }
        
        historyRepository.delete(history);
        log.info("删除用户 {} 的历史记录 {}", userId, recordId);
    }

    /**
     * 实体转 DTO
     */
    private HistoryDTO convertToDTO(ReadingHistory history) {
        HistoryDTO dto = new HistoryDTO();
        dto.setId(history.getId());
        dto.setNewsId(history.getNewsId());
        dto.setNewsTitle(history.getNewsTitle());
        dto.setNewsType(history.getNewsType());
        dto.setNewsCategory(history.getNewsCategory());
        if (history.getReadAt() != null) {
            dto.setReadAt(history.getReadAt().toString());
        }
        return dto;
    }
}
