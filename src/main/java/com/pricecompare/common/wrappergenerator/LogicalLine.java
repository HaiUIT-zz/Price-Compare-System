package com.pricecompare.common.wrappergenerator;

/*
* LogicalLine type value :
* 0 = Unknown
* 1 = Price
* 2 = Product name
 */
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

    public int getObjectId()
    {
        return objectId;
    }

    public void setObjectId(int objectId)
    {
        this.objectId = objectId;
    }

    public String getObject()
    {
        return object;
    }

    public void setObject(String object)
    {
        this.object = object;
    }

    public String getLine()
    {
        return line;
    }

    public void setLine(String line)
    {
        this.line = line;
    }

    public String getFormat()
    {
        return format;
    }

    public void setFormat(String format)
    {
        this.format = format;
    }

    public void stringToId()
    {
        switch (object)
        {
            case "Price" :
                objectId = 1;
                break;
            case "Name" :
                objectId = 2;
                break;
            default:
                objectId = 0;
                break;
        }
    }
}
