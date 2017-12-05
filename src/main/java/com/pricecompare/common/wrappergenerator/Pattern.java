package com.pricecompare.common.wrappergenerator;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class Pattern
{
    private List<Integer> pattern;
    private int endIndex;

    public Pattern()
    {
        endIndex = -1;
        pattern = new ArrayList<>();
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

    public Pattern clone()
    {
        Pattern p = new Pattern();
        Collections.copy(p.pattern, this.pattern);
        p.endIndex = this.endIndex;
        return p;
    }
}
