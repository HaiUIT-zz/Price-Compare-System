package com.pricecompare.controller;

import com.pricecompare.common.data.entities.AgentLoadMore;
import com.pricecompare.common.data.entities.CrawlingRequire;
import com.pricecompare.common.data.pojos.CrawlerOption;
import com.pricecompare.common.data.reopsitories.CrawlingRequireRepository;
import com.pricecompare.common.data.pojos.Product;
import com.pricecompare.common.wrappergenerator.PhantomCrawler;
import com.pricecompare.common.wrappergenerator.WrapperGenerator;
import com.pricecompare.entities.Agent;
import com.pricecompare.repositories.AgentRepository;
import com.pricecompare.utils.Utilities;
import org.apache.commons.text.StrSubstitutor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin/product")
public class ProductCrawlerController
{
    private static final int NO_LOAD_MORE_METHOD = 1;
    private final CrawlingRequireRepository crawlingRequireRepository;
    private final AgentRepository agentRepository;

    @Autowired
    public ProductCrawlerController(CrawlingRequireRepository crawlingRequireRepository, AgentRepository agentRepository)
    {
        this.crawlingRequireRepository = crawlingRequireRepository;
        this.agentRepository = agentRepository;
    }

    @RequestMapping(value = { "/crawler"}, method = RequestMethod.GET)
    public String index(Model model)
    {
        List<Agent> agents = agentRepository.findAll();
        if(agents != null)
        {
            model.addAttribute("agents", agents);
            model.addAttribute("selected_agent", 1);
        }
        Utilities.setPageContent(model, "product-crawler", "content", "script");
        return "layout_admin";
    }

    @RequestMapping(value = {"/crawl"}, method = RequestMethod.POST)
    public String crawlProduct(Model model, CrawlerOption crawOption)
    {
        PhantomCrawler crawler = null;
        try
        {
            List<CrawlingRequire> crawlingRequires = crawlingRequireRepository.findAll();
            Agent agent = agentRepository.findOne(crawOption.getAgent());
            String url = searchUrlGenerator(agent, crawOption.getQuery());
            if (crawOption.getSleep() > 0)
            {
                crawler = new PhantomCrawler(crawOption.getSleep(), agent);
            }
            else
            {
                crawler = new PhantomCrawler(agent);
            }

            String loadMoreMethod;
            String xpath = "";
            String html = "";

            Set<AgentLoadMore> agentLoadMores = agent.getAgentLoadMores();

            if (agentLoadMores.size() > 0)
            {
                Iterator<AgentLoadMore> i = agentLoadMores.iterator();
                AgentLoadMore agentLoadMore = i.next();
                loadMoreMethod = agentLoadMore.getMethod();
                xpath = agentLoadMore.getXpath();

                switch (loadMoreMethod)
                {
                    case "ajax":
                        html = crawler.getSearchResultAjax(url, xpath);
                        break;
                    case "url":
                        html = crawler.getSearchResultUrl(url);
                        break;
                }

                //parse HTML
                Document document = Jsoup.parse(html);
                document.select("del").remove();

                //select all elements
                Elements elements = document.select("body").select("*");

                WrapperGenerator wrapperGenerator = new WrapperGenerator(crawlingRequires);
                wrapperGenerator.generateLogicalLine(elements, crawOption.getQuery());
                wrapperGenerator.findMostFreqPattern();
                List<Product> products = wrapperGenerator.generateProductsFromPattern();
                model.addAttribute("products", products);
            }
            else
            {
                model.addAttribute("error_code", NO_LOAD_MORE_METHOD);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        finally
        {
            if (crawler != null)
            {
                crawler.quit();
            }
        }
        List<Agent> agents = agentRepository.findAll();
        if(agents != null)
        {
            model.addAttribute("agents", agents);
            model.addAttribute("selected_agent", crawOption.getAgent());
        }
        Utilities.setPageContent(model, "product-crawler", "content", "script");
        return "layout_admin";
    }

    private String searchUrlGenerator(Agent agent, String query)
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("query", query);
        return StrSubstitutor.replace(agent.getSearchUrl(), map);
    }
}
