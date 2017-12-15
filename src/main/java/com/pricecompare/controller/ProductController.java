package com.pricecompare.controller;

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

import java.util.ArrayList;
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

        model.addAttribute("products", this.productRepository.findAll());

        return "user/products";
    }

    @RequestMapping(value = {"/mobilephones/{productId}"}, method = RequestMethod.GET)
    public String productDetail(Model model, @PathVariable("productId") int productId) {
        //filter product have id
        List<ProductAgent> productAgents = this.productAgentRepository.findAll();
        productAgents.removeIf(element -> element.getProduct().getId() != 3);

        //minimize product agent
        List<AgentPrice> agentPrices = new ArrayList<>();
        for(ProductAgent productAgent : productAgents){
            AgentPrice agentPrice = new AgentPrice();
            agentPrice.setAgent(productAgent.getAgent().getName());
            agentPrice.setPrice(productAgent.getPrice());
            agentPrices.add(agentPrice);
        }
        //set product detail to page
        model.addAttribute("product", this.productRepository.findOne(productId));
        model.addAttribute("productAgents", productAgents);
        model.addAttribute("agentPrices", agentPrices);

        return "user/single";
    }
}
