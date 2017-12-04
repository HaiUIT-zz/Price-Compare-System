package com.pricecompare.common.wrappergenerator;

import com.pricecompare.common.data.entities.CrawlingRequire;
import com.pricecompare.common.data.entities.RequireFormat;
import com.pricecompare.common.data.entities.RequireTerm;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StrSubstitutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Knowledge
{
    private String name;
    private List<String> terms;
    private List<String> formats;

    public Knowledge(String name)
    {
        this.name = name;
    }

    //region Getter and Setter
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<String> getTerms()
    {
        return terms;
    }

    public List<String> getFormats()
    {
        return formats;
    }
    //endregion

    //region Mtehods
    public void generateDetail(CrawlingRequire require)
    {
        try
        {
            terms = new ArrayList<>();
            formats = new ArrayList<>();
            for (RequireTerm term : require.getRequireTerms())
            {
                terms.add(term.getTerm());
            }

            for (RequireFormat format : require.getRequireFormats())
            {
                for(String term : terms)
                {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("term", term);
                    formats.add(StrSubstitutor.replace(format.getFormat(), map));
                }
            }
        }
        catch (Exception e)
        {
            System.out.print(e.getMessage());
        }
    }

    public String getFormatOfLine(String line, String query)
    {
        String fomat = "";
        String term = "";
        for (String t : terms)
        {
            if (line.contains(t))
            {
                term = t;
                break;
            }
        }

        if (!term.isEmpty())
        {
            for (String f : formats)
            {
                if(Pattern.matches(f, line))
                {
                    fomat = f;
                    break;
                }

            }
        }else
        {
            if (name.equals("Name"))
            {
                if(StringUtils.containsIgnoreCase(line, query))
                {
                    fomat = "{query-only}";
                }
            }
        }
        return fomat;
    }
    //endregion

    //region Private Helper

    //endregion
}
