package com.pricecompare.controller;

import com.pricecompare.entities.Product;
import com.pricecompare.repositories.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class ProductController {
    private final ProductRepository productRepository;

    @Autowired
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @RequestMapping(value = {"/mobilephones"}, method = RequestMethod.GET)
    public String mobilephones(Model model) {
        List<Product> products = this.productRepository.findAll();
        model.addAttribute("products", this.productRepository.findAll());
        return "user/products";
    }


}
