package com.pricecompare.common.data.pojos;

public class Product
{
    private String name;
    private String price;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public boolean equals(Product p)
    {
        return p.price.equals(price) && p.name.equals(name);
    }
}
