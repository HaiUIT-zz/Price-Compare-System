package com.pricecompare.repositories;

import com.pricecompare.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Integer>
{
    @Query(value = "SELECT p.* FROM products p WHERE lower(p.name) LIKE lower(concat('%',:query,'%'))", nativeQuery = true)
    List<Product> findAllByBrand(@Param("query") String query);

    List<Product> findAllByIdIn(Set<Integer> ids);

    @Transactional
    @Modifying
    @Query(value = "UPDATE products SET visit_count = :visit_count WHERE id = :id", nativeQuery = true)
    int updateVisitCount(@Param("id") int id, @Param("visit_count") int visitCount);

    @Transactional
    @Modifying
    @Query(value = "UPDATE products SET rating_count = :rating_count, rating = :rating  WHERE id = :id", nativeQuery = true)
    int updateRating(@Param("id") int id, @Param("rating_count") int ratingCount, @Param("rating") int rating);
}
