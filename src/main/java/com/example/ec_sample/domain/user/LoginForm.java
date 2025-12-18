package com.example.ec_sample.domain.user;

import jakarta.validation.constraints.NotBlank;

public class LoginForm {


    @NotBlank(message = "商品名は必須です")
    private String name;

    private String password;
}
