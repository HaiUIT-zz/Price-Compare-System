package com.pricecompare.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products_agents")
public class ProductAgent {

    @Id
    @SequenceGenerator(name="products_agents_id_seq", sequenceName="products_agents_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="products_agents_id_seq")
    @Column(name = "id")
    int id;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "url")
    private String url;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "products_agents_fk1"))
    Agent agent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "products_agents_fk0"))
    Product product;

    @Column(name = "location")
    private String location;
}
