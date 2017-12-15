package com.pricecompare.repositories;

import com.pricecompare.entities.ProductAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductAgentRepository extends  JpaRepository<ProductAgent, Integer> {
}

