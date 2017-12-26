package com.pricecompare.repositories;

import com.pricecompare.entities.ManageVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ManageVoteRepository extends JpaRepository<ManageVote, Integer> {
    @Query(value = "SELECT CASE WHEN product_id = :product_id THEN TRUE ELSE FALSE END FROM manage_vote WHERE ip = :ip", nativeQuery = true)
    Object checkExistVoteOfIpForProduct(@Param("product_id") int product_id, @Param("ip") String ip);

    @Modifying
    @Query(value = "INSERT INTO manage_vote (ip, product_id) VALUES (:ip, :product_id)", nativeQuery = true)
    int addVote(@Param("ip") String ip, @Param("product_id") int product_id);


}
