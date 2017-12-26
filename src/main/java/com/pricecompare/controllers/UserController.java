package com.pricecompare.controllers;

import com.pricecompare.entities.Product;
import com.pricecompare.entities.ProductAgent;
import com.pricecompare.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private ProductRepository productRepository;

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String index(Model model) {
        List<Product> products = this.productRepository.findAll();
        List<Product> productsTopRating = new ArrayList<>();
        List<Product> productsTopRatingTemp = products;
        List<Product> productsTopView = new ArrayList<>();
        List<Product> productsTopViewTemp = products;
        //process filter products have rating in top 3
        productsTopRatingTemp.sort((Product product, Product product2) -> {
            if (product.getRating() > product2.getRating())
                return -1;
            if (product.getRating() < product2.getRating())
                return 1;
            return 0;
        });
        int indexLoopTopProduct = 0;
        for (Product product : productsTopRatingTemp) {
            if (indexLoopTopProduct == 3) {
                break;
            }
            productsTopRating.add(product);
            indexLoopTopProduct++;
        }

        model.addAttribute("productsTopRating", productsTopRating);

        //process top view products
        productsTopViewTemp.sort((Product product, Product product2) -> {
            if (product.getVisit_count() > product2.getVisit_count())
                return -1;
            if (product.getVisit_count() < product2.getVisit_count())
                return 1;
            return 0;
        });
        indexLoopTopProduct = 0;
        for (Product product : productsTopViewTemp) {
            if (indexLoopTopProduct == 4) {
                break;
            }
            productsTopView.add(product);
            indexLoopTopProduct++;
        }
        model.addAttribute("productsTopView", productsTopView);
        return "user/index";
    }

    void countAgentForProduct(List<Product> products, List<ProductAgent> productAgents){
        for(Product product : products){
            for(ProductAgent productAgent : )
        }
    }

}
