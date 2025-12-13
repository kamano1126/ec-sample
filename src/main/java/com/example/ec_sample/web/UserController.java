package com.example.ec_sample.web;

import com.example.ec_sample.domain.user.User;
import com.example.ec_sample.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        model.addAttribute("user",new User());
        return "user/register";
    }
/*
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

 */

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        System.out.println("【DEBUG】UserController の POST /users/register が呼ばれました！");
        System.out.println("【DEBUG】リダイレクト先は /users/register/success のはずです！");



        return "redirect:/users/register/success";
    }

    @GetMapping("/register/success")
    public String registerSuccess() {
        return "user/success";
    }

}
