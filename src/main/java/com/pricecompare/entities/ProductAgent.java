package com.pricecompare.entities;

import com.pricecompare.common.data.entities.RequireFormat;
import com.pricecompare.common.data.entities.RequireTerm;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

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
