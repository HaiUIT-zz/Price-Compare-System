package com.pricecompare.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products_agents")
public class ProductAgent
{
    @Id
    @Column(name="id")
    int id;

    @Column(name = "price")
    private double price;

    @Column(name = "url")
    private String url;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "products_agents_fk1"))
    Agent agent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "products_agents_fk0"))
    Product product;
}
