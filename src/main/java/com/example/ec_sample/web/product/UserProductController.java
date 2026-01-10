package com.example.ec_sample.web.product;

import com.example.ec_sample.domain.product.Brand;
import com.example.ec_sample.domain.product.Category;
import com.example.ec_sample.domain.product.Period;
import com.example.ec_sample.service.product.UserProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserProductController {

    private final UserProductService userProductService;

    @GetMapping("/products")
    public String showProductList(@RequestParam(required = false) String sort,
                                  @RequestParam(required = false) Category category,
                                  @RequestParam(required = false) Period period,
                                  @RequestParam(required = false) Brand brand,
                                  @RequestParam(required = false) Boolean inStock,
                                  @RequestParam(required = false) Integer minPrice,
                                  @RequestParam(required = false) Integer maxPrice,
                                  Model model) {

        var products = userProductService.searchProducts(sort,category,period,brand,inStock,minPrice,maxPrice);
        boolean isEmpty = products.isEmpty();

        model.addAttribute("products",products);
        model.addAttribute("isEmpty",products.isEmpty());

        model.addAttribute("sort",sort);
        model.addAttribute("category",category);
        model.addAttribute("period",period);
        model.addAttribute("brand",brand);
        model.addAttribute("inStock",inStock);
        model.addAttribute("minPrice",minPrice);
        model.addAttribute("maxPrice",maxPrice);

        model.addAttribute("categories",Category.values());
        model.addAttribute("periods",Period.values());
        model.addAttribute("brands",Brand.values());

        return "products/list";
    }


}
