package com.pricecompare.entities;

import com.pricecompare.common.data.entities.AgentLoadMore;
import com.pricecompare.common.data.entities.AgentRule;
import com.pricecompare.common.data.entities.CrawlingRequire;
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
@Table(name = "agents")
public class Agent
{
    @Id
    @SequenceGenerator(name="agents_id_seq", sequenceName="agents_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="agents_id_seq")
    @Column(name="id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "search_url")
    private String searchUrl;

    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<AgentRule> agentRules;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "agent", fetch = FetchType.EAGER)
    private Set<ProductAgent> productAgent;

    @OneToMany(mappedBy = "agent")
    private Set<AgentLoadMore> agentLoadMores;
}
