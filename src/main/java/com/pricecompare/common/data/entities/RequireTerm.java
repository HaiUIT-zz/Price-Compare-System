package com.pricecompare.common.data.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "require_terms")
public class RequireTerm
{
    @Id
    @SequenceGenerator(name="require_terms_id_seq", sequenceName="require_terms_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="require_terms_id_seq")
    private long id;

    @Column(name = "term")
    private String term;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "require_id", foreignKey = @ForeignKey(name = "require_terms_fk0"))
    private CrawlingRequire crawlingRequire;
}
