package com.example.ec_sample.web.order;


import com.example.ec_sample.domain.order.Order;
import com.example.ec_sample.domain.order.Status;
import com.example.ec_sample.domain.user.User;
import com.example.ec_sample.service.order.AdminOrderService;
import com.example.ec_sample.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;
    private final AdminOrderService adminOrderService;

    @GetMapping("/admin/order/list")
    public String adminGetList(HttpSession session,
                               @RequestParam(required = false) String sort,
                               @RequestParam(required = false) Status status,
                               Model model){

        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null || !loginUser.getIsAdmin()){
            return "redirect:/";
        }

        List<Order> orders = adminOrderService.getAllOrders();
        if(orders == null || orders.isEmpty()){
            model.addAttribute("isEmpty",true);
            return "admin/order/list";
        }

        model.addAttribute("orders",adminOrderService.searchOrder(sort,status));
        model.addAttribute("sort",sort);
        model.addAttribute("status",status);

        model.addAttribute("statuses",Status.values());

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

        adminOrderService.cancelOrder(id);

        return "redirect:/admin/order/" + id;
    }

    @PostMapping("/admin/order/{id}/ship")
    public String shipOrder(@PathVariable Long id,
                            HttpSession session) {

        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null || !loginUser.getIsAdmin()) {
            return "redirect:/";
        }

        adminOrderService.markAsShipped(id);

        return "redirect:/admin/order/" + id;
    }


}
