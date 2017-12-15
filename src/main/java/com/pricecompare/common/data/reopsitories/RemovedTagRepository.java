package com.pricecompare.common.data.reopsitories;

import com.pricecompare.common.data.entities.RemovedTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RemovedTagRepository extends JpaRepository<RemovedTag, Integer>
{
    @Query(value = "select r.tag from removed_tags r", nativeQuery = true)
    List<String> findAllTag();
}
