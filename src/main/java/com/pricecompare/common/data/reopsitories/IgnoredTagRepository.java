package com.pricecompare.common.data.reopsitories;

import com.pricecompare.common.data.entities.IgnoredTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IgnoredTagRepository extends JpaRepository<IgnoredTag, Integer>
{
    @Query(value = "select i.tag from ignored_tags i", nativeQuery = true)
    List<String> findAllTag();
}
