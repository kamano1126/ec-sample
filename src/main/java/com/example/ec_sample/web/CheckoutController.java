package com.example.ec_sample.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CheckoutController {

    @GetMapping("/checkout/account")
    public String checkoutAccount(HttpSession session) {

        Boolean checkout = (Boolean) session.getAttribute("checkoutInProgress");

        if (!Boolean.TRUE.equals(checkout)) {
            return "redirect:/";
        }

        return "checkout/account";
    }
}
