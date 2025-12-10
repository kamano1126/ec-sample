package com.example.ec_sample.web;

import com.example.ec_sample.domain.user.User;
import com.example.ec_sample.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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

        return "redirect:/register/success";
    }
}
