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
@Table(name = "ignored_tags")
public class IgnoredTag
{
    @Id
    @SequenceGenerator(name="ignored_tags_id_seq", sequenceName="ignored_tags_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ignored_tags_id_seq")
    private int id;

    @Column(name = "tag")
    private String tag;
}
