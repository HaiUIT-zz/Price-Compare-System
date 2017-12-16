package com.pricecompare.repositories;

import com.pricecompare.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Integer>
{
    @Query(value = "SELECT p.* FROM products p WHERE lower(p.name) LIKE lower(concat('%',:query,'%'))", nativeQuery = true)
    List<Product> findAllByBrand(@Param("query") String query);

    List<Product> findAllByIdIn(Set<Integer> ids);
}
