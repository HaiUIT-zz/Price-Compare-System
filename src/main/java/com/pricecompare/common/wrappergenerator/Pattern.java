package com.pricecompare.common.wrappergenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pattern
{
    private List<Integer> pattern;
    private int endIndex;

    public Pattern()
    {
        endIndex = -1;
        pattern = new ArrayList<>();
    }

    public List<Integer> getPattern()
    {
        return pattern;
    }

    public void setPattern(List<Integer> pattern)
    {
        this.pattern = pattern;
    }

    public int getEndIndex()
    {
        return endIndex;
    }

    public int getStartIndex()
    {
        return pattern.size() - endIndex;
    }

    public void setEndIndex(int endIndex)
    {
        this.endIndex = endIndex;
    }

    public boolean equals(Pattern p)
    {
        if (pattern.size() != p.pattern.size())
        {
            return false;
        }

        for (int i = 0; i < pattern.size(); i++)
        {
            if (!pattern.get(i).equals(p.pattern.get(i)))
            {
                return false;
            }
        }

        return true;
    }

    public Pattern clone(){
        Pattern p = new Pattern();
        Collections.copy(p.pattern, this.pattern);
        p.endIndex = this.endIndex;
        return p;
    }
}
