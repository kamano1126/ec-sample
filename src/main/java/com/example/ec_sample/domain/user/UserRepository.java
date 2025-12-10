package com.example.ec_sample.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long>{

    //すでにあるメールアドレスをチェックする
    boolean existsByEmail(String email);

}
