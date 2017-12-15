package com.pricecompare.common.wrappergenerator;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provide methods that recieved html
 * and return a formated list of String that remove
 * unneccessary html element
 * Currently support html doc in jsoup's format
 */
public class HtmlHelper
{
    private  static HashMap<String, String> formattingTags = XmlHelper.getFormattingTags("static/xml/HtmlFormattingTags.xml");
    private  static HashMap<String, String> ignoreTags = XmlHelper.getFormattingTags("static/xml/HtmlIgnoreTags.xml");

    public static List<LogicalLine> generateLogicalLine(Elements elements, List<Knowledge> knowledges, String query)
    {
        List<Element> extractedElement = getLogicalElement(elements);
        List<LogicalLine> logicalLines = new ArrayList<>();
        for(Element e: extractedElement)
        {
            String line = StringUtils.normalizeSpace(e.text());
            LogicalLine temp = new LogicalLine();
            temp.setLine(line);

            String format = "";
            for (Knowledge k : knowledges)
            {
                format = k.getFormatOfLine(temp.getLine(), query);
                if (!format.isEmpty())
                {
                    temp.setObject(k.getName());
                    break;
                }
            }

            temp.setFormat(format);
            temp.stringToId(knowledges);

            if (!(temp.getObjectId() == 0 && logicalLines.size() == 0))
            {
                logicalLines.add(temp);
                System.out.println(temp.getLine());
            }
        }
        return logicalLines;
    }

    public static List<LogicalLine> generateLogicalLine(Elements elements, Wrapper wrapper, String query)
    {
        List<Element> extractedElement = getLogicalElement(elements);
        List<LogicalLine> logicalLines = new ArrayList<>();
        for(Element e: extractedElement)
        {
            String line = StringUtils.normalizeSpace(e.text());
            if (!line.equals(""))
            {
                LogicalLine temp = new LogicalLine();
                temp.setLine(line);

                for (LogicalLine pattern : wrapper.getPattern())
                {
                    if(!pattern.getFormat().equals(""))
                    {
                        Pattern regex = Pattern.compile(pattern.getFormat());
                        Matcher matcher = regex.matcher(line);
                        if (matcher.find())
                        {
                            temp.setObject(pattern.getObject());
                            break;
                        }
                    }
                }
                //temp.stringToId();
                logicalLines.add(temp);
            }
        }
        return logicalLines;
    }

    //region Private helper
    private static List<Element> getLogicalElement(Elements jsoupElements)
    {
        List<Element> hasNoChild = new ArrayList<>();
        try
        {
            for(Element e : jsoupElements)
            {
                if (e.parent() != null && isFormating(e.tagName()))
                {
                    e.unwrap();
                }
            }

            for(Element e : jsoupElements)
            {
                if (!isIgnore(e.tagName()) && !e.text().equals("") && (!hasChid(e) || !e.ownText().isEmpty()))
                {
                    hasNoChild.add(e);
                }
            }
        }
        catch (Exception e)
        {
            System.out.print(e.getMessage());
        }
        return hasNoChild;
    }

    private static boolean hasChid(Element e)
    {
        return e.children().size() > 0;
    }

    private static boolean isFormating(String tag)
    {
        return formattingTags.containsValue(tag);
    }

    private static  boolean isIgnore(String tag)
    {
        return  ignoreTags.containsValue(tag);
    }
    //endregion
}
