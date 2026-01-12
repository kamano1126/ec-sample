package com.example.ec_sample.service.product;

import com.example.ec_sample.domain.product.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final String uploadDir = "images/";


            @Transactional
            public void decreaseStock (Long productId,int quantity){

                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new IllegalArgumentException("商品が存在しません"));

                if (product.getStock() < quantity) {
                    throw new IllegalStateException("在庫不足です");
                }

                product.setStock(product.getStock() - quantity);
            }

            public List<Product> findAll () {
                return productRepository.findAll();
            }

            public Product findByID (Long id){
                return productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("指定された商品が見つかりません: id=" + id));
            }
        }