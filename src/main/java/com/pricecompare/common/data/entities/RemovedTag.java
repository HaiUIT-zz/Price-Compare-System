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
@Table(name = "removed_tags")
public class RemovedTag
{
    @Id
    @SequenceGenerator(name="removed_tags_id_seq", sequenceName="removed_tags_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="removed_tags_id_seq")
    private int id;

    @Column(name = "tag")
    private String tag;
}
