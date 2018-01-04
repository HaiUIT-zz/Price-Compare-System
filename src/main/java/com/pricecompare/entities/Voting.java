package com.pricecompare.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "voting")
public class Voting {
    @Id
    int id;
    @Column(name = "email")
    String email;
    @Column(name="rating")
    int rating;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "voting_product_id_fkey"))
    Product product;
}
