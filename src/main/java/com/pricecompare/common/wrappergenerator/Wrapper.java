package com.pricecompare.common.wrappergenerator;

import java.util.ArrayList;
import java.util.List;

public class Wrapper
{
    private String target;
    private List<LogicalLine> pattern;

    public Wrapper()
    {
        target = "";
        pattern = new ArrayList<>();
    }

    public String getTarget()
    {
        return target;
    }

    public void setTarget(String target)
    {
        this.target = target;
    }

    public List<LogicalLine> getPattern()
    {
        return pattern;
    }

    public void setPattern(List<LogicalLine> pattern)
    {
        this.pattern = pattern;
    }
}
