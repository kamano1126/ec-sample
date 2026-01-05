package com.example.ec_sample.service;

import com.example.ec_sample.domain.order.*;
import com.example.ec_sample.domain.product.Product;
import com.example.ec_sample.domain.product.ProductRepository;
import com.example.ec_sample.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public Order createOrder(User user, Map<Long, Integer> cart) {

        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(Status.CREATED);

        int totalAmount = 0;

        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {

            Long productId = entry.getKey();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("商品が存在しません"));

            Integer quantity = entry.getValue();

            if (product.getStock() < quantity) {
                throw new IllegalStateException("在庫不足です");
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setPrice(product.getPrice());
            item.setQuantity(quantity);

            order.getOrderItems().add(item);

            totalAmount += product.getPrice() * quantity;
            product.setStock(product.getStock() - quantity);
        }

        order.setTotalAmount(totalAmount);

        return orderRepository.save(order);
    }

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

}
