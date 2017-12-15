package com.pricecompare.common.data.entities;

import com.pricecompare.entities.Agent;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "agent_rules")
public class AgentRule implements Serializable
{
    @Id
    @ManyToOne
    @JoinColumn(name = "agent_id", foreignKey = @ForeignKey(name = "agent_rules_fk0"))
    private Agent agent;

    @Id
    @ManyToOne
    @JoinColumn(name = "require_id", foreignKey = @ForeignKey(name = "agent_rules_fk1"))
    private CrawlingRequire crawlingRequire;

    @Column(name = "format")
    private String format;

    @Column(name = "rule_index")
    private int ruleIndex;
}
