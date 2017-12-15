package com.pricecompare.common.data.entities;

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
@Table(name = "specific_details")
public class SpecificDetail
{
    @Id
    @SequenceGenerator(name="specific_details_id_seq", sequenceName="specific_details_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="specific_details_id_seq")
    private int id;

    @Column(name = "possible_text")
    private String possibleText;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "specific_id", foreignKey = @ForeignKey(name = "specific_details_fk0"))
    private ProductSpecific productSpecific;
}
