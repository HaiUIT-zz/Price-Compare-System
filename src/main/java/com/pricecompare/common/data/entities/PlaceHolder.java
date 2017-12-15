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
@Table(name = "placeholders")
public class PlaceHolder
{
    @Id
    @SequenceGenerator(name="placeholders_id_seq", sequenceName="placeholders_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="placeholders_id_seq")
    private int id;

    @Column(name = "value")
    private String value;
}
