package com.example.ec_sample.web.upload;

import com.example.ec_sample.domain.product.Product;
import com.example.ec_sample.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

// これは画像をアップロードするためのクラス

@Controller
@RequiredArgsConstructor
public class UploadController {

    private final ProductService productService;

    @GetMapping("/products/upload")
    public String showUploadForm(UploadForm form){
        return "product/upload";
    }

    @PostMapping("/products/upload")
    public String uploadImage(
            @Valid UploadForm form,
            BindingResult bindingResult
    )throws Exception{
        if (bindingResult.hasErrors()){
            return "products/upload";
        }

        productService.attachImageToProduct(form.getProductId(),form.getFile());
        return "redirect:/products/" + form.getProductId();
    }
}
