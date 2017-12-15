package com.pricecompare.common.data.pojos;

import com.pricecompare.common.data.entities.AgentRule;
import com.pricecompare.entities.Agent;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class Wrapper
{
    HashMap<String, String> pId;
    List<Integer> pattern;

    public Wrapper(Agent agent)
    {
        pId = new HashMap<>();
        pattern = new ArrayList<>();
        List<AgentRule> agentRules = new ArrayList<>(agent.getAgentRules());
        agentRules.sort(Comparator.comparingInt(AgentRule::getRuleIndex));
        for (AgentRule rule: agentRules)
        {
            pId.put(rule.getCrawlingRequire().getText(), rule.getFormat());
            pattern.add(rule.getCrawlingRequire().getId());
        }
    }
}
