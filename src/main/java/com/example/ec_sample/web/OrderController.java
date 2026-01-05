package com.example.ec_sample.web;

import com.example.ec_sample.domain.product.Product;
import com.example.ec_sample.domain.user.User;
import com.example.ec_sample.service.OrderService;
import com.example.ec_sample.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;

    @PostMapping("/order/confirm")
    public String confirmOrder(HttpSession session,
                               RedirectAttributes redirectAttributes){

        Map<Long,Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()){
            redirectAttributes.addFlashAttribute("toast","cartNull");
            return "redirect:/cart";
        }

        User user = (User) session.getAttribute("loginUser");

        try{
            orderService.createOrder(user,cart);
        }catch (IllegalStateException e){
            redirectAttributes.addFlashAttribute("toast","warning");
            return "redirect:/cart";
        }

        session.removeAttribute("cart");
        return "redirect:/order/complete";
    }

    @GetMapping("/order/complete")
    public String orderComplete(){
        return "order/complete";
    }
}
