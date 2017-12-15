package com.pricecompare.common.data.entities;

import com.pricecompare.entities.Agent;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "agent_loadmore_methods")
public class AgentLoadMore
{
    @Id
    @SequenceGenerator(name="agent_loadmore_methods_id_seq", sequenceName="agent_loadmore_methods_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="agent_loadmore_methods_id_seq")
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "agent_id", foreignKey = @ForeignKey(name = "agent_loadmore_methods_pk0"))
    private Agent agent;

    @Column(name = "method")
    private String method;

    @Column(name = "value")
    private String value;

    @Column(name = "xpath")
    private String xpath;
}
