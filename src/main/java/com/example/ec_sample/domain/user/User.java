package com.example.ec_sample.domain.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import com.example.ec_sample.domain.user.Role;


@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "ユーザー名は必須です")
    private String username;

    @Email(message = "メールアドレスの形式が不正です")
    @NotBlank(message = "メールアドレスは必須です")
    private String email;

    @NotBlank(message = "パスワードは必須です")
    private String password;

    private Boolean isAdmin;

    @Enumerated(EnumType.STRING)
    private Role role;

}
