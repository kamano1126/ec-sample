package com.example.ec_sample.service.product;

import com.example.ec_sample.domain.product.Brand;
import com.example.ec_sample.domain.product.Category;
import com.example.ec_sample.domain.product.Period;
import com.example.ec_sample.domain.product.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {

    // カテゴリ
    public static Specification<Product> hasCategory(Category category) {
        return (root, query, cb) ->
                category == null ? null : cb.equal(root.get("category"), category);
    }

    // ブランド
    public static Specification<Product> hasBrand(Brand brand) {
        return (root, query, cb) ->
                brand == null ? null : cb.equal(root.get("brand"), brand);
    }

    // 年代
    public static Specification<Product> hasPeriod(Period period){
        return (root, query, cb) ->
                period == null ? null : cb.equal(root.get("period"),period);
    }

    // 在庫ありのみ
    public static Specification<Product> inStock(Boolean inStockOnly) {
        return (root, query, cb) -> {
            if (Boolean.TRUE.equals(inStockOnly)) {
                return cb.greaterThan(root.get("stock"), 0);
            }
            return null;
        };
    }

    // 最低価格
    public static Specification<Product> priceGte(Integer min) {
        return (root, query, cb) ->
                min == null ? null : cb.greaterThanOrEqualTo(root.get("price"), min);
    }

    // 最高価格
    public static Specification<Product> priceLte(Integer max) {
        return (root, query, cb) ->
                max == null ? null : cb.lessThanOrEqualTo(root.get("price"), max);
    }
}
