package com.example.cordo.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BaseSecurity {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public BaseSecurity(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public String encodePassword(String password) {
        String realPassword = bCryptPasswordEncoder.encode(password);
        return realPassword;
    }
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        boolean babuy = bCryptPasswordEncoder.matches(rawPassword,encodedPassword);
        return babuy;
    }
}
