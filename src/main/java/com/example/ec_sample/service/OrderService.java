package com.example.ec_sample.service;

import com.example.ec_sample.domain.order.*;
import com.example.ec_sample.domain.product.Product;
import com.example.ec_sample.domain.product.ProductRepository;
import com.example.ec_sample.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderPepository;
    private final OrderItemRepository orderItemPepository;
    private final ProductRepository productPepository;

    public Order createOrder(User user, Map<Long, Integer> cart) {

        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(Status.CREATED);

        int totalAmount = 0;

        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {

            Long productId = entry.getKey();
            Integer quantity = entry.getValue();

            Product product = productPepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("商品が存在しません"));

            if (product.getStock() < quantity) {
                throw new IllegalStateException("在庫不足です");
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setPrice(product.getPrice());
            item.setQuantity(quantity);

            order.getOrderItems().add(item); // ← ここが超重要

            totalAmount += product.getPrice() * quantity;
            product.setStock(product.getStock() - quantity);
        }

        order.setTotalAmount(totalAmount);

        // 🔥 Order を1回保存するだけ
        return orderPepository.save(order);
    }
}
