package com.pricecompare.repositories;

import com.pricecompare.entities.Voting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface VotingRepository extends JpaRepository<Voting, Integer> {
    @Query(value = "SELECT COUNT(*) FROM VOTING WHERE product_id = :product_id AND email = :email", nativeQuery = true)
    int checkExistVoteOfIpForProduct(@Param("product_id") int product_id, @Param("email") String email);

    @Modifying
    @Query(value = "INSERT INTO voting (email, product_id, rating) VALUES (:email, :product_id, :star)", nativeQuery = true)
    int addVote(@Param("email") String email, @Param("product_id") int product_id, @Param("star") int star);

    @Modifying
    @Query(value = "UPDATE voting SET rating = :star WHERE product_id = :product_id AND email = :email", nativeQuery = true)
    int updateVote(@Param("product_id") int product_id, @Param("email") String email, @Param("star") int star);
}
