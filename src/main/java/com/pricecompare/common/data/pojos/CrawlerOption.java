package com.pricecompare.common.data.pojos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CrawlerOption
{
    private int agent;
    private String query;
    private boolean ignoreColor;
    private boolean ignoreRam;
    private int sleep = 0;
}
