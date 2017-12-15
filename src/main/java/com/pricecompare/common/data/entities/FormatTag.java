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
@Table(name = "format_tags")
public class FormatTag
{
    @Id
    @SequenceGenerator(name="format_tags_id_seq", sequenceName="format_tags_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="format_tags_id_seq")
    private int id;

    @Column(name = "tag")
    private String tag;
}
