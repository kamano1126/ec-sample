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
    private final String uploadDir = "C:/upload-images/";  //プロジェクト外の安全な保存場所に画像を保存する


    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findByID(Long id){
        return productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("指定された商品が見つかりません: id=" +id));
    }

    public void attachImageToProduct(Long productId, MultipartFile file) throws IOException {

        // 1. 既存商品を取得
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("指定された商品が見つかりません: id=" + productId));

        // 2. ファイル名をユニークに(他と絶対に被らない名前にする)
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // 3. 保存パスを作成
        Path filePath = Paths.get(uploadDir + fileName);
        Files.createDirectories(filePath.getParent());

        // 4. 画像ファイルを保存
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 5. 商品に画像パスをセット
        product.setImagePath("/images/" + fileName);

        // 6. DBへ保存（更新）
        productRepository.save(product);
    }

}
