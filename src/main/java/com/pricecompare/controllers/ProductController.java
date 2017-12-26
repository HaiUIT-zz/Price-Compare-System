package com.pricecompare.controllers;

import com.google.gson.Gson;
import com.pricecompare.common.data.pojos.ProductPOJO;
import com.pricecompare.entities.ManageVote;
import com.pricecompare.entities.Product;
import com.pricecompare.entities.ProductAgent;
import com.pricecompare.repositories.AgentRepository;
import com.pricecompare.repositories.ManageVoteRepository;
import com.pricecompare.repositories.ProductAgentRepository;
import com.pricecompare.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.pricecompare.common.data.pojos.AgentPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Controller
public class ProductController {
    Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductRepository productRepository;
    private final ProductAgentRepository productAgentRepository;
    private final AgentRepository agentRepository;

    @Autowired
    ManageVoteRepository manageVoteRepository;

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
        logger.debug(String.valueOf(this.productRepository.updateVisitCount(productId, product.getVisit_count() + 1)));
        int indexRelatedProduct = 0;
        for (Product product1 : this.productRepository.findAll()) {
            if (product1.getName().contains(product.getName().split("\\s")[0])) {
                relatedProducts.add(product1);
                indexRelatedProduct++;
            }
            if (indexRelatedProduct == 4) {
                break;
            }
        }
        productAgents.removeIf(element -> element.getProduct().getId() != productId);

        if (productAgents.size() != 0) {
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
        if (relatedProducts.size() != 0) {
            model.addAttribute("relatedProducts", relatedProducts);
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
        for (ProductAgent productAgent : productAgents) {
            ids.add(productAgent.getProduct().getId());
        }

        //set product detail to page
        model.addAttribute("products", this.productRepository.findAllByIdIn(ids));
        return "user/products";
    }

    @ResponseBody
    @RequestMapping(value = {"/allProducts"}, method = RequestMethod.GET)
    public String allProducts(HttpServletRequest request) {
        String url = request.getContextPath();
        List<ProductPOJO> productPOJOS = new ArrayList<>();
        for (Product product : this.productRepository.findAll()) {
            ProductPOJO productPOJO = new ProductPOJO();
            productPOJO.setId(product.getId());
            productPOJO.setName(product.getName());
            productPOJO.setUrlDetailProduct(url + "/mobilephones/" + product.getId());
            productPOJOS.add(productPOJO);
        }
        return new Gson().toJson(productPOJOS);
    }

    @ResponseBody
    @RequestMapping(value = {"/vote1"}, method = RequestMethod.POST)
    public void votes1(ServletRequest servletRequest) {
        int star = Integer.parseInt(servletRequest.getParameter("star"));
        int product_id = Integer.parseInt(servletRequest.getParameter("product_id"));
        String address = servletRequest.getRemoteAddr();
        if(this.manageVoteRepository.checkExistVoteOfIpForProduct(product_id, address)==null){
            this.manageVoteRepository.addVote(address, product_id);
        }
        else{

        }
    }


    @RequestMapping(value = {"/vote"}, method = RequestMethod.GET)
    public void votes(ServletRequest servletRequest) {
        int id = Integer.parseInt(servletRequest.getParameter("id"));
        int rating = Integer.parseInt(servletRequest.getParameter("rating"));
        Product product = this.productRepository.findOne(id);
        double total_rating = rating + product.getRating() * product.getRating_count();
        int total_rating_count = 1 + product.getRating_count();
        int rating_result = new Double(total_rating / total_rating_count).intValue();
        this.productRepository.updateRating(id, total_rating_count, rating_result);
    }


}
