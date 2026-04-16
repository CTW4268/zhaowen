package com.example.news.repository;

import com.example.news.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 国家数据访问接口
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {

    /**
     * 根据国家名称查找
     */
    Optional<Country> findByCountryName(String countryName);
}
