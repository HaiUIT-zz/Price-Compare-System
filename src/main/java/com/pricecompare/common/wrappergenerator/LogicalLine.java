package com.pricecompare.common.wrappergenerator;

/*
* LogicalLine type value :
* 0 = Unknown
* 1 = Price
* 2 = Product name
 */

import com.pricecompare.common.data.entities.CrawlingRequire;
import com.pricecompare.common.data.reopsitories.CrawlingRequireRepository;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;

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

    public void stringToId(List<CrawlingRequire> crawlingRequireList)
    {
        for (CrawlingRequire crawlingRequire : crawlingRequireList)
        {
            if (crawlingRequire.getText().equals(object))
            {
                objectId = crawlingRequire.getId();
                return;
            }
        }
    }
}
