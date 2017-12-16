package com.pricecompare.controller;

import com.pricecompare.entities.Product;
import com.pricecompare.entities.ProductAgent;
import com.pricecompare.repositories.AgentRepository;
import com.pricecompare.repositories.ProductAgentRepository;
import com.pricecompare.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.pricecompare.common.data.pojos.AgentPrice;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.*;

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

        model.addAttribute("products", this.productRepository.findAll());

        return "user/products";
    }

    @RequestMapping(value = {"/mobilephones/{productId}"}, method = RequestMethod.GET)
    public String productDetail(Model model, @PathVariable("productId") int productId) {
        //filter product have id
        List<ProductAgent> productAgents = this.productAgentRepository.findAll();

        List<Product> relatedProducts = new ArrayList<>();
        Product product = this.productRepository.findOne(productId);
        int indexRelatedProduct = 0;
        for(Product product1 : this.productRepository.findAll()){
            if(product1.getName().contains(product.getName().split("\\s")[0])){
                relatedProducts.add(product1);
                indexRelatedProduct++;
            }
            if(indexRelatedProduct==4){
                break;
            }
        }
        productAgents.removeIf(element -> element.getProduct().getId() != productId);

        if(productAgents.size()!=0){
            //minimize product agent
            List<AgentPrice> agentPrices = new ArrayList<>();
            for (ProductAgent productAgent : productAgents) {
                AgentPrice agentPrice = new AgentPrice();
                agentPrice.setAgent(productAgent.getAgent().getName());
                agentPrice.setPrice(productAgent.getPrice());
                agentPrices.add(agentPrice);
            }
            model.addAttribute("productAgents", productAgents);
            model.addAttribute("agentPrices", agentPrices);
        }

        //add related products list
        if(relatedProducts.size()!=0){
            model.addAttribute("relatedProducts",relatedProducts);
        }

        //set product detail to page
        model.addAttribute("product", this.productRepository.findOne(productId));
        return "user/single";
    }

    @RequestMapping(value = {"/mobilephonesf"}, method = RequestMethod.GET)
    public String mobilephonesFilterByPrice(Model model, @RequestParam("from") int from, @RequestParam("to") int to) {
        System.out.println(from);
        System.out.println(to);

        //filter product have id
        List<ProductAgent> productAgents = this.productAgentRepository.findAll();
        productAgents.removeIf(item -> !(item.getPrice().compareTo(BigDecimal.valueOf(from)) >= 0 &&
               item.getPrice().compareTo(BigDecimal.valueOf(to)) <= 0));

        Set<Integer> ids = new HashSet<>();
        for(ProductAgent productAgent : productAgents){
            ids.add(productAgent.getProduct().getId());
        }

        //set product detail to page
        model.addAttribute("products", this.productRepository.findAllByIdIn(ids));
        return "user/products";
    }
}
