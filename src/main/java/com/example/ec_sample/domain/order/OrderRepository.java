package com.example.ec_sample.domain.order;

import com.example.ec_sample.domain.product.Product;
import com.example.ec_sample.domain.user.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);
}

