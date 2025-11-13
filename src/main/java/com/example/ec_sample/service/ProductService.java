package com.example.ec_sample.service;
//コントローラから呼び出されて、商品データを取得します。
import com.example.ec_sample.domain.product.Product;
import com.example.ec_sample.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findByID(Long id){
        return productRepository.findByID(id).orElseThrow(() -> new IllegalArgumentException("指定された商品が見つかりません: id=" +id));
    }
}
