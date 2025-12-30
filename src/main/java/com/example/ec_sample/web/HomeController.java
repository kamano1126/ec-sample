package com.example.ec_sample.web;


import com.example.ec_sample.domain.product.Product;
import com.example.ec_sample.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class HomeController {

    private final ProductService productService;

    public HomeController(ProductService productService){
        this.productService = productService;
    }
    @GetMapping("/")
    public String home(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "index";
    }

    @GetMapping("/products/{id}")
    public String showDetail(@PathVariable Long id,
                             Model model){
        Product product = productService.findByID(id);
        model.addAttribute("product",product);
        return "products/detail";
    }
}
