package com.example.ec_sample.domain.order;

import com.example.ec_sample.domain.product.Product;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Select("select * from product where id = #{productId}")
    Product findById(long productId);
}
