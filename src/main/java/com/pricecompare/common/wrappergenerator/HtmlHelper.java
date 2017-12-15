package com.pricecompare.common.wrappergenerator;

import com.pricecompare.common.data.pojos.Wrapper;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlHelper
{
    private static HashMap<String, String> formattingTags;
    private static HashMap<String, String> ignoreTags;
    private static HashMap<String, String> removeTags;

    public static void setFormattingTags(List<String> formattingTags)
    {
        if (HtmlHelper.formattingTags == null)
        {
            HtmlHelper.formattingTags = new HashMap<>();
        }
        else
        {
            HtmlHelper.formattingTags.clear();
        }
        for (String tag: formattingTags)
        {
            HtmlHelper.formattingTags.put(tag, tag);
        }
    }

    public static void setIgnoreTags(List<String> ignoreTags)
    {
        if (HtmlHelper.ignoreTags == null)
        {
            HtmlHelper.ignoreTags = new HashMap<>();
        }
        else
        {
            HtmlHelper.ignoreTags.clear();
        }
        for(String tag: ignoreTags)
        {
            HtmlHelper.ignoreTags.put(tag, tag);
        }
    }

    public static void setRemoveTag(List<String> removeTags)
    {
        if (HtmlHelper.removeTags == null)
        {
            HtmlHelper.removeTags = new HashMap<>();
        }
        else
        {
            HtmlHelper.removeTags.clear();
        }
        for(String tag: removeTags)
        {
            HtmlHelper.removeTags.put(tag, tag);
        }
    }

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

    public static List<LogicalLine> generateLogicalLine(Elements elements, Wrapper wrapper,List<Knowledge> knowledges, String query)
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

                for (String key : wrapper.getPId().keySet())
                {
                    if(!wrapper.getPId().get(key).equals(""))
                    {
                        String pattern = wrapper.getPId().get(key);
                        Pattern regex;
                        Matcher matcher;
                        if (!pattern.equals("{query-only}"))
                        {
                            regex = Pattern.compile(pattern);
                            matcher = regex.matcher(line);
                            if (matcher.matches())
                            {
                                temp.setObject(key);
                                break;
                            }
                        }
                        else
                        {
                            if (StringUtils.containsIgnoreCase(temp.getLine(), query))
                            {
                                temp.setObject(key);
                                break;
                            }
                        }
                    }
                }
                temp.stringToId(knowledges);
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
            for (Element e: jsoupElements)
            {
                if (isRemove(e.tagName()))
                {
                    e.remove();
                }
            }
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

    private static boolean isRemove(String tag)
    {
        return removeTags.containsValue(tag);
    }
    //endregion
}
