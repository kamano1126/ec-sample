package com.example.ec_sample.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    //すでにあるメールアドレスをチェックする
    User findByEmail(String email);

    boolean existsByEmail(String email);
}