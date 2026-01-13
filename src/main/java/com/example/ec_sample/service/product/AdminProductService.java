package com.example.ec_sample.service.product;

import com.example.ec_sample.domain.product.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import static com.example.ec_sample.service.product.ProductSpecifications.*;
import static com.example.ec_sample.service.product.ProductSpecifications.inStock;
import static com.example.ec_sample.service.product.ProductSpecifications.priceGte;
import static com.example.ec_sample.service.product.ProductSpecifications.priceLte;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductRepository productRepository;
    private final String uploadDir = "images/";  //プロジェクト外の安全な保存場所に画像を保存する

    public List<Product> searchProducts(String sort,
                                        Category category,
                                        Period period,
                                        Brand brand){
        //ソート
        Sort sortOrder = Sort.by("id").descending();
        if ("id_asc".equals(sort)){
            sortOrder = Sort.by("id");
        }else if ("date_asc".equals(sort)){
            sortOrder = Sort.by("createdAt");
        }else if ("date_desc".equals(sort)){
            sortOrder = Sort.by("createdAt").descending();
        }

        Specification<Product> spec = hasCategory(category)
                .and(hasPeriod(period))
                .and(hasBrand(brand));

        return productRepository.findAll(spec,sortOrder);
    }

    public Product save (Product product){
        productRepository.save(product);
        return product;
    }


    public void attachImageToProduct (Long productId, MultipartFile file) throws IOException {

        // 1. 既存商品を取得
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("指定された商品が見つかりません: id=" + productId));

        // 2. ファイル名をユニークに(他と絶対に被らない名前にする)
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // 3. 保存パスを作成
        Path filePath = Paths.get(uploadDir + fileName);
        Files.createDirectories(filePath.getParent());  //フォルダがなかったら作る

        // 4. 画像ファイルを保存
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 5. 商品に画像パスをセット
        product.setImagePath("/images/" + fileName);

        // 6. DBへ保存（更新）
        productRepository.save(product);
    }

    public void delete (Long id){
        productRepository.deleteById(id);
    }
}
