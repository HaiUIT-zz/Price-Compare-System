package com.pricecompare.repositories;

import com.pricecompare.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query(value = "SELECT p.* FROM products p WHERE lower(p.name) LIKE lower(concat('%',:query,'%'))", nativeQuery = true)
    List<Product> findAllByBrand(@Param("query") String query);

    List<Product> findAllByIdIn(Set<Integer> ids);

    @Transactional
    @Modifying
    @Query(value = "UPDATE products SET agent_count = :agentCount WHERE id = :id", nativeQuery = true)
    int updateAgentCount(@Param("agentCount") int agentCount, @Param("id") int id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE products SET visit_count = :visit_count WHERE id = :id", nativeQuery = true)
    int updateVisitCount(@Param("id") int id, @Param("visit_count") int visitCount);

    @Transactional
    @Modifying
    @Query(value = "UPDATE products SET rating_count = :rating_count, rating = :rating  WHERE id = :id", nativeQuery = true)
    void updateRating(@Param("id") int id, @Param("rating_count") int ratingCount, @Param("rating") int rating);

    @Transactional
    @Query(value = "SELECT * FROM products WHERE type LIKE :type", nativeQuery = true)
    List<Product> findByType(@Param("type") String type);

    @Transactional
    @Query(value = "SELECT * FROM products WHERE name ILIKE :keyword", nativeQuery = true)
    List<Product> findProduct(@Param("keyword") String keyword);

    @Modifying
    @Transactional
    @Query(value = "UPDATE products SET rating = (SELECT (SELECT SUM(rating) FROM VOTING WHERE product_id = :product_id) / (SELECT COUNT(*) FROM VOTING WHERE product_id = :product_id) ) WHERE products.id = :product_id", nativeQuery = true)
    void updateRating(@Param("product_id") int product_id);

    @Transactional
    @Query(value = "SELECT DISTINCT p.id, p.name, p.image, p.visit_count, p.rating, p.agent_count, p.rating_count, p.type FROM products p INNER JOIN products_agents pa ON p.id = pa.product_id WHERE (:location = pa.location) AND (pa.price BETWEEN :from AND :to ) AND (  p.name ILIKE '%' || :keyword || '%' )", nativeQuery = true)
    List<Product> getProductsByLocationPriceKeyword(@Param("location") String location, @Param("keyword") String keyword, @Param("from") Integer from, @Param("to") Integer to);

    @Transactional
    @Query(value = "SELECT DISTINCT p.id, p.name, p.image, p.visit_count, p.rating, p.agent_count, p.rating_count, p.type FROM products p INNER JOIN products_agents pa ON p.id = pa.product_id WHERE (:location = pa.location) AND (pa.price BETWEEN :from AND :to )", nativeQuery = true)
    List<Product> getProductsByLocationPrice(@Param("location") String location, @Param("from") Integer from, @Param("to") Integer to);

    @Transactional
    @Query(value = "SELECT DISTINCT p.id, p.name, p.image, p.visit_count, p.rating, p.agent_count, p.rating_count, p.type FROM products p INNER JOIN products_agents pa ON p.id = pa.product_id WHERE (:location = pa.location)", nativeQuery = true)
    List<Product> getProductsByLocation(@Param("location") String location);

    @Transactional
    @Query(value = "SELECT DISTINCT p.id, p.name, p.image, p.visit_count, p.rating, p.agent_count, p.rating_count, p.type FROM products p INNER JOIN products_agents pa ON p.id = pa.product_id WHERE pa.price BETWEEN :from AND :to", nativeQuery = true)
    List<Product> getProductsByPrice(@Param("from") Integer from, @Param("to") Integer to);

    @Transactional
    @Query(value = "SELECT DISTINCT p.id, p.name, p.image, p.visit_count, p.rating, p.agent_count, p.rating_count, p.type FROM products p INNER JOIN products_agents pa ON p.id = pa.product_id WHERE (:location = pa.location) AND (  p.name ILIKE '%' || :keyword || '%' )", nativeQuery = true)
    List<Product> getProductsByLocationKeyword(@Param("location") String location, @Param("keyword") String keyword);

    @Transactional
    @Query(value = "SELECT DISTINCT p.id, p.name, p.image, p.visit_count, p.rating, p.agent_count, p.rating_count, p.type FROM products p INNER JOIN products_agents pa ON p.id = pa.product_id WHERE (pa.price BETWEEN :from AND :to ) AND (  p.name ILIKE '%' || :keyword || '%' )", nativeQuery = true)
    List<Product> getProductsByPriceKeyword(@Param("keyword") String keyword, @Param("from") Integer from, @Param("to") Integer to);

    @Transactional
    @Query(value = "SELECT DISTINCT p.id, p.name, p.image, p.visit_count, p.rating, p.agent_count, p.rating_count, p.type FROM products p INNER JOIN products_agents pa ON p.id = pa.product_id WHERE (  p.name ILIKE '%' || :keyword || '%' )", nativeQuery = true)
    List<Product> getProductsByKeyword( @Param("keyword") String keyword);


}
