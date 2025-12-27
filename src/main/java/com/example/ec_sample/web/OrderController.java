package com.example.ec_sample.web;

import com.example.ec_sample.domain.user.User;
import com.example.ec_sample.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order/confirm")
    public String confirmOrder(HttpSession session){

        Map<Long,Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()){
            return "redirect:/cart";
        }
        
        User user = (User) session.getAttribute("loginUser");

        orderService.createOrder(user,cart);

        session.removeAttribute("cart");

        return "redirect:/order/complete";
    }

    @GetMapping("/order/complete")
    public String orderComplete(){
        return "order/complete";
    }
}
