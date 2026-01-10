package com.example.ec_sample.service.order;
import com.example.ec_sample.domain.order.*;
import com.example.ec_sample.domain.product.Product;
import com.example.ec_sample.domain.product.ProductRepository;
import com.example.ec_sample.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.example.ec_sample.service.order.OrderSpecifications.hasStatus;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("注文が存在しません"));

        // 発送済み・キャンセル済みは不可
        if (order.getStatus() == Status.SHIPPED ||
                order.getStatus() == Status.CANCELLED) {
            throw new IllegalStateException("この注文はキャンセルできません");
        }

        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
        }

        order.setStatus(Status.CANCELLED);
    }

    public void markAsShipped(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("注文が存在しません"));

        if(order.getStatus() != Status.PAID){
            throw new IllegalStateException("発送できない状態です");
        }

        order.setStatus(Status.SHIPPED);
    }

    public List<Order> searchOrder(String sort, Status status){

        Sort sortOrder = Sort.by("createdAt");
        if ("status_asc".equals(sort)){
            sortOrder =Sort.by("status").ascending();
        }

        Specification<Order> spec = hasStatus(status);

        return orderRepository.findAll(spec,sortOrder);
    }
}
