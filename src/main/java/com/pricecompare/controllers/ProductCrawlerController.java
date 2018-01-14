package com.pricecompare.controllers;

import com.google.common.base.CharMatcher;
import com.google.gson.Gson;
import com.pricecompare.common.data.entities.*;
import com.pricecompare.common.data.pojos.CrawlerOption;
import com.pricecompare.common.data.pojos.Wrapper;
import com.pricecompare.common.data.reopsitories.*;
import com.pricecompare.common.data.pojos.ProductDTO;
import com.pricecompare.common.wrappergenerator.HtmlHelper;
import com.pricecompare.common.wrappergenerator.PhantomCrawler;
import com.pricecompare.common.wrappergenerator.WrapperGenerator;
import com.pricecompare.entities.Agent;
import com.pricecompare.entities.Product;
import com.pricecompare.entities.ProductAgent;
import com.pricecompare.repositories.AgentRepository;
import com.pricecompare.repositories.ProductAgentRepository;
import com.pricecompare.repositories.ProductRepository;
import com.pricecompare.utils.Utilities;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StrSubstitutor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    private final IgnoredWordRepository ignoredWordRepository;
    private final IgnoredTagRepository ignoredTagRepository;
    private final RemovedTagRepository removedTagRepository;
    private final FormatTagRepository formatTagRepository;
    private final ProductRepository productRepository;
    private final ProductSpecificRepository productSpecificRepository;
    private final ProductAgentRepository productAgentRepository;

    @Autowired
    public ProductCrawlerController(CrawlingRequireRepository crawlingRequireRepository,
                                    AgentRepository agentRepository, IgnoredWordRepository ignoredWordRepository,
                                    IgnoredTagRepository ignoredTagRepository, RemovedTagRepository removedTagRepository,
                                    FormatTagRepository formatTagRepository, ProductRepository productRepository,
                                    ProductSpecificRepository productSpecificRepository, ProductAgentRepository productAgentRepository)
    {
        this.crawlingRequireRepository = crawlingRequireRepository;
        this.agentRepository = agentRepository;
        this.ignoredWordRepository = ignoredWordRepository;
        this.ignoredTagRepository = ignoredTagRepository;
        this.removedTagRepository = removedTagRepository;
        this.formatTagRepository = formatTagRepository;
        this.productRepository = productRepository;
        this.productSpecificRepository = productSpecificRepository;
        this.productAgentRepository = productAgentRepository;
    }

    @RequestMapping(value = { "/crawler"}, method = RequestMethod.GET)
    public String index(Model model)
    {
        List<Agent> agents = agentRepository.findAllNotdeleted();
        if(agents != null)
        {
            model.addAttribute("agents", agents);
            model.addAttribute("selected_agent", 2);
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
            String xpath;
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
                HtmlHelper.setFormattingTags(formatTagRepository.findAllTag());
                HtmlHelper.setIgnoreTags(ignoredTagRepository.findAllTag());
                HtmlHelper.setRemoveTag(removedTagRepository.findAllTag());

                //select all elements
                Elements elements = document.select("body").select("*");

                Wrapper wrapper = new Wrapper(agent);
                WrapperGenerator wrapperGenerator = new WrapperGenerator(crawlingRequires);
                wrapperGenerator.generateLogicalLine(elements, crawOption.getQuery(), wrapper);
                wrapperGenerator.setUsedPattern(wrapper);
                List<ProductDTO> products = wrapperGenerator.generateProducts();
                prodcutNameProcess(products, crawOption);
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

    @RequestMapping(value = {"/crawler/save/{agent}"}, method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public String saveProducts(@RequestBody List<ProductDTO> productDTOS, @PathVariable(name = "agent") int agentId)
    {
        try
        {
            Agent agent = agentRepository.findOne(agentId);
            ProductDTO productDTO = productDTOS.get(0);
            {
                if (productDTO.getPossibleInDbId() == 0)
                {
                    saveNewProduct(productDTO, agent);
                }
                else
                {
                    updateOldProduct(productDTO, agent);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return new Gson().toJson("{success : true}");
    }

    private String searchUrlGenerator(Agent agent, String query)
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("query", query);
        return StrSubstitutor.replace(agent.getSearchUrl(), map);
    }
    
    private void prodcutNameProcess(List<ProductDTO> products, CrawlerOption crawlerOption)
    {
        String crawledName;
        String dbName;
        ProductSpecific productSpecific;
        List<IgnoredWord> ignoredWords = ignoredWordRepository.findAll();
        List<Product> productList = productRepository.findAllByBrand(crawlerOption.getQuery());
        if (productList == null || productList.size() == 0)
        {
            for ( ProductDTO product : products)
            {
                product.setPossibleInDb("New product");
                product.setPossibleInDbId(0);
            }
            return;
        }
        for (ProductDTO product: products)
        {
            for (Product realProduct : productList)
            {
                product.setChangedName(product.getRawName());
                dbName = realProduct.getName();
                for (IgnoredWord word: ignoredWords)
                {
                    product.setChangedName(product.getChangedName().replaceAll("(?i)" + word.getWord(), ""));
                    dbName = dbName.replaceAll("(?i)" + word.getWord(), "");
                }

                crawledName = product.getChangedName();

                if (crawlerOption.isIgnoreColor())
                {
                    productSpecific = productSpecificRepository.findOne(1);
                    for(SpecificDetail detail: productSpecific.getSpecificDetails())
                    {
                        crawledName = crawledName.replaceAll("(?i)" + detail.getPossibleText(), "");
                        dbName = dbName.replaceAll("(?i)" + detail.getPossibleText(), "");
                    }
                }

                if (crawlerOption.isIgnoreRam())
                {
                    productSpecific = productSpecificRepository.findOne(2);
                    for(SpecificDetail detail: productSpecific.getSpecificDetails())
                    {
                        crawledName = crawledName.replaceAll("(?i)" + detail.getPossibleText(), "");
                        dbName = dbName.replaceAll("(?i)" + detail.getPossibleText(), "");
                    }
                }

                if (StringUtils.equalsIgnoreCase(crawledName, dbName))
                {
                    product.setPossibleInDb(realProduct.getName());
                    product.setPossibleInDbId(realProduct.getId());
                }
            }

            if (product.getPossibleInDb() == null || product.getPossibleInDb().equals("") || product.getPossibleInDbId() == 0)
            {
                product.setPossibleInDb("New product");
            }
        }
    }

    private void saveNewProduct(ProductDTO productDTO, Agent agent)
    {
        Product product = new Product();
        product.setName(productDTO.getRawName());
        product.setAgent_count(1);
        product.setRating(0.0D);
        product.setRating_count(0);
        product.setVisit_count(0);
        productRepository.save(product);

        ProductAgent productAgent = new ProductAgent();
        productAgent.setAgent(agent);
        productAgent.setProduct(product);
        productAgent.setPrice(new BigDecimal(CharMatcher.digit().retainFrom(productDTO.getPrice())));
        productAgent.setUrl(productUrlmaker(agent, productDTO.getRawName()));
        productAgentRepository.save(productAgent);
    }

    private void updateOldProduct(ProductDTO productDTO, Agent agent)
    {
        List<ProductAgent> productAgents = productAgentRepository.getByProductAndAgent(productDTO.getPossibleInDbId(), agent.getId());
        if (productAgents != null && productAgents.size() > 0)
        {
            ProductAgent productAgent = productAgents.get(0);
            productAgent.setPrice(new BigDecimal(CharMatcher.digit().retainFrom(productDTO.getPrice())));
            productAgentRepository.save(productAgent);
        }
        else
        {
            Product product = productRepository.findOne(productDTO.getPossibleInDbId());
            product.setAgent_count(product.getAgent_count() + 1);
            productRepository.save(product);
            ProductAgent productAgent = new ProductAgent();
            productAgent.setAgent(agent);
            productAgent.setProduct(product);
            productAgent.setPrice(new BigDecimal(CharMatcher.digit().retainFrom(productDTO.getPrice())));
            productAgent.setUrl(productUrlmaker(agent, productDTO.getRawName()));
            productAgentRepository.save(productAgent);
        }
    }

    private  String productUrlmaker(Agent agent, String productName)
    {
        String url;
        productName = StringUtils.normalizeSpace(productName);
        productName = StringUtils.replaceAll(productName, " ", "+");
        HashMap<String, String> map = new HashMap<>();
        map.put("query", productName);
        url = StrSubstitutor.replace(agent.getSearchUrl(), map);
        return  url;
    }
}
