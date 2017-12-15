package com.pricecompare.entities;

<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
=======
import lombok.*;

import javax.persistence.*;
>>>>>>> 5e02cfaa60e77e9214dbc8d27e6d0465fb7db5da

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
    private BigDecimal price;

    @Column(name = "url")
    private String url;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "products_agents_fk1"))
    Agent agent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "products_agents_fk0"))
    Product product;
}
