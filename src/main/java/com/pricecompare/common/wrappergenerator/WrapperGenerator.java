package com.pricecompare.common.wrappergenerator;
import com.pricecompare.common.data.entities.CrawlingRequire;
import com.pricecompare.common.data.pojos.ProductCrawled;
import com.pricecompare.common.data.pojos.Wrapper;
import lombok.Getter;
import org.jsoup.select.Elements;

import java.util.*;

@Getter
public class WrapperGenerator
{
    private List<LogicalLine> logicalLines;
    private List<Knowledge> knowledges;
    private Pattern usedPattern;

    public WrapperGenerator(List<CrawlingRequire> crawlingRequires)
    {
        knowledges = new ArrayList<>();
        logicalLines = new ArrayList<>();
        usedPattern = new Pattern();
        try
        {
            for (CrawlingRequire require: crawlingRequires)
            {
                knowledges.add(new Knowledge(require.getText(), require.getId()));
                knowledges.get(knowledges.size() - 1).generateDetail(require);
            }
        }
        catch (Exception e)
        {
            System.out.print(e.getMessage());
        }
    }

    public void findMostFreqPattern()
    {
        List<Pattern> patterns = generatePatterns();

        int maxFreq = 0;
        Pattern mostFreqPattern = patterns.get(0);

        Iterator<Pattern> iterator = patterns.iterator();

        while (iterator.hasNext())
        {
            Pattern p = iterator.next();
            int patternFreq = patternFreq(p, patterns);
            if (patternFreq > maxFreq)
            {
                maxFreq = patternFreq;
                mostFreqPattern = p;
            }

            if (patternFreq > 0)
            {
                iterator = patterns.iterator();
            }
        }
        usedPattern = mostFreqPattern;
    }

    public void setUsedPattern(Wrapper wrapper)
    {
        usedPattern.setPattern(wrapper.getPattern());
    }

    public List<ProductCrawled> generateProducts()
    {
        List<ProductCrawled> products = new ArrayList<>();

        for (int i = 0; i < logicalLines.size() - usedPattern.getPattern().size() + 1; i++)
        {
            if (logicalLines.get(i).getObjectId() == usedPattern.getPattern().get(0))
            {
                int counter = 1;
                while (counter < usedPattern.getPattern().size() && logicalLines.get(i + counter).getObjectId() == usedPattern.getPattern().get(counter))
                {
                    counter++;
                }
                if (counter == usedPattern.getPattern().size())
                {
                    ProductCrawled p = new ProductCrawled();
                    for (int k = i; k < i + counter; k++)
                    {
                        switch (usedPattern.getPattern().get(k - i))
                        {
                            case 1:
                                p.setPrice(logicalLines.get(k).getLine());
                                break;
                            case 2:
                                p.setRawName(logicalLines.get(k).getLine());
                                break;
                        }
                    }
                    boolean containt = false;
                    for (ProductCrawled product: products)
                    {
                        if (p.equals(product))
                        {
                            containt = true;
                            break;
                        }
                        else
                        {
                            containt = false;
                        }
                    }
                    if(!containt)
                    {
                        products.add(p);
                    }
                    i += counter - 1;
                }
            }
        }
        return products;
    }

    public void generateLogicalLine(Elements elements, String query)
    {
        logicalLines = HtmlHelper.generateLogicalLine(elements, knowledges, query);
    }

    public void  generateLogicalLine(Elements elements, String query, Wrapper wrapper)
    {
        logicalLines = HtmlHelper.generateLogicalLine(elements, wrapper, knowledges, query);
    }


    //region Private Helper
    private List<String> getRequire(List<CrawlingRequire> crawlingRequires)
    {
        List<String> requires = new ArrayList<>();
        for (CrawlingRequire require : crawlingRequires)
        {
            requires.add(require.getText());
        }
        return requires;
    }

    private List<Pattern> generatePatterns()
    {
        List<Integer> logicalLineIds = generateLogicalIds();
        List<Pattern> patterns = new ArrayList<>();
        int nextIndex = 0;
        while (nextIndex < logicalLineIds.size())
        {
            Pattern p = patternFinder(nextIndex, logicalLineIds);
            nextIndex = p.getEndIndex();
            if(p.getPattern().size() > 1 && validatePattern(p))
            {
                patterns.add(p);
            }
        }
        return patterns;
    }

    private List<Integer> generateLogicalIds()
    {
        List<Integer> logicalIds = new ArrayList<>();
        for (LogicalLine line : logicalLines)
        {
            logicalIds.add(line.getObjectId());
        }
        return  logicalIds;
    }

    private  Pattern patternFinder(int start, List<Integer> logicalLineIds)
    {
        Pattern pattern = new Pattern();
        int newIndex = logicalLineIds.size();
        for (int i = start; i < logicalLineIds.size(); i++)
        {
            int id = logicalLineIds.get(i);
            if(id == 0)
            {
                pattern.getPattern().add(id);
            }
            else if (!pattern.getPattern().contains(id))
            {
                pattern.getPattern().add(id);
            }else
            {
                newIndex = i;
                break;
            }
        }
        pattern.setEndIndex(newIndex);
        return  pattern;
    }

    private int patternFreq(Pattern pattern, List<Pattern> patterns)
    {
        int freq = 0;
        for (Iterator<Pattern> iterator = patterns.iterator(); iterator.hasNext();)
        {
            Pattern p = iterator.next();
            if (p.equals(pattern))
            {
                freq++;
                iterator.remove();
            }
        }
        return freq;
    }

    private boolean validatePattern(Pattern pattern)
    {
        int counter = 0;
        Set<Integer> uniqueId = new HashSet<>(pattern.getPattern());
        for (int id : uniqueId)
        {
            if (id == 0)
            {
                continue;
            }
            for (Knowledge k : knowledges)
            {
                if (id == k.getId())
                {
                    counter++;
                    break;
                }
            }
        }
        return counter == knowledges.size();
    }
    //endregion
}
