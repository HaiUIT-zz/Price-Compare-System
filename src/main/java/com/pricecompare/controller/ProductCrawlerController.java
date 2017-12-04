package com.pricecompare.controller;

import com.pricecompare.common.data.entities.CrawlingRequire;
import com.pricecompare.common.data.entities.RequireFormat;
import com.pricecompare.common.data.reopsitories.CrawlingRequireRepository;
import com.pricecompare.entities.Product;
import com.pricecompare.common.wrappergenerator.WrapperGenerator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/product")
public class ProductCrawlerController
{
    private final CrawlingRequireRepository crawlingRequireRepository;

    @Autowired
    public ProductCrawlerController(CrawlingRequireRepository crawlingRequireRepository)
    {
        this.crawlingRequireRepository = crawlingRequireRepository;
    }

    @RequestMapping(value = { "/crawler"}, method = RequestMethod.GET)
    public String index()
    {
        return "layout_admin :: mainPage(page='product-crawler', content='content', script='script')";
    }

    @RequestMapping(value = {"/crawler/{agent}/{query}"}, method = RequestMethod.GET)
    public String crawlProduct(Model model, @PathVariable("query") String query, @PathVariable("agent") String agent)
    {
        try
        {
            List<CrawlingRequire> crawlingRequires = crawlingRequireRepository.findAll();
            String path = "static/xml/sample/" + agent + "-" + query + ".html";
            Resource resource = new ClassPathResource(path);
            File sample = resource.getFile();
            //parse HTML
            Document document = Jsoup.parse(sample, "utf8");

            //select all elements
            Elements elements = document.select("body").select("*");

            WrapperGenerator wrapperGenerator = new WrapperGenerator(crawlingRequires);
            wrapperGenerator.generateLogicalLine(elements, query);
            List<Product> products = wrapperGenerator.generateWrapper();
            model.addAttribute("products", products);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return "layout_admin :: mainPage(page='product-crawler', content='content', script='script')";
    }
}
