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
@Table(name = "values")
public class Value
{
    @Id
    @SequenceGenerator(name="values_id_seq", sequenceName="values_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="values_id_seq")
    private int id;

    @Column(name = "value")
    private String value;

    @ManyToMany(mappedBy = "values")
    private Set<Attribute> attributes;
}
