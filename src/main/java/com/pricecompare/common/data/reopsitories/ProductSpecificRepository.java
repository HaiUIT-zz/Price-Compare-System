package com.pricecompare.common.data.reopsitories;

import com.pricecompare.common.data.entities.ProductSpecific;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductSpecificRepository extends JpaRepository<ProductSpecific, Integer>
{
}
