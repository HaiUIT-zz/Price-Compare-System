package com.pricecompare.common.data.pojos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCrawled
{
    private String rawName;
    private String changedName;
    private String price;
    private String possibleInDb;
    private int possibleInDbId;
    public boolean equals(ProductCrawled p)
    {
        return p.price.equals(price) && p.rawName.equals(rawName);
    }
}