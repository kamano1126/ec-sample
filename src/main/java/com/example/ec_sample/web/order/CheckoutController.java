package com.example.ec_sample.web.order;

import com.example.ec_sample.domain.user.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CheckoutController {

    @PostMapping("/checkout/start")
    public String startCheckout(HttpSession session,
                                RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("loginUser");

        if (user == null) {
            // チェックアウト途中フラグ
            session.setAttribute("checkoutInProgress", true);
            redirectAttributes.addFlashAttribute("toast", "loginRequired");
            return "redirect:/checkout/account";
        }

        return "forward:/order/confirm/submit";
    }

    @GetMapping("/checkout/account")
    public String checkoutAccount(HttpSession session) {

        Boolean checkout = (Boolean) session.getAttribute("checkoutInProgress");

        if (!Boolean.TRUE.equals(checkout)) {
            return "redirect:/";
        }

        return "checkout/account";
    }

    @GetMapping("/order/confirm")
    public String blockConfirmGet() {
        return "redirect:/"; // or /cart
    }

}
