package com.pricecompare.common.data.reopsitories;

import com.pricecompare.common.data.entities.FormatTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FormatTagRepository extends JpaRepository<FormatTag, Integer>
{
    @Query(value = "select f.tag from format_tags f", nativeQuery = true)
    List<String> findAllTag();
}
