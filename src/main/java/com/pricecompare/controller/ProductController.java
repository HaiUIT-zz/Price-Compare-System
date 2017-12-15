package com.pricecompare.controller;

import com.pricecompare.common.data.entities.CrawlingRequire;
import com.pricecompare.common.wrappergenerator.WrapperGenerator;
import com.pricecompare.entities.Agent;
import com.pricecompare.entities.Product;
import com.pricecompare.entities.ProductAgent;
import com.pricecompare.repositories.AgentRepository;
import com.pricecompare.repositories.ProductAgentRepository;
import com.pricecompare.repositories.ProductRepository;

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

@Controller
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductAgentRepository productAgentRepository;
    private final AgentRepository agentRepository;

    @Autowired
    public ProductController(ProductRepository productRepository, ProductAgentRepository productAgentRepository, AgentRepository agentRepository) {
        this.productAgentRepository = productAgentRepository;
        this.productRepository = productRepository;
        this.agentRepository = agentRepository;
    }

    @RequestMapping(value = {"/mobilephones"}, method = RequestMethod.GET)
    public String mobilephones(Model model) {
        List<ProductAgent> productAgents = this.productAgentRepository.findAll();
        for(ProductAgent productAgent : productAgents){
            System.out.println(productAgent.getAgent().getName());
            System.out.println(productAgent.getProduct().getName());
            System.out.println(productAgent.getUrl());
            System.out.println(productAgent.getPrice());
        }

        List<Product> products = this.productRepository.findAll();
        model.addAttribute("products", this.productRepository.findAll());
        return "user/products";
    }

    @RequestMapping(value = {"/mobilephones/{productId}"}, method = RequestMethod.GET)
    public String productDetail(Model model, @PathVariable("productId") int productId)
    {
        model.addAttribute("product", this.productRepository.findOne(productId));
       return "user/single";
    }
}
