package com.pricecompare.common.wrappergenerator;

import com.pricecompare.common.data.entities.PlaceHolder;
import com.pricecompare.common.data.pojos.InputStyle;
import com.pricecompare.common.utils.JSWaiter;
import com.pricecompare.entities.Agent;
import org.apache.commons.text.StrSubstitutor;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import java.util.HashMap;
import java.util.List;
public class PhantomCrawler
{
    private PhantomJSDriver driver;
    private JSWaiter waiter;
    private long sleepTime = 0;
    private Agent agent;
    private static final String JUNK_QUERY = "nevershowup";
    private static final String XPATH_INPUT_STYLE = "//input[${attr}='${value}']";
    private static final String XPATH_PLACEHOLDER = "//input[contains(@placeholder='${value}']";

    public PhantomCrawler(long sleepTime, Agent agent)
    {
        driver = new PhantomJSDriver();
        waiter = new JSWaiter();
        driver.manage().window().maximize();
        waiter.setDriver(driver);
        this.sleepTime = sleepTime;
        this.agent = agent;
    }

    public PhantomCrawler(Agent agent)
    {
        driver = new PhantomJSDriver();
        waiter = new JSWaiter();
        driver.manage().window().maximize();
        waiter.setDriver(driver);
        this.agent = agent;
    }

    public String getSearchResultAjax(String searchUrl, String xpath) throws InterruptedException
    {
        WebElement ele;
        driver.get(searchUrl);
        waitPageLoad();
        ele = findBy(By.xpath(xpath));
        while (ele != null && ele.isDisplayed())
        {
            ele.click();
            waitPageLoad();
            ele = findBy(By.xpath(xpath));
        }
        return driver.getPageSource();
    }

    public String getSearchResultUrl(String searchUrl) throws InterruptedException
    {
        driver.get(searchUrl);
        waitPageLoad();
        return driver.getPageSource();
    }

    public String getSearchUrl(String homePage, List<PlaceHolder> placeHolders, List<InputStyle> possibleInputs) throws InterruptedException
    {
        driver.get(homePage);
        waitPageLoad();
        String htmlx = driver.getPageSource();
        WebElement ele = null;

        for (PlaceHolder placeHolder: placeHolders)
        {
            ele = findBy(By.xpath(xPathPlaceHolder(placeHolder)));
            if (ele != null)
            {
                break;
            }
        }
        if (ele == null)
        {
            for (InputStyle inputStyle : possibleInputs)
            {
                ele = findBy(By.xpath(xPathInputStyle(inputStyle)));
                if (ele != null)
                {
                    break;
                }
            }
        }

        if (ele == null)
        {
            return null;
        }

        ele.sendKeys(JUNK_QUERY);
        ele.sendKeys(Keys.LEFT_CONTROL);
        waitPageLoad();
        ele.sendKeys(Keys.ENTER);
        waitPageLoad();
        String html = driver.getPageSource();
        if (html.contains(JUNK_QUERY))
        {
            return driver.getCurrentUrl();
        }
        return null;
    }

    public void quit()
    {
        driver.quit();
    }

    private WebElement findBy(By by)
    {
        try
        {
            return driver.findElement(by);
        }
        catch (NoSuchElementException e)
        {
            return null;
        }
    }

    private String xPathInputStyle(InputStyle inputStyle)
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("attr", inputStyle.getAttribute());
        map.put("value", inputStyle.getValue());
        return StrSubstitutor.replace(XPATH_INPUT_STYLE, map);
    }

    private String xPathPlaceHolder(PlaceHolder placeHolder)
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("value", placeHolder.getValue());
        return StrSubstitutor.replace(XPATH_PLACEHOLDER, map);
    }

    private void waitPageLoad() throws InterruptedException
    {
        if (sleepTime > 0)
        {
            Thread.sleep(sleepTime * 1000);
        }
        else
        {
            waiter.waitJsJQueryAngular();
        }
    }
}
