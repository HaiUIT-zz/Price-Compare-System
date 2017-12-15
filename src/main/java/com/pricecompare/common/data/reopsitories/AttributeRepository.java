package com.pricecompare.common.data.reopsitories;

import com.pricecompare.common.data.entities.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeRepository extends JpaRepository<Attribute, Integer>
{
}
