package com.example.ec_sample.domain.order;

import com.example.ec_sample.domain.product.Product;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Select("select * from order where id = #{orderId}")
    Product findById(long orderId);
}
