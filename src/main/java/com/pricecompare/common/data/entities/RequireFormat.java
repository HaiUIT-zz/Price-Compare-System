package com.pricecompare.common.data.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "require_formats")
public class RequireFormat
{
    @Id
    @SequenceGenerator(name="require_formats_id_seq", sequenceName="require_formats_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="require_formats_id_seq")
    private long id;

    @Column(name = "format")
    private String format;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "require_id")
    private CrawlingRequire crawlingRequire;
}
