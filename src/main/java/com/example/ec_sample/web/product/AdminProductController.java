package com.example.ec_sample.web.product;

import com.example.ec_sample.domain.product.Brand;
import com.example.ec_sample.domain.product.Category;
import com.example.ec_sample.domain.product.Period;
import com.example.ec_sample.domain.product.Product;
import com.example.ec_sample.domain.user.User;
import com.example.ec_sample.service.product.AdminProductService;
import com.example.ec_sample.service.product.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminProductController{

    private final ProductService productService;
    private final AdminProductService adminProductService;


    //一覧
    @GetMapping("/products")
    public String showProductList(@RequestParam(required = false) String sort,
                                  @RequestParam(required = false) Category category,
                                  @RequestParam(required = false) Period period,
                                  @RequestParam(required = false) Brand brand,
                                  Model model){

        model.addAttribute("products",adminProductService.searchProducts(sort,category,period,brand));

        model.addAttribute("sort",sort);
        model.addAttribute("category",category);
        model.addAttribute("period",period);
        model.addAttribute("brand",brand);

        model.addAttribute("categories",Category.values());
        model.addAttribute("periods",Period.values());
        model.addAttribute("brands",Brand.values());

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


        Product saved = adminProductService.save(product);


        if(!image.isEmpty()){
            adminProductService.attachImageToProduct(saved.getId(),image);
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




        //if-画像が選択されていたら差し替え(編集POST)
        if(image != null && !image.isEmpty()){
            adminProductService.attachImageToProduct(saved.getId(), image);
        }

        adminProductService.save(saved);
        return "redirect:/admin/products";
    }


    @GetMapping("/products/new")
    public String showProductForm(HttpSession session) {

        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null || !"ROLE_ADMIN".equals(loginUser.getRole())) {
            return "redirect:/?toast=error";
        }

        return "admin/product-form";
    }


    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null || !loginUser.getIsAdmin()) {
            redirectAttributes.addFlashAttribute("toast","warning");
            return "redirect:/";
        }

        redirectAttributes.addFlashAttribute("toast","delete");
        adminProductService.delete(id);
        return "redirect:/admin/products";
    }




}
