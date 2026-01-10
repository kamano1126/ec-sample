package com.example.ec_sample.web.order;

import com.example.ec_sample.domain.order.Order;
import com.example.ec_sample.domain.user.User;
import com.example.ec_sample.service.order.AdminOrderService;
import com.example.ec_sample.service.order.OrderService;
import com.example.ec_sample.service.order.UserOrderService;
import com.example.ec_sample.service.product.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserOrderController {

    private final UserOrderService userOrderService;

    @PostMapping("/order/confirm/submit")
    public String confirmOrder(HttpSession session,
                               RedirectAttributes redirectAttributes){

        User user = (User) session.getAttribute("loginUser");

        if(user == null){
            redirectAttributes.addFlashAttribute("toast","loginRequired");
            return "redirect:/login";
        }

        Map<Long,Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()){
            redirectAttributes.addFlashAttribute("toast","cartNull");
            return "redirect:/cart";
        }

        try{
            Long orderId = userOrderService.createOrder(user,cart);
            userOrderService.markAsPaid(orderId);
        }catch (IllegalStateException e){
            redirectAttributes.addFlashAttribute("toast","warning");
            return "redirect:/cart";
        }

        session.removeAttribute("cart");
        return "redirect:/order/complete";
    }

    @GetMapping("/orders")
    public String orderHistory(HttpSession session, org.springframework.ui.Model model) {

        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        List<Order> orders = userOrderService.findOrdersByUser(loginUser);

        model.addAttribute("orders", orders);
        return "order/history";
    }

    @GetMapping("/order/{id}")
    public String orderDetail(@PathVariable Long id,
                              HttpSession session,
                              Model model) {

        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        Order order = userOrderService.findOrderForUser(id, loginUser);

        model.addAttribute("order", order);
        return "order/detail";
    }

    @GetMapping("/order/complete")
    public String orderComplete(){
        return "order/complete";
    }

    @PostMapping("/order/{id}/cancel")
    public String cancelOrder(@PathVariable Long id,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {

        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        userOrderService.cancelOrderByUser(id, loginUser);

        redirectAttributes.addFlashAttribute("toast","cancel");
        return "redirect:/order/" + id;
    }
}
