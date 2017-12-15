package com.pricecompare.common.data.pojos;

import lombok.Data;

@Data
public class CrawlerOption
{
    private int agent;
    private String query;
    private boolean color;
    private boolean ram;
    private int sleep = 0;
}
