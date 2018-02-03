package com.pricecompare.controllers;

import com.google.gson.Gson;
import com.pricecompare.common.data.pojos.ProductPOJO;
import com.pricecompare.entities.Product;
import com.pricecompare.entities.ProductAgent;
import com.pricecompare.entities.Voting;
import com.pricecompare.repositories.AgentRepository;
import com.pricecompare.repositories.VotingRepository;
import com.pricecompare.repositories.ProductAgentRepository;
import com.pricecompare.repositories.ProductRepository;
import jdk.nashorn.internal.runtime.regexp.joni.constants.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.pricecompare.common.data.pojos.AgentPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.SQLData;
import java.util.*;

@Controller
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductAgentRepository productAgentRepository;
    private final AgentRepository agentRepository;
    Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    VotingRepository manageVoteRepository;
    @Autowired
    VotingRepository votingRepository;
    @Autowired
    private EntityManager em;

    @Autowired
    public ProductController(ProductRepository productRepository, ProductAgentRepository productAgentRepository, AgentRepository agentRepository) {
        this.productAgentRepository = productAgentRepository;
        this.productRepository = productRepository;
        this.agentRepository = agentRepository;
    }

    @RequestMapping(value = {"/mobilephones/{page}"}, method = RequestMethod.GET)
    public String mobilephones(Model model, @PathVariable("page") int page) {
        List<Product> products = this.productRepository.findAll();
        int size = products.size();
        final int productPerPage = 6;
        int numberOfPage1 = products.size() / productPerPage;
        if (numberOfPage1 * productPerPage < products.size()) {
            numberOfPage1++;
        }
        int start = productPerPage * (page - 1);
        int end = start + productPerPage;
        if (end > size) {
            end = size;
        }
        products = products.subList(start, end);
        model.addAttribute("currentPage", page);
        model.addAttribute("products", products);
        model.addAttribute("numberOfPage", numberOfPage1);
        List<Product> _products = this.productRepository.findAll();
        List<Product> productsTopRating = new ArrayList<>();
        List<Product> productsTopRatingTemp = _products;
        //process filter products have rating in top 3
        productsTopRatingTemp.sort((Product _product, Product product2) -> {
            if (_product.getRating() > product2.getRating())
                return -1;
            if (_product.getRating() < product2.getRating())
                return 1;
            return 0;
        });
        int indexLoopTopProduct = 0;
        for (Product product1 : productsTopRatingTemp) {
            if (indexLoopTopProduct == 5) {
                break;
            }
            productsTopRating.add(product1);
            indexLoopTopProduct++;
        }
        model.addAttribute("productsTopRating", productsTopRating);
        return "user/products";
    }

    @RequestMapping(value = {"/mobilephones/get/{productId}"}, method = RequestMethod.GET)
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
            System.out.println(new Gson().toJson(agentPrices));
            model.addAttribute("agentPrices", new Gson().toJson(agentPrices));
        }
        //add related products list
        if (relatedProducts.size() != 0) {
            model.addAttribute("relatedProducts", relatedProducts);
        }

        //set product detail to page
        model.addAttribute("product", this.productRepository.findOne(productId));

        //process voting of product
        List<Voting> votings = this.votingRepository.findAll();
        List<Voting> _votings = new ArrayList<>();
        for (Voting voting : votings) {
            if (voting.getProduct().getId() == productId) {
                _votings.add(voting);
            }
        }
        model.addAttribute("_votings", _votings);

        //process count agents of product
        int agentCount = 0;
        for (Product product2 : this.productRepository.findAll()) {
            for (ProductAgent productAgent : productAgentRepository.findAll()) {
                if (product2.getId() == productAgent.getProduct().getId()) {
                    agentCount++;
                }
            }
            this.productRepository.updateAgentCount(agentCount, product2.getId());
            agentCount = 0;
        }

        //update rating of products
        int index = 0;
        int total = 0;
        double rating = 0;
        for (Product product3 : this.productRepository.findAll()) {
            for (Voting voting : this.votingRepository.findAll()) {
                if (product3.getId() == voting.getProduct().getId()) {
                    index++;
                    total += voting.getRating();
                }
            }
            rating = ((double) total / index);
            if (((rating - 0.5)) > (int) rating) {
                rating = (int) rating + 1;
            } else {
                rating = (int) rating;
            }
            this.productRepository.updateRating(product3.getId(), index, (int) rating);
            index = 0;
            total = 0;
            rating = 0;
        }

        return "user/single";
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
            productPOJO.setUrlDetailProduct(url + "/mobilephones/get/" + product.getId());
            productPOJOS.add(productPOJO);
        }
        return new Gson().toJson(productPOJOS);
    }

    @ResponseBody
    @RequestMapping(value = {"/processRating"}, method = RequestMethod.POST)
    public void processRating(ServletRequest servletRequest) {
        int star = Integer.parseInt(servletRequest.getParameter("star"));
        System.out.println("star");
        System.out.println(star);
        String email = servletRequest.getParameter("email");

        System.out.println("email");
        System.out.println(email);
        int product_id = Integer.parseInt(servletRequest.getParameter("product_id"));
        System.out.println("product_id");
        System.out.println(product_id);
        System.out.println(votingRepository.checkExistVoteOfIpForProduct(product_id, email));
        if (votingRepository.checkExistVoteOfIpForProduct(product_id, email) == 1) {
            this.votingRepository.updateVote(product_id, email, star);
            this.productRepository.updateRating(product_id);
        } else {
            this.votingRepository.addVote(email, product_id, star);
            this.productRepository.updateRating(product_id);
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

    @RequestMapping(value = {"/mobilephones/find"}, method = RequestMethod.POST)
    public String findProduct(Model model,
//                              ServletRequest servletRequest,
                              @RequestParam(value = "page", required = false) Integer page,
                              @RequestParam(value = "Search", required = false) String search) {
//        String keyword = servletRequest.getParameter("Search");
        String keyword = search;
        System.out.println("=====================================================Keyword:" + keyword);
        if (keyword == null && keyword.isEmpty()) {
            return "redirect:/mobilephones";
        } else {
            List<Product> products = this.productRepository.findProduct("%" + keyword + "%");

            //process paging
            int size = products.size();
            final int productPerPage = 6;
            int numberOfPage1 = products.size() / productPerPage;
            if (numberOfPage1 * productPerPage < products.size()) {
                numberOfPage1++;
            }
            int start = productPerPage * (page - 1);
            int end = start + productPerPage;
            if (end > size) {
                end = size;
            }
            products = products.subList(start, end);
            model.addAttribute("currentPage", page);
            model.addAttribute("products", products);
            model.addAttribute("numberOfPage", numberOfPage1);
            //end of process paging


            int numberOfPage = products.size() / 9;
            model.addAttribute("numberOfPage", numberOfPage);
            model.addAttribute("products", products);
            return "user/products";
        }
    }

    @RequestMapping(value = {"/mobilephonesf"}, method = RequestMethod.GET)
    public String laptopsFilterByPrice(Model model,
                                       @RequestParam(value = "from", required = false) Integer from,
                                       @RequestParam(value = "to", required = false) Integer to,
                                       @RequestParam(value = "location", required = false) String location) {
        //filter product have id
        List<ProductAgent> productAgents = this.productAgentRepository.findAll();

        if ((from != null) && (to != null)) {
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
        if (location != null) {
            productAgents.removeIf(item -> (!item.getLocation().equals(location)));
            Set<Integer> ids = new HashSet<>();
            for (ProductAgent productAgent : productAgents) {
                ids.add(productAgent.getProduct().getId());
            }
            //set product detail to page
            model.addAttribute("products", this.productRepository.findAllByIdIn(ids));
            return "user/products";
        }
        return "user/products";
    }


    /**
     * Get products
     *
     * @param model
     * @param category
     * @param keyword
     * @param region
     * @param from
     * @param to
     * @return
     */
    @RequestMapping(value = {"/products"})
    public String getProducts(Model model,
                              @RequestParam(value = "category", required = false) String category,
                              @RequestParam(value = "keyword", required = false) String keyword,
                              @RequestParam(value = "region", required = false) String region,
                              @RequestParam(value = "from", required = false) Integer from,
                              @RequestParam(value = "to", required = false) Integer to,
                              @RequestParam(value = "page", required = false) Integer page) {
        List<Product> products;
        keyword=keyword.equals("")?null:keyword;
        category=category.equals("")?null:category;
        region=region.equals("")?null:region;

        if (category != null) {
            if (region != null) {
                if (from != null && to != null) {
                    products = this.productRepository.getProductsByLocationPrice(region, from, to);
                } else {
                    products = this.productRepository.getProductsByLocation(region);
                }
            } else {
                if (from != null && to != null) {
                    products = this.productRepository.getProductsByPrice(from, to);
                } else {
                    products = this.productRepository.findAll();
                }
            }
        } else {
            if (region != null) {
                if (from != null && to != null) {
                    products = this.productRepository.getProductsByLocationPriceKeyword(region, keyword, from, to);
                } else {
                    products = this.productRepository.getProductsByLocationKeyword(region, keyword);
                }
            } else {
                if (from != null && to != null) {
                    products = this.productRepository.getProductsByPriceKeyword(keyword, from, to);
                } else {
                    products = this.productRepository.getProductsByKeyword(keyword);
                }
            }
        }
        //process paging
        int size = products.size();
        final int productPerPage = 9;
        int numberOfPage = products.size() / productPerPage;
        if (numberOfPage * productPerPage < products.size()) {
            numberOfPage++;
        }
        int start = productPerPage * (page - 1);
        int end = start + productPerPage;
        if (end > size) {
            end = size;
        }
        products = products.subList(start, end);
        model.addAttribute("products", products);
        model.addAttribute("numberOfPage", numberOfPage);
        //end of process paging
        if (category != null) {
            model.addAttribute("category", category);
        }
        if (keyword != null) {
            model.addAttribute("keyword", keyword);
        }
        if (region != null) {
            model.addAttribute("region", region);
        }
        if (from != null) {
            model.addAttribute("from", from);
        }
        if (to  != null) {
            model.addAttribute("to", to);
        }
        model.addAttribute("currentPage", page);

        //process products top rating
        List<Product> _products = this.productRepository.findAll();
        List<Product> productsTopRating = new ArrayList<>();
        List<Product> productsTopRatingTemp = _products;
        //process filter products have rating in top 3
        productsTopRatingTemp.sort((Product _product, Product product2) -> {
            if (_product.getRating() > product2.getRating())
                return -1;
            if (_product.getRating() < product2.getRating())
                return 1;
            return 0;
        });
        int indexLoopTopProduct = 0;
        for (Product product1 : productsTopRatingTemp) {
            if (indexLoopTopProduct == 5) {
                break;
            }
            productsTopRating.add(product1);
            indexLoopTopProduct++;
        }
        model.addAttribute("productsTopRating", productsTopRating);
        return "user/_products";
    }
}


