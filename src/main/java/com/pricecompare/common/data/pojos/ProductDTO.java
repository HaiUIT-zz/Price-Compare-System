package com.pricecompare.common.data.pojos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO
{
    private String rawName;
    private String changedName;
    private String price;
    private String possibleInDb;
    private int possibleInDbId;
    public boolean equals(ProductDTO p)
    {
        return p.price.equals(price) && p.rawName.equals(rawName);
    }
}