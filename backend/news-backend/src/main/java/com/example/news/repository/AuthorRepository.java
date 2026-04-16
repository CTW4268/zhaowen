package com.example.news.repository;

import com.example.news.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 作者数据访问接口
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    /**
     * 根据作者名称查找
     */
    Optional<Author> findByAuthorName(String authorName);

    /**
     * 根据作者名称和国家ID查找
     */
    @Query("SELECT a FROM Author a WHERE a.authorName = :name AND a.countryId = :countryId")
    Optional<Author> findByAuthorNameAndCountryId(String name, Integer countryId);
}
