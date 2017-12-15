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
@Table(name = "ignored_words")
public class IgnoredWord
{
    @Id
    @SequenceGenerator(name="ignored_words_id_seq", sequenceName="ignored_words_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ignored_words_id_seq")
    private int id;

    @Column(name = "word")
    private String word;
}
