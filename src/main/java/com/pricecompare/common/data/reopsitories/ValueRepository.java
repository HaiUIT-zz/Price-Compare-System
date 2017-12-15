package com.pricecompare.common.data.reopsitories;

import com.pricecompare.common.data.entities.Value;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValueRepository extends JpaRepository<Value, Integer>
{
}
