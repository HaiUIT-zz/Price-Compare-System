package com.pricecompare.repositories;

import com.pricecompare.entities.ProductAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductAgentRepository extends  JpaRepository<ProductAgent, Integer> {
    @Query(value = "SELECT pa.* FROM products_agents pa WHERE pa.product_id = :p_id AND pa.agent_id = :a_id" , nativeQuery = true)
    List<ProductAgent> getByProductAndAgent(@Param("p_id") int productId, @Param("a_id") int agentId);
}

