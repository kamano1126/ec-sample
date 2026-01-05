package com.example.ec_sample.web.admin;


import com.example.ec_sample.domain.order.Order;
import com.example.ec_sample.domain.user.User;
import com.example.ec_sample.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping("/admin/order/list")
    public String adminGetList(HttpSession session,
                               Model model){
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null || !loginUser.getIsAdmin()){
            return "redirect:/";
        }

        List<Order> orders = orderService.getAllOrders();

        model.addAttribute("orders",orders);
        return "admin/order/list";
    }


}
