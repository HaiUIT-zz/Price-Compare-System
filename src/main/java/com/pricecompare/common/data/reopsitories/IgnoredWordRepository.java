package com.pricecompare.common.data.reopsitories;

import com.pricecompare.common.data.entities.IgnoredWord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IgnoredWordRepository extends JpaRepository<IgnoredWord, Integer>
{
}
