package com.example.ec_sample.service.order;

import com.example.ec_sample.domain.order.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public Order findById(Long id){
         return orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("注文が存在しません"));
    }

    public int calculateTotalAmount(Order order) {
        return order.getOrderItems().stream()
                .mapToInt(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
