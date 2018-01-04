package com.pricecompare.common.data.pojos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgentLoadMoreDTO
{
    private int id;
    private int agentId;
    private int method;
    private String path;
    private String value;
}
