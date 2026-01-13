package com.example.ec_sample.service.product;

import com.example.ec_sample.domain.product.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.ec_sample.service.product.ProductSpecifications.*;
import static com.example.ec_sample.service.product.ProductSpecifications.inStock;
import static com.example.ec_sample.service.product.ProductSpecifications.priceGte;
import static com.example.ec_sample.service.product.ProductSpecifications.priceLte;

@Service
@RequiredArgsConstructor
public class UserProductService {

    private final ProductRepository productRepository;

    public List<Product> searchProducts(String sort,
                                        Category category,
                                        Period period,
                                        Brand brand,
                                        Boolean inStockOnly,
                                        Integer minPrice,
                                        Integer maxPrice){

        //ソート
        Sort sortOrder = Sort.by("id").descending();

        if ("price_asc".equals(sort)){
            sortOrder = Sort.by("price");
        }else if ("price_desc".equals(sort)) {
            sortOrder = Sort.by("price").descending();
        }else if ("oldest".equals(sort)){
            sortOrder = Sort.by("id");
        }

        Specification<Product> spec = hasCategory(category)
                .and(hasPeriod(period))
                .and(hasBrand(brand))
                .and(inStock(inStockOnly))
                .and(priceGte(minPrice))
                .and(priceLte(maxPrice));

        return productRepository.findAll(spec,sortOrder);


    }

}
