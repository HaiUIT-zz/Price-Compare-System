package com.pricecompare.common.wrappergenerator;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Wrapper
{
    private String target;
    private List<LogicalLine> pattern;

    public Wrapper()
    {
        target = "";
        pattern = new ArrayList<>();
    }
}
