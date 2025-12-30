package com.example.ec_sample.web;
// "/" にアクセスしたら商品一覧ページを表示します
import com.example.ec_sample.domain.product.Product;
import com.example.ec_sample.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public String showProductList(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "products/list";  // templates/products/list.html を表示
    }

    @GetMapping("/upload")
    public String showUploadForm() {
        return "products/upload";
    }

    @PostMapping("/upload")
    public String uploadProduct(
            @RequestParam("productId") Long productId,
            @RequestParam("file") MultipartFile file) throws Exception {

        productService.attachImageToProduct(productId, file);
        return "redirect:/products/" + productId;
    }


}
