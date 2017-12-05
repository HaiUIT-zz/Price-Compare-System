package com.pricecompare.common.data.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "crawling_requires")
public class CrawlingRequire
{
    @Id
    @SequenceGenerator(name="crawling_requires_id_seq", sequenceName="crawling_requires_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="crawling_requires_id_seq")
    private int id;

    @Column(name = "code")
    private String code;

    @Column(name = "text")
    private String text;

    @OneToMany(mappedBy ="crawlingRequire", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<RequireFormat> requireFormats;

    @OneToMany(mappedBy ="crawlingRequire", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<RequireTerm> requireTerms;
}
