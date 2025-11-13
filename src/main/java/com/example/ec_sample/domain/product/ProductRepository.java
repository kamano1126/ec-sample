package com.example.ec_sample.domain.product;
//DBとのやりとりを担当するクラス（Springが自動で実装してくれます）
import org.springframework.data.jpa.repository.JpaRepository;
import org.apache.ibatis.annotations.Select;


public interface ProductRepository extends JpaRepository<Product, Long> {
    @Select("select * from product where id = #{productId}")
    Product findById(long productId);
}
