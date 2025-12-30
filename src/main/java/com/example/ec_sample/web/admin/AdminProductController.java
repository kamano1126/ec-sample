package com.example.ec_sample.web.admin;

import com.example.ec_sample.domain.product.Brand;
import com.example.ec_sample.domain.product.Category;
import com.example.ec_sample.domain.product.Period;
import com.example.ec_sample.domain.product.Product;
import com.example.ec_sample.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminProductController{

    private final ProductService productService;

    //一覧
    @GetMapping("/products")
    public String showProductList(Model model){
        model.addAttribute("products",productService.findAll());
        return "admin/products";
    }

     //追加GET
    @GetMapping("/new")
    public String newProduct(Model model){
        model.addAttribute("product", new Product());
        return "admin/new";
    }

     //追加POST
    @PostMapping("/create")
    public String createProduct(@Valid@ModelAttribute("product") Product product,
                                BindingResult bindingResult,
                                @RequestParam ("image")MultipartFile image,
                                Model model) throws Exception {

        if (image == null || image.isEmpty()){
            bindingResult.reject("image.required","画像は必須です");
                model.addAttribute("categories",Category.values());
                model.addAttribute("brands",Brand.values());
                model.addAttribute("periods",Period.values());
            return "admin/new";
        }

        if(bindingResult.hasErrors()){
            model.addAttribute("categories",Category.values());
            model.addAttribute("brands",Brand.values());
            model.addAttribute("periods",Period.values());
            return "admin/new";
        }


        Product saved = productService.save(product);


        if(!image.isEmpty()){
            productService.attachImageToProduct(saved.getId(),image);
        }
        return "redirect:/admin/products";
    }

     //編集GET
    @GetMapping("/{id}/edit")
    public String editProduct(@PathVariable Long id,
                              Model model){
        model.addAttribute("product", productService.findByID(id));
        model.addAttribute("categories", Category.values());
        model.addAttribute("brands", Brand.values());
        model.addAttribute("periods", Period.values());
        return "admin/edit";
    }
     //編集POST
    @PostMapping("/{id}/update")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute ("product")Product formProduct,
                                BindingResult bindingResult,
                                @RequestParam(value = "image",required = false) MultipartFile image,
                                Model model) throws Exception{

        if(bindingResult.hasErrors()){
            model.addAttribute("categories", Category.values());
            model.addAttribute("brands", Brand.values());
            model.addAttribute("periods", Period.values());
            return "admin/edit";
        }
        Product saved = productService.findByID(id);

        //値を上書き(編集POST)
        saved.setName(formProduct.getName());
        saved.setPrice(formProduct.getPrice());
        saved.setDescription(formProduct.getDescription());
        saved.setStock(formProduct.getStock());
        saved.setSize(formProduct.getSize());
        saved.setCategory(formProduct.getCategory());
        saved.setPeriod(formProduct.getPeriod());
        saved.setBrand(formProduct.getBrand());
        saved.setJoinDate(formProduct.getJoinDate());


        if (formProduct.getId() == null && (image == null || image.isEmpty())){
            bindingResult.reject("image.required","画像は必須です");
            return "admin/edit";
        }

        //if-画像が選択されていたら差し替え(編集POST)
        if(image != null && !image.isEmpty()){
            productService.attachImageToProduct(saved.getId(), image);
        }

        return "redirect:/admin/products";
    }




}
