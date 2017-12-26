package com.pricecompare.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "manage_vote")
public class ManageVote {
    @Id
    @Column(name="id")
    int id;

    @Column(name="ip")
    String ip;

    @Column(name="product_id")
    int product_id;
}
