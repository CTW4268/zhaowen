package com.example.news.service;

import com.example.news.dto.response.FavoriteDTO;
import com.example.news.dto.response.PageResult;
import com.example.news.entity.Favorite;
import com.example.news.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 收藏服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    /**
     * 获取用户收藏列表
     */
    @Transactional(readOnly = true)
    public PageResult<FavoriteDTO> getUserFavorites(Long userId, String type, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<Favorite> favoritePage = favoriteRepository.findByUserId(userId, type, pageable);
        
        List<FavoriteDTO> dtoList = favoritePage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return PageResult.of(dtoList, favoritePage.getTotalElements(), page, size);
    }

    /**
     * 添加收藏
     */
    @Transactional
    public Favorite addFavorite(Long userId, Long newsId, String type, String newsTitle) {
        // 检查是否已收藏
        Optional<Favorite> existing = favoriteRepository.findByUserIdAndNewsId(userId, newsId);
        if (existing.isPresent()) {
            throw new RuntimeException("已收藏该新闻");
        }

        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setNewsId(newsId);
        favorite.setNewsType(type);
        favorite.setNewsTitle(newsTitle);

        favorite = favoriteRepository.save(favorite);
        log.info("用户 {} 收藏了新闻 {}", userId, newsId);
        return favorite;
    }

    /**
     * 取消收藏
     */
    @Transactional
    public void removeFavorite(Long userId, Long newsId) {
        favoriteRepository.deleteByUserIdAndNewsId(userId, newsId);
        log.info("用户 {} 取消了收藏 {}", userId, newsId);
    }

    /**
     * 检查收藏状态
     */
    @Transactional(readOnly = true)
    public boolean isFavorited(Long userId, Long newsId) {
        return favoriteRepository.findByUserIdAndNewsId(userId, newsId).isPresent();
    }

    /**
     * 实体转 DTO
     */
    private FavoriteDTO convertToDTO(Favorite favorite) {
        FavoriteDTO dto = new FavoriteDTO();
        dto.setId(favorite.getId());
        dto.setNewsId(favorite.getNewsId());
        dto.setNewsType(favorite.getNewsType());
        dto.setNewsTitle(favorite.getNewsTitle());
        if (favorite.getCreatedAt() != null) {
            dto.setCreatedAt(favorite.getCreatedAt().toString());
        }
        return dto;
    }
}
