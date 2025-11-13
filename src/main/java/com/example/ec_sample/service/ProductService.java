package com.example.ec_sample.service;
//コントローラから呼び出されて、商品データを取得します。
import com.example.ec_sample.domain.product.Product;
import com.example.ec_sample.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final String uploadDir = "src/main/resources/static/uploads/"; // 保存先フォルダ


    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findByID(Long id){
        return productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("指定された商品が見つかりません: id=" +id));
    }

    public void saveProduct(String name,  MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);

        Files.createDirectories(filePath.getParent());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Product product = new Product();
        product.setName(name);
        product.setImagePath("/uploads/" + fileName); // ← HTMLで表示用パスを保存
        productRepository.save(product);
    }
}
