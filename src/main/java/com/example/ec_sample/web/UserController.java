package com.example.ec_sample.web;

import com.example.ec_sample.domain.user.User;
import com.example.ec_sample.domain.user.UserRepository;
import com.example.ec_sample.service.ProductService;
import com.example.ec_sample.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ProductService productService;
    private final UserRepository userRepository;

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        model.addAttribute("user",new User());
        return "user/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid User user,
                               BindingResult bindingResult,
                               Model model,
                               HttpSession session){
        if(bindingResult.hasErrors()){
            return "user/register";
        }

        try{
            userService.register(user);

            Boolean checkout = (Boolean) session.getAttribute("checkoutInProgress");

            if(Boolean.TRUE.equals(checkout)){
                session.removeAttribute("checkoutInProgress");
                return "redirect:/users/register/success";
            }
        }catch (IllegalArgumentException e){
            model.addAttribute("emailError",e.getMessage());
            return "user/register";
        }
        return "redirect:/users/register/success";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model){
        return "user/login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session,
                            Model model,
                            RedirectAttributes redirectAttributes) {

        User loginUser = userService.login(email,password);

        //emailとpasswordの値がtrueならホーム画面に戻る
        if (loginUser != null) {
            User freshUser = userRepository.findById(loginUser.getId()).get();
            session.setAttribute("loginUser", freshUser);

            Boolean checkout = (Boolean) session.getAttribute("checkoutInProgress");

            if (Boolean.TRUE.equals(checkout)){
                session.removeAttribute("checkoutInProgress");
                return "forward:/order/confirm/submit";
            }

            redirectAttributes.addFlashAttribute("toast", "login");
            return "redirect:/";
        }

        //エラーフラグが立っていたらログイン画面に戻しエラーメッセージを表示する
        model.addAttribute("loginError", "ログインに失敗しました");

        //redirectしたらmodel(loginError)が消えてしまう為redirectしない
        return "user/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session,
                         RedirectAttributes redirectAttributes){
        session.invalidate();//セッションを破棄する
        redirectAttributes.addFlashAttribute("toast","logout");
        return "redirect:/";
    }

    @GetMapping("/register/success")
    public String registerSuccess() {
        return "user/success";
    }

}
