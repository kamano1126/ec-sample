package com.example.ec_sample.service;

import com.example.ec_sample.domain.user.User;
import com.example.ec_sample.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(User user){
        if (userRepository.existsByEmail(user.getEmail())){
            throw new IllegalArgumentException("このメールアドレスはすでに登録されています");
        }

        //パスワードをハッシュ化する
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return null;
        }

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            return null;
        }

        return user;

    }




}
