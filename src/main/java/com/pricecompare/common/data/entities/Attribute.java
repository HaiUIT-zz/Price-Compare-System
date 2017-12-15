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
@Table(name = "attributes")
public class Attribute
{
    @Id
    @SequenceGenerator(name="attributes_id_seq", sequenceName="attributes_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="attributes_id_seq")
    private int id;

    @Column(name = "attribute")
    private String attribute;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "input_styles", joinColumns = @JoinColumn(name = "attribute_id", foreignKey = @ForeignKey(name = "input_styles_fk0")),
            inverseJoinColumns = @JoinColumn(name = "value_id", foreignKey = @ForeignKey(name = "input_styles_fk1")))
    private Set<Value> values;
}
