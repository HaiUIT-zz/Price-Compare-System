package com.pricecompare.common.data.pojos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AgentPrice {
    private String agent;
    private BigDecimal price;
}
