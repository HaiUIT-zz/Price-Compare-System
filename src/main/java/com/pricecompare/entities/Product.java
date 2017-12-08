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
@Table(name = "products")
public class Product
{
    @Id
    @SequenceGenerator(name="products_id_seq", sequenceName="products_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="products_id_seq")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "image")
    private String image;

    @Column(name = "visit_count")
    private long visit_count;

    @Column(name = "rating")
    private double rating;

    @Column(name = "agent_count")
    private  int agent_count;
}
