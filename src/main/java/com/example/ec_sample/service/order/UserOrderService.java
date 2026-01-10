package com.example.ec_sample.service.order;

import com.example.ec_sample.domain.order.Order;
import com.example.ec_sample.domain.order.OrderItem;
import com.example.ec_sample.domain.order.OrderRepository;
import com.example.ec_sample.domain.order.Status;
import com.example.ec_sample.domain.product.Product;
import com.example.ec_sample.domain.product.ProductRepository;
import com.example.ec_sample.domain.user.User;
import com.example.ec_sample.service.product.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class UserOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final OrderService orderService;

    public Long createOrder(User user, Map<Long, Integer> cart) {

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
        orderRepository.save(order);
        return order.getId();
    }

    public void markAsPaid(Long orderId){
        Order order = orderService.findById(orderId);

        if (order.getStatus() != Status.CREATED){
            throw new IllegalStateException("支払い済みです");
        }

        order.setStatus(Status.PAID);

        for (OrderItem item:order.getOrderItems()) {
            productService.decreaseStock(
                    item.getProduct().getId(),
                    item.getQuantity());
        }
    }

    public void cancelOrderByUser(Long orderId, User loginUser) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("注文が存在しません"));

        // ★ 本人確認（最重要）
        if (!order.getUser().getId().equals(loginUser.getId())) {
            throw new SecurityException("他人の注文はキャンセルできません");
        }

        // ステータスチェック
        if (order.getStatus() == Status.SHIPPED ||
                order.getStatus() == Status.CANCELLED) {
            throw new IllegalStateException("この注文はキャンセルできません");
        }

        // 在庫を戻す
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
        }

        order.setStatus(Status.CANCELLED);
    }

    public List<Order> findOrdersByUser(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public Order findOrderForUser(Long orderId, User user) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("注文が存在しません"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new SecurityException("他人の注文は参照できません");
        }

        return order;
    }
}
