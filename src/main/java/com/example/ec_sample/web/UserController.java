package com.example.ec_sample.web;

import com.example.ec_sample.domain.user.User;
import com.example.ec_sample.service.ProductService;
import com.example.ec_sample.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        model.addAttribute("user",new User());
        return "user/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid User user,
                               BindingResult bindingResult,
                               Model model){
        if(bindingResult.hasErrors()){
            return "user/register";
        }

        try{
            userService.register(user);
        }catch (IllegalArgumentException e){
            model.addAttribute("emailError",e.getMessage());
            return "user/register";
        }
        System.out.println("POST /register を通過");
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
                            Model model) {

        User loginUser = userService.login(email,password);

        //emailとpasswordの値がtrueならホーム画面に戻る
        if (loginUser != null) {
            session.setAttribute("loginUser",loginUser.getId());
            return "redirect:/?toast=login";
        }

        //エラーフラグが立っていたらログイン画面に戻しエラーメッセージを表示する
        model.addAttribute("loginError", "ログインに失敗しました");

        //redirectしたらmodel(loginError)が消えてしまう為redirectしない
        return "user/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();//セッションを破棄する
        return "redirect:/";
    }

    @GetMapping("/register/success")
    public String registerSuccess() {
        return "user/success";
    }

    @GetMapping("/admin/products/new")
    public String showProductForm(HttpSession session) {

        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null || !"ROLE_ADMIN".equals(loginUser.getRole())) {
            return "redirect:/?toast=error";
        }

        return "admin/product-form";
    }


    @PostMapping("/admin/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id,
                                HttpSession session) {

        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null || !loginUser.getIsAdmin()) {
            return "redirect:/?toast=warning";
        }

        productService.delete(id);
        return "redirect:/products?toast=success";
    }

}
