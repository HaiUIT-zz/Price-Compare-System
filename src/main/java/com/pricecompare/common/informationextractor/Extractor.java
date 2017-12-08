package com.pricecompare.common.informationextractor;

import com.pricecompare.common.wrappergenerator.HtmlHelper;
import com.pricecompare.common.wrappergenerator.LogicalLine;
import com.pricecompare.common.wrappergenerator.Wrapper;
import com.pricecompare.common.wrappergenerator.XmlHelper;
import org.jsoup.select.Elements;
import java.util.List;

public class Extractor
{
    private Wrapper wrapper;

    public void setWrapper(String target)
    {
        wrapper = XmlHelper.getWrapper(target);
    }

    public void extract(Elements elements, String query)
    {
        setWrapper("wrapper/sampleWrapper.xml");
        List<LogicalLine> logicalLines = HtmlHelper.generateLogicalLine(elements, wrapper, query);
    }

    private int findObject(List<LogicalLine> logicalLines)
    {
        int count = 0;
        boolean match = false;
        int i = 0;
        while (i < logicalLines.size())
        {
            if(logicalLines.get(i).getObjectId() == wrapper.getPattern().get(0).getObjectId())
            {
                for (int j = i + 1; j < i + wrapper.getPattern().size(); j++)
                {
                    if(logicalLines.get(j).getObjectId() != wrapper.getPattern().get(j - i).getObjectId())
                    {
                        match = false;
                        break;
                    }
                    else
                    {
                        match = true;
                    }
                }
                if (match)
                {
                    count++;
                    i += wrapper.getPattern().size();
                    match = false;
                }
                else
                {
                    i++;
                }
            }
            else
            {
                i++;
            }
        }
        return count;
    }
}
