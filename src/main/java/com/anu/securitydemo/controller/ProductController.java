package com.anu.securitydemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {
    private record Product(int productId, String productName, float productPrice){
    }
    List<Product> products = new ArrayList<>();

    @GetMapping
    public List<Product> getProducts(){
        log.info("inside localhost:8080/product");
        return products;
    }

        @PostMapping
        public Product addProduct(@RequestBody Product product){
            return (products.add(product)) ?  product :  null;
        }

}
