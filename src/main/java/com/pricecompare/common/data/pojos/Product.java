package com.pricecompare.common.data.pojos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product
{
    private String name;
    private String price;
    public boolean equals(Product p)
    {
        return p.price.equals(price) && p.name.equals(name);
    }
}