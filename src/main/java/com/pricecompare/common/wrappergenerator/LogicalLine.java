package com.pricecompare.common.wrappergenerator;

/*
* LogicalLine type value :
* 0 = Unknown
* 1 = Price
* 2 = Product name
 */

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LogicalLine
{
    private int objectId;
    private String object;
    private String line;
    private String format;

    public LogicalLine()
    {
        object = "UNKNOWN";
        objectId = 0;
        line = "";
        format = "";
    }

    public void stringToId(List<Knowledge> knowledges)
    {
        for (Knowledge knowledge : knowledges)
        {
            if (knowledge.getName().equals(object))
            {
                objectId = knowledge.getId();
                return;
            }
        }
    }
}
