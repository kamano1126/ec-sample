package com.example.ec_sample.web.admin;


import com.example.ec_sample.domain.order.Order;
import com.example.ec_sample.domain.user.User;
import com.example.ec_sample.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/admin/order/{id}")
    public String orderDetail(@PathVariable("id") Long orderId,
                              HttpSession session,
                              Model model){

        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null || !loginUser.getIsAdmin()){
            return "redirect:/";
        }

        Order order = orderService.findById(orderId);
        model.addAttribute("order",order);

        model.addAttribute("totalAmount",orderService.calculateTotalAmount(order));
        return "admin/order/detail";
    }

    @PostMapping("/admin/order/{id}/cancel")
    public String cancelOrder(@PathVariable Long id,
                              HttpSession session) {

        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null || !loginUser.getIsAdmin()) {
            return "redirect:/";
        }

        orderService.cancelOrder(id);

        return "redirect:/admin/order/" + id;
    }

    @PostMapping("/admin/order/{id}/ship")
    public String shipOrder(@PathVariable Long id,
                            HttpSession session) {

        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null || !loginUser.getIsAdmin()) {
            return "redirect:/";
        }

        orderService.markAsShipped(id);

        return "redirect:/admin/order/" + id;
    }


}
