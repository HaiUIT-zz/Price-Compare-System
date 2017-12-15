package com.pricecompare.entities;

<<<<<<< HEAD
=======
import com.pricecompare.common.data.entities.AgentLoadMore;
import com.pricecompare.common.data.entities.AgentRule;
import com.pricecompare.common.data.entities.CrawlingRequire;
>>>>>>> 5e02cfaa60e77e9214dbc8d27e6d0465fb7db5da
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
