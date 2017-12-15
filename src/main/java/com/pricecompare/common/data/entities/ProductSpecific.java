package com.pricecompare.common.data.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_specifics")
public class ProductSpecific
{
    @Id
    @SequenceGenerator(name="product_specifics_id_seq", sequenceName="product_specifics_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="product_specifics_id_seq")
    private int id;

    @Column(name = "code")
    private String code;

    @Column(name = "text")
    private String text;

    @OneToMany(mappedBy = "productSpecific" , cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<SpecificDetail> specificDetails;
}
